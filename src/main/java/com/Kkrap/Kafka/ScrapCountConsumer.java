package com.Kkrap.Kafka;

import com.Kkrap.Repository.FoldersRepository;
import com.Kkrap.Security.SecurityConfig;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ScrapCountConsumer {

    private final FoldersRepository foldersRepository;
    private static final Logger logger = LoggerFactory.getLogger(ScrapCountConsumer.class);

    @KafkaListener(topics = "folder-scrap-topic", groupId = "folder-consumer")
    @Transactional
    public void consumeScrapCount(String message) {
        try {
            String[] parts = message.split(":");
            Long folderId = Long.parseLong(parts[0]);
            Long increment = Long.parseLong(parts[1]);

            foldersRepository.incrementScrapCount(folderId, increment);

            logger.info("[Kafka] ScrapCount updated - folderId=" + folderId + ", +"+ increment);
        } catch (Exception e) {
            logger.info("[Kafka] Error processing scrapCount message: " + message);
            throw new RuntimeException("Kafka ScrapCountConsuemr failed : ", e);
        }
    }
}

