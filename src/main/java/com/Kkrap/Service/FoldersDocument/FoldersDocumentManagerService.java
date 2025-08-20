package com.Kkrap.Service.FoldersDocument;

import com.Kkrap.ElasticSearch.FoldersDocument;
import com.Kkrap.Entity.Folders;
import com.Kkrap.Entity.Links;
import com.Kkrap.Entity.Users;
import com.Kkrap.Exception.FoldersNotFoundException;
import com.Kkrap.ResponseDTO.ElasticSearchRankingResponse;
import com.Kkrap.Service.FolderLink.FoldersLinksService;
import com.Kkrap.Service.FolderLink.FoldersManagerService;
import com.Kkrap.Service.FolderLink.FoldersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FoldersDocumentManagerService {
    private final FoldersDocumentService foldersDocumentService;

    private final FoldersService foldersService;

    private final FoldersLinksService foldersLinksService;

    private static final Logger logger = LoggerFactory.getLogger(FoldersDocumentManagerService.class);

    public FoldersDocumentManagerService(FoldersDocumentService foldersDocumentService,
                                         FoldersService foldersService,
                                         FoldersLinksService foldersLinksService){
        this.foldersDocumentService = foldersDocumentService;
        this.foldersService = foldersService;
        this.foldersLinksService = foldersLinksService;
    }


    public List<FoldersDocument> getAllDocuments() {
        return foldersDocumentService.findAll();
    }

    public void migrateAllFoldersToElasticsearch() {

        List<Folders> allFolders =foldersService.findByVisibleTrue();

        allFolders.forEach(folder -> {
                    Links link = foldersLinksService.getFirstLinkByFolder(folder).orElse(null);
                    foldersDocumentService.indexNewFolder(folder, link);
                });

        logger.info("모든 visible = true 폴더가 Elasticsearch에 색인되었습니다!");
    }


    public void deleteAllDocuments() {
        foldersDocumentService.deleteAll();
    }

//    public List<FoldersDocument> searchFoldersTop10(String keyword) {
//        return foldersDocumentService.findTop10ByFolderNameContainingIgnoreCase(keyword);
//    }

    public List<FoldersDocument> searchFoldersTop10(String rawKeyword) {
        String q = normalizeForEsPrefixSearch(rawKeyword);
        if (q.isBlank()) return List.of();
        Pageable pageable = PageRequest.of(0, 10); // size 여기서 제어
        return foldersDocumentService.searchByKeywordSimple(q, pageable);
    }

    private String normalizeForEsPrefixSearch(String keyword) {
        if (keyword == null) return "";
        String trimmed = keyword.trim().replaceAll("\\s+", " ");
        if (trimmed.isEmpty()) return "";
        String[] tokens = trimmed.split(" ");
        tokens[tokens.length - 1] = tokens[tokens.length - 1] + "*"; // 접두 매치
        return String.join(" ", tokens);
    }

    public List<FoldersDocument> searchFolders(String keyword) {
        return foldersDocumentService.findByFolderNameContainingIgnoreCase(keyword);
    }

    public ElasticSearchRankingResponse getWeeklyRankings() {
        List<FoldersDocument> topViewCount = foldersDocumentService.getTop10ViewCountLastWeek();
        List<FoldersDocument> topScrapCount = foldersDocumentService.getTop10ScrapCountLastWeek();

        return new ElasticSearchRankingResponse(topViewCount, topScrapCount);
    }

    //조회수 업데이트 후 색인 업데이트
//    @Transactional
//    public void updateFolderDocumentById(Long folderId) {
//        Folders folder = foldersService.findById(folderId);
//        Links link = foldersLinksService.getFirstLinkByFolder(folder).orElse(null);
//        foldersDocumentService.indexNewFolder(folder, link);
//    }


    public void updateUserInfoInFolderDocuments(Users users, List<Folders> userFolders) {

        // 색인 업데이트
        userFolders.forEach(folder -> {
            Links link = foldersLinksService.getFirstLinkByFolder(folder).orElse(null);
            foldersDocumentService.indexNewFolder(folder, link);
        });
    }



    /**
     * 단일 폴더 색인 갱신.
     * - 폴더가 삭제되었거나 없으면 무시 (로그만 남김)
     * - 폴더가 비공개(visible=false)이면 ES에서 제거
     * - 대표 링크(첫 링크) 재계산 후 upsert
     */
    @Transactional(readOnly = true)
    public void updateFolderDocumentById(Long folderId) {
        try {
            Folders folder = foldersService.findById(folderId);

            if (!folder.isVisible()) {
                logger.info("[ES] folderId={} not visible. delete from index.", folderId);
                safeDeleteFromEs(folderId);
                return;
            }

            Links firstLink = foldersLinksService.getFirstLinkByFolder(folder).orElse(null);
            foldersDocumentService.indexNewFolder(folder, firstLink);
            logger.info("[ES] Reindexed folderId={}", folderId);

        } catch (FoldersNotFoundException e) {
            logger.warn("[ES] folderId={} not found in DB. delete from index.", folderId);
            safeDeleteFromEs(folderId);
        } catch (Exception e) {
            logger.error("[ES] Failed to reindex folderId={}", folderId, e);
        }
    }

    /**
     * 여러 폴더를 한 번에 갱신(중복 제거).
     * afterCommit에서 source/target 같이 넘길 때 편의 메서드.
     */
//    @Transactional(readOnly = true)
//    public void updateFolderDocumentsByIds(Collection<Long> folderIds) {
//        if (folderIds == null || folderIds.isEmpty()) return;
//        Set<Long> distinct = new HashSet<>(folderIds);
//        distinct.forEach(this::updateFolderDocumentById);
//    }
    @Transactional(readOnly = true)
    public void reindexUserVisibleFolders(Long userId) {
        List<Folders> folders = foldersService.findByUserIdAndVisibleTrue(userId);
        for (Folders folder : folders) {
            Links firstLink = foldersLinksService.getFirstLinkByFolder(folder).orElse(null);
            foldersDocumentService.indexNewFolder(folder, firstLink); // upsert
        }
        logger.info("[ES] reindexed user visible folders. userId={}", userId);
    }

    public void safeDeleteFromEs(Long folderId) {
        try {
            foldersDocumentService.deleteById(folderId);
        } catch (Exception ex) {
            // 존재하지 않아도 예외 무시
            logger.debug("[ES] delete skip or failed for folderId={}: {}", folderId, ex.getMessage());
        }
    }



}
