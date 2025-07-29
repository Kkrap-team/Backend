package com.Kkrap.Scheduler;

import com.Kkrap.Entity.Folders;
import com.Kkrap.Entity.Links;
import com.Kkrap.Service.FolderLink.FoldersLinksService;
import com.Kkrap.Service.FolderLink.FoldersService;
import com.Kkrap.Service.FoldersDocument.FoldersDocumentService;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

public class ElasticsearchIndexScheduler {
    private final FoldersService foldersService;
    private final FoldersLinksService foldersLinksService;
    private final FoldersDocumentService foldersDocumentService;

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

    // 매 시간 정각마다 실행
    @Scheduled(cron = "0 0 */3 * * *") // 초 분 시 일 월 요일
    public void hourlyIndexAllVisibleFolders() {
        List<Folders> folders = foldersService.findByVisibleTrue();

        folders.forEach(folder -> {
            Links link = foldersLinksService.getFirstLinkByFolder(folder).orElse(null);
            foldersDocumentService.indexNewFolder(folder, link);
        });

        System.out.println("[Elasticsearch] 매시간 색인 완료! 폴더 수: " + folders.size());
    }
}
