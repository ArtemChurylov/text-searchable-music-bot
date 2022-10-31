package com.music.bot.exeptions;

public class DownloadFileException extends Throwable {

    public DownloadFileException(String message) {
        super(message);
    }

    public DownloadFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
