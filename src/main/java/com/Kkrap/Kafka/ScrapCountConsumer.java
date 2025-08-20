package com.Kkrap.Kafka;

import com.Kkrap.Repository.FoldersRepository;
import com.Kkrap.Security.SecurityConfig;
import com.Kkrap.Service.FoldersDocument.FoldersDocumentManagerService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
@RequiredArgsConstructor
public class ScrapCountConsumer {

    private final FoldersRepository foldersRepository;
    private static final Logger logger = LoggerFactory.getLogger(ScrapCountConsumer.class);
    private final FoldersDocumentManagerService foldersDocumentManagerService;

    @KafkaListener(topics = "folder-scrap-topic", groupId = "folder-consumer")
    @Transactional
    public void consumeScrapCount(String message) {
        try {
            String[] parts = message.split(":");
            Long folderId = Long.parseLong(parts[0]);
            Long increment = Long.parseLong(parts[1]);

            foldersRepository.incrementScrapCount(folderId, increment);

            logger.info("[Kafka] ScrapCount updated - folderId=" + folderId + ", +"+ increment);

            // 2) 커밋 성공 후 ES 재색인 (공개만 색인 / 비공개면 삭제는 Manager가 처리)
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override public void afterCommit() {
                    try {
                        foldersDocumentManagerService.updateFolderDocumentById(folderId);
                        logger.info("[ES] Reindexed after scrapCount update - folderId={}", folderId);
                    } catch (Exception e) {
                        logger.error("[ES] Reindex failed after scrapCount update - folderId={}", folderId, e);
                    }
                }
            });

        } catch (Exception e) {
            logger.info("[Kafka] Error processing scrapCount message: " + message);
            throw new RuntimeException("Kafka ScrapCountConsuemr failed : ", e);
        }
    }
}

