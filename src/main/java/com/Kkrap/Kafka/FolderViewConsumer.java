package com.Kkrap.Kafka;

import com.Kkrap.Repository.FoldersRepository;
import com.Kkrap.Scheduler.RedisToKafkaScheduler;
import com.Kkrap.Service.FoldersDocument.FoldersDocumentManagerService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FolderViewConsumer {

    private final FoldersRepository foldersRepository;
    private final FoldersDocumentManagerService foldersDocumentManagerService;

    private static final Logger logger = LoggerFactory.getLogger(FolderViewConsumer.class);

    @KafkaListener(topics = "folder-view-topic", groupId = "folder-consumer")
    public void consumeFolderView(String message) {
        try {
            Long folderId = Long.parseLong(message);

            foldersRepository.incrementViewCountById(folderId);
            logger.info("[Kafka Consumer] 폴더 ID " + folderId + " → 조회수 +1");

            foldersDocumentManagerService.updateFolderDocumentById(folderId);

        } catch (Exception e) {
            logger.info("[Kafka Consumer] 메시지 파싱 오류: " + message);
        }
    }
}