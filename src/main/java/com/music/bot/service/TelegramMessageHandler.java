package com.music.bot.service;

import com.music.bot.exeptions.DownloadFileException;
import com.music.bot.responses.FileInfoResponse;
import com.music.bot.userstate.UserState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Audio;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.music.bot.layout.KeyboardMarkup.KEYBOARD_MENU;
import static com.music.bot.userstate.UserState.SHOW_MENU;
import static com.music.bot.userstate.UserStateStore.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramMessageHandler {

    private final MenuHandler menuHandler;
    private final RestTemplate restTemplate;
    private final MusicKafkaService musicKafkaService;
    private static final String GET_FILE_PATH_URL = "https://api.telegram.org/bot%s/getFile?file_id=%s";
    private static final String DOWNLOAD_FILE_URL = "https://api.telegram.org/file/bot%s/%s";
    private static final String SEND_AUDIO_URL = "https://api.telegram.org/bot%s/sendAudio?chat_id=%s&audio=%s";
    private static final String SEARCH_AUDIO_URL = "http://localhost:8085/music-bot-service/audio?lyrics=%s";

    @Value("${telegram.bot-token}")
    private String BOT_TOKEN;
    @Value("${music.full-path}")
    private String MUSIC_PATH;

    public BotApiMethod<?> handleUpdate(Update update) {
        Message message = update.getMessage();
        if (message != null) {
            log.debug("Received update from {}", update.getMessage().getChat().getFirstName());
            if ("/start".equals(message.getText())) {
                return handleStartCommand(message);
            }
            return handleMessage(message);
        }
        return null;
    }

    private BotApiMethod<?> handleStartCommand(Message message) {
        Long chatId = message.getChatId();
        registerUser(chatId);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Hi, " + message.getChat().getFirstName());
        sendMessage.setReplyMarkup(KEYBOARD_MENU);
        return sendMessage;
    }

    private BotApiMethod<?> handleMessage(Message message) {
        Long chatId = message.getChatId();
        UserState userState = getUserState(chatId);

        switch (userState) {
            case SHOW_MENU -> {
                return menuHandler.handle(message);
            }
            case UPLOAD_SONG -> {
                return handleUploadSongState(message);
            }
            case SEARCH_BY_LYRICS -> {
                return handleSearchByLyrics(message);
            }
            default -> {
                return new SendMessage(message.getChatId().toString(), "No such option");
            }
        }
    }

    private SendMessage handleUploadSongState(Message message) {
        Long chatId = message.getChatId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        Audio audio = message.getAudio();
        if (audio == null) {
            sendMessage.setText("Your file extension is wrong");
            return sendMessage;
        }

        try {
            Path path = downloadFile(audio);
            musicKafkaService.sendMetaData(path, audio);
            sendMessage.setText("File was successfully uploaded\nIt can take some time to appear in your list");
            updateUserState(chatId, SHOW_MENU);
        } catch (DownloadFileException e) {
            sendMessage.setText("Something went wrong, cannot download your file");
        }
        return sendMessage;
    }

    private SendMessage handleSearchByLyrics(Message message) {
        Long chatId = message.getChatId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        try {
            String audioID = restTemplate.getForObject(String.format(SEARCH_AUDIO_URL, message.getText()), String.class);
            restTemplate.execute(String.format(SEND_AUDIO_URL, BOT_TOKEN, chatId, audioID), HttpMethod.POST, null, null);
            updateUserState(chatId, SHOW_MENU);
            sendMessage.setText("Here is your song!");
            return sendMessage;
        } catch (RestClientException e) {
            sendMessage.setText("Cannot find such audio");
            return sendMessage;
        }
    }

    private Path downloadFile(Audio audio) throws DownloadFileException {
        FileInfoResponse infoResponse = restTemplate.getForObject(String.format(GET_FILE_PATH_URL, BOT_TOKEN, audio.getFileId()), FileInfoResponse.class);
        ResponseExtractor<ResponseEntity<byte[]>> responseEntityExtractor = restTemplate.responseEntityExtractor(byte[].class);
        ResponseEntity<byte[]> response = restTemplate.execute(String.format(DOWNLOAD_FILE_URL, BOT_TOKEN, infoResponse.getResult().getFilePath()), HttpMethod.GET, null, responseEntityExtractor);
        if (response == null || response.getBody() == null) {
            throw new DownloadFileException("Cannot download file " + audio.getFileId() + ", no response from Telegram Server");
        }
        Path filePath = Paths.get(MUSIC_PATH, audio.getFileName());
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath.toString())) {
            fileOutputStream.write(response.getBody());
            return filePath;
        } catch (IOException e) {
            throw new DownloadFileException("Cannot download file " + audio.getFileId(), e);
        }
    }
}
