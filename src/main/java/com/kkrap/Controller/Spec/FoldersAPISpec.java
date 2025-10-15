package com.kkrap.Controller.Spec;

import com.kkrap.RequestDTO.*;
import com.kkrap.ResponseDTO.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Folders", description = "폴더 관련 API Endpoint")
public interface FoldersAPISpec {

    //내가 가지고 있는 모든 폴더 안에 있는 링크 4개만 -> 썸네일 전용
    @GetMapping("/users/thumbnails/{targetUserId}")
    @Operation(summary = "모든 폴더 및 링크 최신순 조회", description = "내꺼 상대방꺼 전부 됨")
    ResponseEntity<UserFoldersWithSharedResponse> getAllFoldersWithLinks(Authentication authentication, @PathVariable("targetUserId") Long targetUserId);


    @GetMapping("/users/{folderId}/links/{targetUserId}")
    @Operation(summary = "하나 폴더 링크 전체 조회", description = "내꺼 상대방꺼 전부 됨")
    ResponseEntity<OneFoldersAllLinksResponse> getOneFolderWithLinks(
            Authentication authentication,
            @PathVariable("folderId") Long folderId, @PathVariable("targetUserId") Long targetUserId);

    @GetMapping("/users/noauth/{folderId}/links/{targetUserId}")
    @Operation(summary = "비회원 user 하나 폴더 링크 전체 조회", description = "")
    ResponseEntity<OneFoldersAllLinksResponse> getNoAuthOneFolderWithLinks(
            @PathVariable("folderId") Long folderId, @PathVariable("targetUserId") Long targetUserId);


    //Create - 폴더를 만드는 api
    @PostMapping("/users/folders")
    @Operation(summary = "사용자 폴더 생성", description = "한 명의 사용자가 폴더 생성")
    ResponseEntity<FoldersResponse> createUserFolder(
            Authentication authentication,
            @RequestBody FoldersCreateRequest foldersCreateRequest);

    //Delete
    @DeleteMapping("/users/folders")
    @Operation(summary = "사용자 폴더 삭제", description = "한 명의 사용자가 폴더 삭제하게 되는데 모든 링크가 저장된 폴더는 삭제 되게 하면 안 됩니다.")
    ResponseEntity<FoldersResponse> deleteUserFolder(
            Authentication authentication,
            @RequestBody FoldersDeleteRequest foldersDeleteRequest);


    @PatchMapping("/{folderId}") // 폴더 제목, 설명, 공개비공개 수정
    @Operation(summary = "폴더 제목/설명/공개비공개 수정", description = "userId와 folderId가 필요")
    ResponseEntity<FoldersResponse> updateFolderMetadata(
            Authentication authentication,
            @RequestBody FoldersUpdateRequest request);


    //scrap
    @PostMapping("/users/scrap")
    @Operation(summary = "상대방 폴더 스크랩", description = "상대방 폴더를 내 폴더로 스크랩")
    ResponseEntity<OneFoldersAllLinksResponse> scrapFolder(
            Authentication authentication,
            @RequestBody FoldersScrapRequest request);


    @GetMapping("/users/scroll")
    @Operation(
            summary = "내 공개 폴더 무한 스크롤",
            description = "내가 만든 폴더 중 visible=true인 것만 최신순으로 키셋 페이지네이션"
    )
    ResponseEntity<List<OneFoldersAllLinksResponse>> scrollVisibleFolders(
            Authentication authentication,
            @RequestParam(defaultValue = "20") Long size,
            @RequestParam(required = false) String cursor
    );

    @GetMapping("/noauth/rankings")
    @Operation(
            summary = "최근 90일 랭킹",
            description = "최근 90일 내 생성된 공개 폴더 중 조회수/스크랩수 Top10 반환"
    )
    ResponseEntity<FoldersRankingResponse> get90dFoldersRankings();





}
