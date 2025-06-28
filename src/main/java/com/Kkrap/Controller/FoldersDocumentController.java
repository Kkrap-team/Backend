package com.Kkrap.Controller;

import com.Kkrap.ElasticSearch.FoldersDocument;
import com.Kkrap.ResponseDTO.ElasticSearchRankingResponse;
import com.Kkrap.Service.FoldersDocument.FoldersDocumentManagerService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/folders-search")
public class FoldersDocumentController {

    private final FoldersDocumentManagerService foldersDocumentManagerService;

    public FoldersDocumentController(FoldersDocumentManagerService foldersDocumentManagerService){
        this.foldersDocumentManagerService = foldersDocumentManagerService;
    }

    @GetMapping("/all")
    @Operation(summary = "색인 된 거 전부 조회", description = "색인된 거 전부 조회")
    public List<FoldersDocument> getAllDocuments() {
        return foldersDocumentManagerService.getAllDocuments();
    }

    @DeleteMapping("/all")
    @Operation(summary = "색인 된 거 전부 삭제", description = "색인 된 거 전부 삭제")
    public String deleteAll() {
        foldersDocumentManagerService.deleteAllDocuments();
        return "모든 색인 삭제 완료!";
    }

    @PostMapping("/migrate")
    @Operation(summary = "DB에 저장된 모든 폴더 공개인 것만 넣어주기", description = "DB에 저장된 모든 폴더 공개인 것만 넣어주기")
    public String migrate() {
        foldersDocumentManagerService.migrateAllFoldersToElasticsearch();
        return "마이그레이션 완료!";
    }

    @GetMapping("/search")
    @Operation(summary = "검색바에서 폴더 검색", description = "검색바에서 폴더 검색")
    public ResponseEntity<List<FoldersDocument>> searchFolders(@RequestParam String keyword) {
        return ResponseEntity.ok(foldersDocumentManagerService.searchFolders(keyword));
    }

    @GetMapping("/rankings")
    @Operation(summary = "주간 랭킹 조회", description = "viewCount, likesCount Top10 반환")
    public ResponseEntity<ElasticSearchRankingResponse> getWeeklyRankings() {
        return ResponseEntity.ok(foldersDocumentManagerService.getWeeklyRankings());
    }



//    @GetMapping("/search")
//    public List<FoldersDocument> searchFolders(@RequestParam String keyword) {
//        return foldersDocumentManagerService.searchFolders(keyword);
//    }

//    @PostMapping("/index")
//    public FoldersDocument indexFolder(@RequestBody Folders folder) {
//        return foldersDocumentManagerService.indexNewFolder(folder);
//    }
}
