package com.Kkrap.Service.FoldersDocument;

import com.Kkrap.ElasticSearch.FoldersDocument;
import com.Kkrap.Entity.Folders;
import com.Kkrap.Entity.Users;
import com.Kkrap.Repository.FoldersDocumentRepository;
import com.Kkrap.ResponseDTO.ElasticSearchRankingResponse;
import com.Kkrap.Service.FolderLink.FoldersService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FoldersDocumentManagerService {
    private final FoldersDocumentService foldersDocumentService;

    private final FoldersService foldersService;

    public FoldersDocumentManagerService(FoldersDocumentService foldersDocumentService, FoldersService foldersService){
        this.foldersDocumentService = foldersDocumentService;
        this.foldersService = foldersService;
    }


    public List<FoldersDocument> getAllDocuments() {
        return foldersDocumentService.findAll();
    }

    public void migrateAllFoldersToElasticsearch() {

        List<Folders> allFolders = foldersService.findAll();

        allFolders.stream()
                .filter(folder -> folder.isVisible())
                .forEach(folder -> foldersDocumentService.indexNewFolder(folder));

        System.out.println("visible = false 인 모든 폴더가 Elasticsearch에 색인되었습니다!");
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
        foldersDocumentService.indexNewFolder(folder);
    }
}
