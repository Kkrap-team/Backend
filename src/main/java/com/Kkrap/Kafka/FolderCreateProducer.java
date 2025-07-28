package com.Kkrap.Kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FolderCreateProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String TOPIC = "folder-create-topic";

    public void sendFolderCreatedEvent(String message) {
        kafkaTemplate.send(TOPIC, message);
        System.out.println("[Kafka Producer] 폴더 생성 이벤트 발행: " + message);

    }
}