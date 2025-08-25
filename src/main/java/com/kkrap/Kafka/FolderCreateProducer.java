package com.kkrap.Kafka;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FolderCreateProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String TOPIC = "folder-create-topic";
    private static final Logger logger = LoggerFactory.getLogger(FolderViewConsumer.class);

    public void sendFolderCreatedEvent(String message) {
        kafkaTemplate.send(TOPIC, message);
        logger.info("[Kafka Producer] 폴더 생성 이벤트 발행: " + message);

    }
}