package com.music.bot.controller;

import com.music.bot.service.AudioMusicBot;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.music.bot.controller.RestMapping.BOT_API;


@RequestMapping(BOT_API)
@RestController
@RequiredArgsConstructor
public class BotController {

    private final AudioMusicBot audioMusicBot;

    @PostMapping
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return audioMusicBot.onWebhookUpdateReceived(update);
    }
}
