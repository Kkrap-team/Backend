package com.Kkrap.Service.FoldersDocument;

import com.Kkrap.ElasticSearch.FoldersDocument;
import com.Kkrap.Entity.Folders;
import com.Kkrap.Entity.Links;
import com.Kkrap.Entity.Users;
import com.Kkrap.ResponseDTO.ElasticSearchRankingResponse;
import com.Kkrap.Service.FolderLink.FoldersLinksService;
import com.Kkrap.Service.FolderLink.FoldersManagerService;
import com.Kkrap.Service.FolderLink.FoldersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public List<FoldersDocument> searchFolders(String keyword) {
        return foldersDocumentService.searchByName(keyword);
    }

    public ElasticSearchRankingResponse getWeeklyRankings() {
        List<FoldersDocument> topViewCount = foldersDocumentService.getTop10ViewCountLastWeek();
        List<FoldersDocument> topScrapCount = foldersDocumentService.getTop10ScrapCountLastWeek();

        return new ElasticSearchRankingResponse(topViewCount, topScrapCount);
    }

    //조회수 업데이트 후 색인 업데이트
    @Transactional(readOnly = true)
    public void updateFolderDocumentById(Long folderId) {
        Folders folder = foldersService.findById(folderId);
        Links link = foldersLinksService.getFirstLinkByFolder(folder).orElse(null);
        foldersDocumentService.indexNewFolder(folder, link);
    }


    public void updateUserInfoInFolderDocuments(Users users, List<Folders> userFolders) {

        // 색인 업데이트
        userFolders.forEach(folder -> {
            Links link = foldersLinksService.getFirstLinkByFolder(folder).orElse(null);
            foldersDocumentService.indexNewFolder(folder, link);
        });
    }





}
