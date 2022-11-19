package com.music.bot.service;

import com.music.bot.userstate.UserState;
import com.music.bot.userstate.UserStateStore;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
public class MenuHandler {

    public BotApiMethod<?> handle(Message message) {
        if (message.getText() == null) {
            return new SendMessage(message.getChatId().toString(), "No such option");
        }
        Long chatId = message.getChatId();
        switch (message.getText()) {
            case "Upload song" -> {
                UserStateStore.updateUserState(chatId, UserState.UPLOAD_SONG);
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.setText("Send your song, buddy!\n\nSupported languages:\nEnglish\nSpanish\nFrench\nGerman\nItalian\nPortuguese\nDutch");
                return sendMessage;
            }
            case "Search by lyrics" -> {
                UserStateStore.updateUserState(chatId, UserState.SEARCH_BY_LYRICS);
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.setText("Provide lyrics piece");
                return sendMessage;
            }
            case "Get list of songs" -> {
                return sendListOfSongs(chatId);
            }
            default -> {
                return new SendMessage(message.getChatId().toString(), "No such option");
            }
        }
    }

    private BotApiMethod<?> sendListOfSongs(Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Not implemented");
        return sendMessage;
    }
}
