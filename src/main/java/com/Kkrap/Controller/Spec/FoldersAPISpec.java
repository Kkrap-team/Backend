package com.Kkrap.Controller.Spec;

import com.Kkrap.RequestDTO.*;
import com.Kkrap.ResponseDTO.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Folders", description = "폴더 관련 API Endpoint")
public interface FoldersAPISpec {
    //사용자가 가지고 있는 모든 폴더와 내안 있는 링크들 같이 조회
    @GetMapping("/users/{userId}/folders")
    @Operation(summary = "사용자의 모든 폴더 및 모든 링크", description = "한 명의 사용자가 가진 모든 폴더와 모든 링크 조회")
    ResponseEntity<UserFoldersWithSharedResponse> getUserAllFoldersWithLinks(
            @Parameter(name = "userId", description = "사용자 ID", required = true, example = "1")
            @PathVariable("userId") Long userId);

    //상대방이 가지고 있는 모든 폴더 안에 있는 링크 4개만 -> 썸네일 전용
    @GetMapping("/users/{userId}/folders/thumbnails")
    @Operation(summary = "상대방의 모든 폴더 및 링크 최신순 4개", description = "상대방 보관함에서 보여줄 썸네일 전용")
    ResponseEntity<UserFoldersWithSharedResponse> getAllFoldersWithTop4LinksByUser(
            @PathVariable("userId") Long userId);


    //내가 가지고 있는 모든 폴더 안에 있는 링크 4개만 -> 썸네일 전용
    @GetMapping("/users/{userId}/folders/thumbnails/me")
    @Operation(summary = "내꺼 모든 폴더 및 링크 최신순 4개", description = "내꺼 보관함에서 보여줄 썸네일 전용")
    ResponseEntity<UserFoldersWithSharedResponse> getMeAllFoldersWithTop4Links(
            @PathVariable("userId") Long userId);


    @GetMapping("/users/{userId}/folders/{folderId}/links")
    @Operation(summary = "상대방 하나의 폴더 링크 전체 조회", description = "상대방 하나의 폴더와 모든 링크 조회")
    ResponseEntity<FoldersLinksAllResponse> getOneFolderWithLinksByUser(
            @PathVariable("userId") Long userId,
            @PathVariable("folderId") Long folderId);


    @GetMapping("/me/folders/{folderId}/links")
    @Operation(summary = "나의 하나 폴더 링크 전체 조회", description = "나의 하나 폴더와 모든 링크 조회")
    ResponseEntity<FoldersLinksAllResponse> getMyOneFolderWithLinks(
            @PathVariable("folderId") Long folderId);

    //Create - 폴더를 만드는 api
    @PostMapping("/users/{userId}/folders")
    @Operation(summary = "사용자 폴더 생성", description = "한 명의 사용자가 폴더 생성")
    ResponseEntity<FoldersResponse> createUserFolder(
            @PathVariable("userId") Long userId,
            @RequestBody FoldersCreateRequest foldersCreateRequest);


    //Delete
    @DeleteMapping("/users/{userId}/folders")
    @Operation(summary = "사용자 폴더 삭제", description = "한 명의 사용자가 폴더 삭제하게 되는데 모든 링크가 저장된 폴더는 삭제 되게 하면 안 됩니다.")
    ResponseEntity<FoldersResponse> deleteUserFolder(
            @PathVariable("userId") Long userId,
            @RequestBody FoldersDeleteRequest foldersDeleteRequest);


    @PatchMapping("/{folderId}") // 폴더 제목, 설명, 공개비공개 수정
    @Operation(summary = "폴더 제목/설명/공개비공개 수정", description = "userId와 folderId가 필요")
    ResponseEntity<FoldersResponse> updateFolderMetadata(
            @RequestBody FoldersUpdateRequest request);


    //scrap
    @PostMapping("/users/{userId}/scrap")
    @Operation(summary = "상대방 폴더 스크랩", description = "상대방 폴더를 내 폴더로 스크랩")
    ResponseEntity<FoldersLinksAllResponse> scrapFolder(
            @PathVariable("userId") Long userId,
            @RequestBody FoldersScrapRequest request);
}
