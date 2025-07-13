package com.Kkrap.Controller.Spec;

import com.Kkrap.RequestDTO.FoldersPermissionsCreateRequest;
import com.Kkrap.RequestDTO.FoldersPermissionsDeleteRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "FoldersPermissions", description = "폴더 공유 권한 관리 API Endpoint")
public interface FoldersPermissionsAPISpec {
    @PostMapping("/{userId}/share")
    @Operation(summary = "폴더 공유", description = "특정 폴더를 여러 사용자에게 공유합니다.")
    ResponseEntity<FoldersPermissionsCreateRequest> shareFolderWithUsers(
            @PathVariable("userId") Long userId,
            @RequestBody FoldersPermissionsCreateRequest request);

    @DeleteMapping("/{userId}/share")
    @Operation(summary = "폴더 공유 권한 삭제", description = "특정 사용자에 대한 폴더 공유를 취소합니다.")
    ResponseEntity<FoldersPermissionsDeleteRequest> revokeFolderPermission(
            @PathVariable("userId") Long userId,
            @RequestBody FoldersPermissionsDeleteRequest request);
}
