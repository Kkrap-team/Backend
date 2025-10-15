package com.kkrap.Controller.Spec;


import com.kkrap.ResponseDTO.FoldersRankingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "FoldersDocument", description = "폴더 검색 / 색인 관련 API Endpoint")
public interface FoldersDocumentAPISpec {

    @GetMapping("/noauth/text")
    @Operation(summary = "검색바에서 폴더 검색", description = "검색바에서 폴더 검색")
    ResponseEntity<List<FoldersDocument>> searchFoldersTop10(
            @RequestParam("keyword") String keyword);

    @GetMapping("/noauth/enter")
    @Operation(summary = "검색 초기화 - 새 검색 요청", description = "검색바에서 엔터를 쳐서 처음 검색할 때 실행되는 API")
    ResponseEntity<List<FoldersDocument>> searchFolders(
            @RequestParam("keyword") String keyword);


    @GetMapping("/noauth/rankings")
    @Operation(summary = "주간 랭킹 조회", description = "viewCount, scrapCount Top10 반환")
    ResponseEntity<FoldersRankingResponse> getWeeklyRankings();

    @GetMapping("/noauth/main")
    @Operation(summary = "비회원 main page 폴더 둘러보기", description = "viewCount, scrapCount 각 Top 25개 섞어서 50개 반환 14일 이내에 꺼만 반환")
    ResponseEntity<List<FoldersDocument>> getNoAuthMixedTop();

}
