package com.Kkrap.Scheduler;

import com.Kkrap.Entity.Folders;
import com.Kkrap.Entity.Links;
import com.Kkrap.Service.FolderLink.FoldersLinksService;
import com.Kkrap.Service.FolderLink.FoldersService;
import com.Kkrap.Service.FoldersDocument.FoldersDocumentService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ElasticsearchIndexScheduler {
    private final FoldersService foldersService;
    private final FoldersLinksService foldersLinksService;
    private final FoldersDocumentService foldersDocumentService;

    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchIndexScheduler.class);

    public ElasticsearchIndexScheduler(
            FoldersService foldersService,
            FoldersLinksService foldersLinksService,
            FoldersDocumentService foldersDocumentService
        )
    {
        this.foldersService = foldersService;
        this.foldersLinksService = foldersLinksService;
        this.foldersDocumentService = foldersDocumentService;
    }

    @Scheduled(fixedRate = 1000 * 60 * 60 * 3) // 1시간마다 실행
    public void hourlyIndexAllVisibleFolders() {
        logger.info("[FeedFlush] Elasticsearch 색인 스케줄러 시작됨");

        List<Folders> folders = foldersService.findByVisibleTrue();

        folders.forEach(folder -> {
            Links link = foldersLinksService.getFirstLinkByFolder(folder).orElse(null);
            foldersDocumentService.indexNewFolder(folder, link);
        });

        logger.info("[Elasticsearch] 매 3시간 색인 완료! 폴더 수: {}", folders.size());
    }
}
