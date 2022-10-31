package com.music.bot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "telegram")
public class TelegramBotProperties {

    private String botUsername;
    private String botToken;
    private String botPath;
}
