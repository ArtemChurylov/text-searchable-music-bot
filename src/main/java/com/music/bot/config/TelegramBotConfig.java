package com.music.bot.config;

import com.music.bot.service.TelegramMessageHandler;
import com.music.bot.service.WshMusicBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

@Configuration
public class TelegramBotConfig {

    @Bean
    public WshMusicBot wshMusicBot(TelegramBotProperties properties, TelegramMessageHandler messageHandler) {
        WshMusicBot bot = new WshMusicBot(SetWebhook.builder().url(properties.getBotPath()).build(), messageHandler);
        bot.setBotToken(properties.getBotToken());
        bot.setBotUsername(properties.getBotUsername());
        bot.setBotPath(properties.getBotPath());
        return bot;
    }
}
