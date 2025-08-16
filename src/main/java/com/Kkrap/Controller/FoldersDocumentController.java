package com.Kkrap.Controller;

import com.Kkrap.Controller.Spec.FoldersDocumentAPISpec;
import com.Kkrap.ElasticSearch.FoldersDocument;
import com.Kkrap.ResponseDTO.ElasticSearchRankingResponse;
import com.Kkrap.Service.FoldersDocument.FoldersDocumentManagerService;
import io.micrometer.core.annotation.Timed;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Timed(value = "http.controller", extraTags = {"controller","FoldersDocument"})
@RequestMapping("/folders-search")
public class FoldersDocumentController implements FoldersDocumentAPISpec {

    private final FoldersDocumentManagerService foldersDocumentManagerService;

    public FoldersDocumentController(FoldersDocumentManagerService foldersDocumentManagerService){
        this.foldersDocumentManagerService = foldersDocumentManagerService;
    }

    @Override
    public List<FoldersDocument> getAllDocuments() {
        return foldersDocumentManagerService.getAllDocuments();
    }

    @Override
    public String deleteAll() {
        foldersDocumentManagerService.deleteAllDocuments();
        return "모든 색인 삭제 완료!";
    }

    @Override
    public String migrate() {
        foldersDocumentManagerService.migrateAllFoldersToElasticsearch();
        return "마이그레이션 완료!";
    }

    @Override
    public ResponseEntity<List<FoldersDocument>> searchFoldersTop10(Authentication authentication, String keyword) {
        return ResponseEntity.ok(foldersDocumentManagerService.searchFoldersTop10(keyword));
    }

    @Override
    public ResponseEntity<List<FoldersDocument>> searchFolders(Authentication authentication, String keyword) {
        return ResponseEntity.ok(foldersDocumentManagerService.searchFolders(keyword));
    }

    @Override
    public ResponseEntity<ElasticSearchRankingResponse> getWeeklyRankings(Authentication authentication) {
        return ResponseEntity.ok(foldersDocumentManagerService.getWeeklyRankings());
    }


}
