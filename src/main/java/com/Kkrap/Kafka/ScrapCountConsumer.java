package com.Kkrap.Kafka;

import com.Kkrap.Repository.FoldersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ScrapCountConsumer {

    private final FoldersRepository foldersRepository;

    @KafkaListener(topics = "folder-scrap-topic", groupId = "folder-consumer")
    @Transactional
    public void consumeScrapCount(String message) {
        try {
            String[] parts = message.split(":");
            Long folderId = Long.parseLong(parts[0]);
            Long increment = Long.parseLong(parts[1]);

            foldersRepository.incrementScrapCount(folderId, increment);

            System.out.println("[Kafka] ScrapCount updated - folderId=" + folderId + ", +"+ increment);
        } catch (Exception e) {
            System.err.println("[Kafka] Error processing scrapCount message: " + message);
            throw new RuntimeException("Kafka ScrapCountConsuemr failed : ", e);
        }
    }
}

