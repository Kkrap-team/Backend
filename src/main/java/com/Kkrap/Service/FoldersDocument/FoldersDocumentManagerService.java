package com.Kkrap.Service.FoldersDocument;

import com.Kkrap.ElasticSearch.FoldersDocument;
import com.Kkrap.Entity.Folders;
import com.Kkrap.Repository.FoldersDocumentRepository;
import com.Kkrap.ResponseDTO.ElasticSearchRankingResponse;
import com.Kkrap.Service.FolderLink.FoldersService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoldersDocumentManagerService {
    private final FoldersDocumentService foldersDocumentService;

    private final FoldersService foldersService;

    public FoldersDocumentManagerService(FoldersDocumentService foldersDocumentService, FoldersService foldersService){
        this.foldersDocumentService = foldersDocumentService;
        this.foldersService = foldersService;
    }


    //이거는 우리 백엔드에서 사용할 DB 전체를 색인
    public FoldersDocument indexNewFolder(Folders folder) {
        FoldersDocument doc = FoldersDocument.from(folder);
        return foldersDocumentService.save(doc);
    }

    //폴더 생성 후 -> 카프카에서 이걸 실행
    public void indexNewFolder(Long folderId) {
        Folders folders = foldersService.findById(folderId);
        FoldersDocument doc = FoldersDocument.from(folders);
        foldersDocumentService.save(doc);
    }

    public List<FoldersDocument> getAllDocuments() {
        return foldersDocumentService.findAll();
    }

    public void migrateAllFoldersToElasticsearch() {

        List<Folders> allFolders = foldersService.findAll();

        allFolders.stream()
                .filter(folder -> folder.isVisible())
                .forEach(this::indexNewFolder);

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



}
