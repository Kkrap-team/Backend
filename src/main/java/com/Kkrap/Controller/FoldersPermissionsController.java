package com.Kkrap.Controller;

import com.Kkrap.RequestDTO.FoldersPermissionsCreateRequest;
import com.Kkrap.RequestDTO.FoldersPermissionsDeleteRequest;
import com.Kkrap.Service.FollowsFoldersPermission.FoldersPermissionsManagerService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/folderspermissions")
public class FoldersPermissionsController {

    private final FoldersPermissionsManagerService foldersPermissionsManagerService;

    public FoldersPermissionsController(FoldersPermissionsManagerService foldersPermissionsManagerService){
        this.foldersPermissionsManagerService = foldersPermissionsManagerService;
    }

    @PostMapping("/{userId}/share")
    @Operation(summary = "폴더 공유", description = "특정 폴더를 여러 사용자에게 공유합니다.")
    public ResponseEntity<FoldersPermissionsCreateRequest> shareFolderWithUsers(
            @PathVariable("userId") Long userId,
            @RequestBody FoldersPermissionsCreateRequest request) {
        return foldersPermissionsManagerService.shareFolderWithUsers(userId, request);
    }

    @DeleteMapping("/{userId}/share")
    @Operation(summary = "폴더 공유 권한 삭제", description = "특정 사용자에 대한 폴더 공유를 취소합니다.")
    public ResponseEntity<FoldersPermissionsDeleteRequest> revokeFolderPermission(
            @PathVariable("userId") Long userId,
            @RequestBody FoldersPermissionsDeleteRequest request) {
        return foldersPermissionsManagerService.revokeFolderPermission(userId, request);
    }




}
