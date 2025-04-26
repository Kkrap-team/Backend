package com.Kkrap.Controller;

import com.Kkrap.RequestDTO.FoldersCreateRequest;

import com.Kkrap.RequestDTO.FoldersDeleteRequest;
import com.Kkrap.ResponseDto.FoldersResponse;
import com.Kkrap.ResponseDto.FoldersLinksAllResponse;
import com.Kkrap.Service.FoldersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RestController
@RequestMapping("/folders")
public class FoldersController {
    @Autowired
    private FoldersService foldersService;

    //Selected
    //사용자가 가지고 있는 모든 폴더와 내안 있는 링크들 같이 조회
    @GetMapping("/{userId}")
    @Operation(summary = "사용자의 모든 폴더 및 모든 링크", description = "한 명의 사용자가 가진 모든 폴더와 모든 링크 조회")
    public ResponseEntity<List<FoldersLinksAllResponse>> getFoldersAll(
            @Parameter(name = "userId", description = "사용자 ID", required = true, example = "1")
            @PathVariable("userId") Long userId){
        return foldersService.getFoldersAll(userId);
    }

    //사용자 가지고 있는 모든 폴더 안에 있는 링크 4개만 -> 썸네일 전용
    @GetMapping("/{userId}/thumbnailUrl")
    @Operation(summary = "사용자의 모든 폴더 및 링크 최신순 4개", description = "보관함에서 보여줄 썸네일 전용")
    public ResponseEntity<List<FoldersLinksAllResponse>> getFoldersAllthumbnailUrl(
            @Parameter(name = "userId", description = "사용자 ID", required = true, example = "1")
            @PathVariable("userId") Long userId){
        return foldersService.getFoldersAllthumbnailUrl(userId);
    }

    @GetMapping("/{folderId}/links")
    @Operation(summary = "하나의 폴더 링크 전체 조회", description = "하나의 폴더와 모든 링크 조회")
    public ResponseEntity<FoldersLinksAllResponse> getOneFolderLinksAll(
            @Parameter(name = "folderId", description = "폴더 ID", required = true, example = "10")
            @PathVariable("folderId") Long folderId
    ){
        return foldersService.getOneFolderLinksAll(folderId);
    }

    //Create
    // 1. 폴더를 만드는 api
    @PostMapping("/{userId}")
    @Operation(summary = "사용자 폴더 Create", description = "한 명의 사용자가 폴더 생성")
    public ResponseEntity<FoldersResponse> createFolder(
            @Parameter(name = "userId", description = "사용자 ID", required = true, example = "1")
            @PathVariable("userId") Long userId, @RequestBody FoldersCreateRequest foldersCreateRequest) {
        return foldersService.createFolder(userId, foldersCreateRequest);
    }

    //Delete
    @DeleteMapping("/{userId}")
    @Operation(summary = "사용자 폴더 Delete", description = "한 명의 사용자가 폴더 삭제하게 되는데 모든 링크가 저장된 폴더는 삭제 되게 하면 안 됩니다.")
    public ResponseEntity<FoldersResponse> deleteFolder(
            @Parameter(name = "userId", description = "사용자 ID", required = true, example = "1")
            @PathVariable("userId") Long userId, @RequestBody FoldersDeleteRequest foldersDeleteRequest)
    {
        return foldersService.deleteFolder(userId, foldersDeleteRequest);
    }

}
