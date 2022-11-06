package com.music.bot.service;

import com.music.bot.AudioMetadata;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Audio;

import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class MusicKafkaService {

    private static final String MUSIC_METADATA_TOPIC = "com.music.bot.MusicMetadata";
    private final KafkaTemplate<String, AudioMetadata> kafkaTemplate;

    public void sendMetaData(Path path, Audio audio) {
        AudioMetadata audioMetadata = AudioMetadata.newBuilder()
                .setId(audio.getFileId())
                .setSource(path.toString())
                .setTitle(audio.getTitle())
                .setAuthor(audio.getPerformer())
                .build();
        ProducerRecord<String, AudioMetadata> producerRecord = new ProducerRecord<>(MUSIC_METADATA_TOPIC, audioMetadata.getId(), audioMetadata);
        kafkaTemplate.send(producerRecord);
    }
}
