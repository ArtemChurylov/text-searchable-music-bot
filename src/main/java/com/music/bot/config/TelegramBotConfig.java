package com.music.bot.config;

import com.music.bot.service.TelegramMessageHandler;
import com.music.bot.service.AudioMusicBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.starter.SpringWebhookBot;

@Configuration
public class TelegramBotConfig {

    @Bean
    public SpringWebhookBot wshMusicBot(TelegramBotProperties properties, TelegramMessageHandler messageHandler) {
        AudioMusicBot bot = new AudioMusicBot(SetWebhook.builder().url(properties.getBotPath()).build(), messageHandler);
        bot.setBotToken(properties.getBotToken());
        bot.setBotUsername(properties.getBotUsername());
        bot.setBotPath(properties.getBotPath());
        return bot;
    }
}
