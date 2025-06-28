package com.Kkrap.Kafka;

import com.Kkrap.Service.FoldersDocument.FoldersDocumentManagerService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class FolderCreateConsumer {

    private final FoldersDocumentManagerService foldersDocumentManagerService;

    public FolderCreateConsumer(FoldersDocumentManagerService foldersDocumentManagerService){
        this.foldersDocumentManagerService = foldersDocumentManagerService;
    }


    @KafkaListener(topics = "folder-create-topic", groupId = "folder-consumer")
    public void consumeFolderCreate(String message) {
        System.out.println("[Kafka Consumer] 폴더 생성 이벤트 수신: " + message);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(message);

            Long folderId = jsonNode.get("folderId").asLong();

            // 서비스 계층에 위임
            foldersDocumentManagerService.indexNewFolder(folderId);

            System.out.println("[Kafka Consumer] Elasticsearch 색인 추가 완료: " + folderId);

        } catch (Exception e) {
            System.err.println("[Kafka Consumer] 메시지 처리 실패: " + message);
            e.printStackTrace();
        }
    }
}