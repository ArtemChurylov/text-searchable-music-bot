package com.music.bot.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileInfoResponse {

    @JsonProperty("ok")
    boolean status;
    Result result;

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Result {

        @JsonProperty("file_id")
        String fileId;
        @JsonProperty("file_unique_id")
        String fileUniqueId;
        @JsonProperty("file_size")
        String fileSize;
        @JsonProperty("file_path")
        String filePath;
    }
}

