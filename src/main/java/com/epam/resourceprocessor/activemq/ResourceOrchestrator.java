package com.epam.resourceprocessor.activemq;

import com.epam.resourceprocessor.entities.Song;
import com.epam.resourceprocessor.services.Mp3FileProcessor;
import com.epam.resourceprocessor.services.SongBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.metadata.Metadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

@Component
@Slf4j
public class ResourceOrchestrator {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Mp3FileProcessor mp3FileProcessor;

    @Autowired
    private SongBuilder songBuilder;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${spring.activemq.queues.resource-get}")
    private String getResourceQueue;

    @Value("${spring.activemq.queues.resource-content}")
    private String resourceContentQueue;

    @Value("${spring.activemq.queues.song-create}")
    private String createSongQueue;

    @Value("${spring.activemq.queues.song-created}")
    private String songCreatedQueue;

    public void processSongCreation(Long resourceId) throws JsonProcessingException {
        log.info("send request to get resource {} to queue", resourceId);
        jmsTemplate.convertAndSend(getResourceQueue, resourceId);
        Object resourceBytes =  jmsTemplate.receiveAndConvert(resourceContentQueue);
        log.info("received resource byte array");
        Metadata metadata = mp3FileProcessor.getSongMetadata(SerializationUtils.serialize(resourceBytes));
        Song song = songBuilder.buildFromMp3File(metadata, resourceId);
        log.info(song.toString());
        jmsTemplate.convertAndSend(createSongQueue, objectMapper.writeValueAsString(song));
        Object createdSongId = jmsTemplate.receiveAndConvert(songCreatedQueue);
        log.info("Song {}: {} was created", createdSongId, song);
    }
}
