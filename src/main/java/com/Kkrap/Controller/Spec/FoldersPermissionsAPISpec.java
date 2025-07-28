package com.Kkrap.Controller.Spec;

import com.Kkrap.RequestDTO.FoldersPermissionsCreateRequest;
import com.Kkrap.RequestDTO.FoldersPermissionsDeleteRequest;
import com.Kkrap.ResponseDTO.FollowInviteCandidateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{userId}/folders/{folderId}/candidates")
    @Operation(summary = "폴더 공유 대상 후보(내 팔로잉) 조회", description = "내가 팔로우 중인 유저 중 이 폴더에 이미 초대된 사람은 invited=true 로 반환")
    ResponseEntity<List<FollowInviteCandidateResponse>> getFollowingsWithInviteFlag(
            @Parameter(description = "현재 로그인한 사용자 ID") @PathVariable("userId") Long userId,
            @Parameter(description = "조회할 폴더 ID") @PathVariable("folderId") Long folderId
    );
}
