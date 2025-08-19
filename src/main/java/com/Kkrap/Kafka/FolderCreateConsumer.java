package com.Kkrap.Kafka;

import com.Kkrap.Entity.Folders;
import com.Kkrap.Entity.Links;
import com.Kkrap.Service.FolderLink.FoldersLinksService;
import com.Kkrap.Service.FolderLink.FoldersService;
import com.Kkrap.Service.FoldersDocument.FoldersDocumentManagerService;
import com.Kkrap.Service.FoldersDocument.FoldersDocumentService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.links.Link;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class FolderCreateConsumer {

    private final FoldersDocumentService foldersDocumentService;
    private final FoldersService foldersService;

    private final FoldersLinksService foldersLinksService;

    private static final Logger logger = LoggerFactory.getLogger(FolderCreateConsumer.class);

    public FolderCreateConsumer(FoldersDocumentService foldersDocumentService,
                                FoldersService foldersService,
                                FoldersLinksService foldersLinksService){
        this.foldersDocumentService = foldersDocumentService;
        this.foldersService = foldersService;
        this.foldersLinksService = foldersLinksService;
    }


    @KafkaListener(topics = "folder-create-topic", groupId = "folder-consumer")
    public void consumeFolderCreate(String message) {
        logger.info("[Kafka Consumer] 폴더 생성 이벤트 수신: " + message);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(message);

            Long folderId = jsonNode.get("folderId").asLong(); // 이 문자열이 null 일 때 ""

            Folders folders = foldersService.findById(folderId);
            Links link = foldersLinksService.getFirstLinkByFolder(folders).orElse(null);
            // 서비스 계층에 위임
            foldersDocumentService.indexNewFolder(folders, link);

            logger.info("[Kafka Consumer] Elasticsearch 색인 추가 완료: " + folderId);

        } catch (Exception e) {
            logger.error("[Kafka Consumer] 메시지 처리 실패: " + message);
            e.printStackTrace();
        }
    }
}