package com.Kkrap.Controller.Spec;

import com.Kkrap.RequestDTO.FoldersPermissionsCreateRequest;
import com.Kkrap.RequestDTO.FoldersPermissionsDeleteRequest;
import com.Kkrap.ResponseDTO.FollowInviteCandidateResponse;
import com.Kkrap.ResponseDTO.FollowInviteListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "FoldersPermissions", description = "폴더 공유 권한 관리 API Endpoint")
public interface FoldersPermissionsAPISpec {
    @PostMapping("/share")
    @Operation(summary = "폴더 공유", description = "특정 폴더를 여러 사용자에게 공유합니다.")
    ResponseEntity<FoldersPermissionsCreateRequest> shareFolderWithUsers(
            Authentication authentication,
            @RequestBody FoldersPermissionsCreateRequest request);

    @DeleteMapping("/share")
    @Operation(summary = "폴더 공유 권한 삭제", description = "특정 사용자에 대한 폴더 공유를 취소합니다.")
    ResponseEntity<FoldersPermissionsDeleteRequest> revokeFolderPermission(
            Authentication authentication,
            @RequestBody FoldersPermissionsDeleteRequest request);

    @GetMapping("/folders/{folderId}/candidates")
    @Operation(summary = "폴더 공유 대상 후보(내 팔로잉) 조회", description = "내가 팔로우 중인 유저 중 이 폴더에 이미 초대된 사람은 invited=true 로 반환")
    ResponseEntity<FollowInviteListResponse> getFollowingsWithInviteFlag(
            Authentication authentication,
            @Parameter(description = "조회할 폴더 ID") @PathVariable("folderId") Long folderId
    );
}
