package com.Kkrap.Controller.Spec;


import com.Kkrap.ElasticSearch.FoldersDocument;
import com.Kkrap.ResponseDTO.ElasticSearchRankingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "FoldersDocument", description = "폴더 검색 / 색인 관련 API Endpoint")
public interface FoldersDocumentAPISpec {
    @GetMapping("/all")
    @Operation(summary = "색인 된 거 전부 조회", description = "색인된 거 전부 조회")
    List<FoldersDocument> getAllDocuments();

    @DeleteMapping("/all")
    @Operation(summary = "색인 된 거 전부 삭제 - 프론트엔드 사용금지", description = "색인 된 거 전부 삭제")
    String deleteAll();

    @PostMapping("/migrate")
    @Operation(summary = "DB에 저장된 모든 폴더 공개인 것만 넣어주기 - 프론트엔드 사용금지", description = "DB에 저장된 모든 폴더 공개인 것만 넣어주기")
    String migrate();

    @GetMapping("/search")
    @Operation(summary = "검색바에서 폴더 검색", description = "검색바에서 폴더 검색")
    ResponseEntity<List<FoldersDocument>> searchFolders(@RequestParam("keyword") String keyword);

    @GetMapping("/rankings")
    @Operation(summary = "주간 랭킹 조회", description = "viewCount, scrapCount Top10 반환")
    ResponseEntity<ElasticSearchRankingResponse> getWeeklyRankings();
}
