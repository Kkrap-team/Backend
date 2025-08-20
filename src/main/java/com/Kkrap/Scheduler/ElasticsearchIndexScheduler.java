package com.Kkrap.Scheduler;

import com.Kkrap.ElasticSearch.FoldersDocument;
import com.Kkrap.Entity.Folders;
import com.Kkrap.Entity.Links;
import com.Kkrap.Service.FolderLink.FoldersLinksService;
import com.Kkrap.Service.FolderLink.FoldersService;
import com.Kkrap.Service.FoldersDocument.FoldersDocumentManagerService;
import com.Kkrap.Service.FoldersDocument.FoldersDocumentService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ElasticsearchIndexScheduler {
    private static final Logger log = LoggerFactory.getLogger(ElasticsearchIndexScheduler.class);
    private static final int BATCH = 500;

    private final FoldersService foldersService;
    private final FoldersLinksService foldersLinksService;
    private final FoldersDocumentService foldersDocumentService;
    private final FoldersDocumentManagerService foldersDocumentManagerService;

    public ElasticsearchIndexScheduler(
            FoldersService foldersService,
            FoldersLinksService foldersLinksService,
            FoldersDocumentService foldersDocumentService,
            FoldersDocumentManagerService foldersDocumentManagerService
    ) {
        this.foldersService = foldersService;
        this.foldersLinksService = foldersLinksService;
        this.foldersDocumentService = foldersDocumentService;
        this.foldersDocumentManagerService = foldersDocumentManagerService;
    }

    // 주석을 3시간으로 맞추거나 fixedRate를 1시간으로 바꿔주세요.
    @Scheduled(fixedRate = 1000L * 60 * 60 * 1)
    public void reconcileIndex() {
        long t0 = System.currentTimeMillis();
        log.info("[ES Reconcile] 시작");

        // 1) DB의 공개 폴더만 전량 upsert (배치)
        List<Folders> visible = foldersService.findByVisibleTrue();
        int ok = 0, fail = 0;

        for (int i = 0; i < visible.size(); i += BATCH) {
            List<Folders> batch = visible.subList(i, Math.min(i + BATCH, visible.size()));
            for (Folders f : batch) {
                try {
                    Links first = foldersLinksService.getFirstLinkByFolder(f).orElse(null);
                    // Upsert (saveAll 사용 가능하면 대체)
                    foldersDocumentService.indexNewFolder(f, first);
                    ok++;
                } catch (Exception e) {
                    fail++;
                    log.warn("[ES Reconcile] upsert 실패 folderId={}", f.getFolderId(), e);
                }
            }
        }

        // 2) 고아 문서 삭제(ES에는 있으나 DB에는 없거나 비공개)
        try {
            List<FoldersDocument> esAll = foldersDocumentService.findAll(); // 필요 시 페이징
            Set<Long> visibleIds = visible.stream().map(Folders::getFolderId).collect(Collectors.toSet());
            int deleted = 0;
            for (FoldersDocument doc : esAll) {
                Long id = doc.getFolderId();
                if (!visibleIds.contains(id)) {
                    try {
                        // 안전 삭제(없어도 조용히)
                        foldersDocumentManagerService.safeDeleteFromEs(id);
                        deleted++;
                    } catch (Exception ignore) {}
                }
            }
            log.info("[ES Reconcile] upsert ok={}, fail={}, orphanDeleted={}", ok, fail, deleted);
        } catch (Exception e) {
            log.warn("[ES Reconcile] 고아 문서 정리 실패", e);
        }

        log.info("[ES Reconcile] 종료 ({} ms)", System.currentTimeMillis() - t0);
    }
}


//@Component
//public class ElasticsearchIndexScheduler {
//    private final FoldersService foldersService;
//    private final FoldersLinksService foldersLinksService;
//    private final FoldersDocumentService foldersDocumentService;
//
//    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchIndexScheduler.class);
//
//    public ElasticsearchIndexScheduler(
//            FoldersService foldersService,
//            FoldersLinksService foldersLinksService,
//            FoldersDocumentService foldersDocumentService
//        )
//    {
//        this.foldersService = foldersService;
//        this.foldersLinksService = foldersLinksService;
//        this.foldersDocumentService = foldersDocumentService;
//    }
//
//    @Scheduled(fixedRate = 1000 * 60 * 60 * 3) // 1시간마다 실행
//    public void hourlyIndexAllVisibleFolders() {
//        logger.info("[FeedFlush] Elasticsearch 색인 스케줄러 시작됨");
//
//        List<Folders> folders = foldersService.findByVisibleTrue();
//
//        folders.forEach(folder -> {
//            Links link = foldersLinksService.getFirstLinkByFolder(folder).orElse(null);
//            foldersDocumentService.indexNewFolder(folder, link);
//        });
//
//        logger.info("[Elasticsearch] 매 3시간 색인 완료! 폴더 수: {}", folders.size());
//    }
//}
