package com.music.bot.service;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.SpringWebhookBot;

@Slf4j
@Getter
@Setter
public class WshMusicBot extends SpringWebhookBot {

    private String botUsername;
    private String botToken;
    private String botPath;

    private final TelegramMessageHandler messageHandler;

    public WshMusicBot(SetWebhook setWebhook, TelegramMessageHandler messageHandler) {
        super(setWebhook);
        this.messageHandler = messageHandler;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return messageHandler.handleUpdate(update);
    }
}
