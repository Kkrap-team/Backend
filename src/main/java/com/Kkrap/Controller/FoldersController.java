package com.Kkrap.Controller;

import com.Kkrap.RequestDTO.FoldersCreateRequest;

import com.Kkrap.RequestDTO.FoldersDeleteRequest;
import com.Kkrap.RequestDTO.FoldersUpdateRequest;
import com.Kkrap.ResponseDTO.FoldersResponse;
import com.Kkrap.ResponseDTO.FoldersLinksAllResponse;
import com.Kkrap.ResponseDTO.UserFoldersWithSharedResponse;
import com.Kkrap.Service.FolderLink.FoldersManagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RestController
@RequestMapping("/folders")
public class FoldersController {

    private final FoldersManagerService foldersManagerService;

    public FoldersController(FoldersManagerService foldersManagerService){
        this.foldersManagerService = foldersManagerService;
    }

    //사용자가 가지고 있는 모든 폴더와 내안 있는 링크들 같이 조회
    @GetMapping("/users/{userId}/folders")
    @Operation(summary = "사용자의 모든 폴더 및 모든 링크", description = "한 명의 사용자가 가진 모든 폴더와 모든 링크 조회")
    public ResponseEntity<UserFoldersWithSharedResponse> getUserAllFoldersWithLinks(
            @Parameter(name = "userId", description = "사용자 ID", required = true, example = "1")
            @PathVariable("userId") Long userId){
        return foldersManagerService.getUserAllFoldersWithLinks(userId);
    }



    //상대방이 가지고 있는 모든 폴더 안에 있는 링크 4개만 -> 썸네일 전용
    @GetMapping("/users/{userId}/folders/thumbnails")
    @Operation(summary = "상대방의 모든 폴더 및 링크 최신순 4개", description = "상대방 보관함에서 보여줄 썸네일 전용")
    public ResponseEntity<UserFoldersWithSharedResponse> getAllFoldersWithTop4LinksByUser(
            @Parameter(name = "userId", description = "사용자 ID", required = true, example = "1")
            @PathVariable("userId") Long userId){
        return foldersManagerService.getAllFoldersWithTop4LinksByUser(userId);
    }

    //내가 가지고 있는 모든 폴더 안에 있는 링크 4개만 -> 썸네일 전용
    @GetMapping("/users/{userId}/folders/thumbnails/me")
    @Operation(summary = "내꺼 모든 폴더 및 링크 최신순 4개", description = "내꺼 보관함에서 보여줄 썸네일 전용")
    public ResponseEntity<UserFoldersWithSharedResponse> getMeAllFoldersWithTop4Links(
            @Parameter(name = "userId", description = "사용자 ID", required = true, example = "1")
            @PathVariable("userId") Long userId){
        return foldersManagerService.getMeAllFoldersWithTop4Links(userId);
    }

    //조회수 -> Redis -> Kafka
    @GetMapping("/users/{userId}/folders/{folderId}/links")
    @Operation(summary = "상대방 하나의 폴더 링크 전체 조회", description = "상대방 하나의 폴더와 모든 링크 조회")
    public ResponseEntity<FoldersLinksAllResponse> getOneFolderWithLinksByUser(
            @PathVariable("userId") Long userId,
            @PathVariable("folderId") Long folderId
    ){
        return foldersManagerService.getOneFolderWithLinksByUser(userId, folderId);
    }

    @GetMapping("/me/folders/{folderId}/links")
    @Operation(summary = "나의 하나 폴더 링크 전체 조회", description = "나의 하나 폴더와 모든 링크 조회")
    public ResponseEntity<FoldersLinksAllResponse> getMyOneFolderWithLinks(
            @Parameter(name = "folderId", description = "폴더 ID", required = true, example = "10")
            @PathVariable("folderId") Long folderId
    ){
        return foldersManagerService.getMyOneFolderWithLinks(folderId);
    }


    //Create
    // 1. 폴더를 만드는 api
    @PostMapping("/users/{userId}/folders")
    @Operation(summary = "사용자 폴더 Create", description = "한 명의 사용자가 폴더 생성")
    public ResponseEntity<FoldersResponse> createUserFolder(
            @Parameter(name = "userId", description = "사용자 ID", required = true, example = "1")
            @PathVariable("userId") Long userId, @RequestBody FoldersCreateRequest foldersCreateRequest) {
        return foldersManagerService.createUserFolder(userId, foldersCreateRequest);
    }

    //Delete
    @DeleteMapping("/users/{userId}/folders")
    @Operation(summary = "사용자 폴더 Delete", description = "한 명의 사용자가 폴더 삭제하게 되는데 모든 링크가 저장된 폴더는 삭제 되게 하면 안 됩니다.")
    public ResponseEntity<FoldersResponse> deleteUserFolder(
            @Parameter(name = "userId", description = "사용자 ID", required = true, example = "1")
            @PathVariable("userId") Long userId, @RequestBody FoldersDeleteRequest foldersDeleteRequest)
    {
        return foldersManagerService.deleteUserFolder(userId, foldersDeleteRequest);
    }

    @PatchMapping("/{folderId}") // 폴더 제목, 설명, 공개비공개 수정
    @Operation(summary = "폴더 제목, 설명, 공개비공개 수정", description = "userId와 folderId가 필요")
    public ResponseEntity<FoldersResponse> updateFolderMetadata(@RequestBody FoldersUpdateRequest request){
        return foldersManagerService.updateFolderMetadata(request);
    }
}
