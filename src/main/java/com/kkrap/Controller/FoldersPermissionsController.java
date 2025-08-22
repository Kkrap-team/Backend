package com.kkrap.Controller;

import com.kkrap.Controller.Spec.FoldersPermissionsAPISpec;
import com.kkrap.RequestDTO.FoldersPermissionsCreateRequest;
import com.kkrap.RequestDTO.FoldersPermissionsDeleteRequest;
import com.kkrap.ResponseDTO.FollowInviteListResponse;
import com.kkrap.Service.FollowsFoldersPermission.FoldersPermissionsManagerService;
import io.micrometer.core.annotation.Timed;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@Timed(value = "http.controller", extraTags = {"controller","FoldersPermissions"})
@RequestMapping("/folderspermissions")
public class FoldersPermissionsController implements FoldersPermissionsAPISpec {

    private final FoldersPermissionsManagerService foldersPermissionsManagerService;

    public FoldersPermissionsController(FoldersPermissionsManagerService foldersPermissionsManagerService){
        this.foldersPermissionsManagerService = foldersPermissionsManagerService;
    }

    @Override
    public ResponseEntity<FoldersPermissionsCreateRequest> shareFolderWithUsers(
            Authentication authentication,
            FoldersPermissionsCreateRequest request) {
        Long userId = Long.parseLong(authentication.getName());
        return foldersPermissionsManagerService.shareFolderWithUsers(userId, request);
    }

    @Override
    public ResponseEntity<FoldersPermissionsDeleteRequest> revokeFolderPermission(
            Authentication authentication,
            FoldersPermissionsDeleteRequest request) {
        Long userId = Long.parseLong(authentication.getName());
        return foldersPermissionsManagerService.revokeFolderPermission(userId, request);
    }

    @Override
    public ResponseEntity<FollowInviteListResponse> getFollowingsWithInviteFlag(
            Authentication authentication,
            Long folderId
    ) {
        Long userId = Long.parseLong(authentication.getName());
        return foldersPermissionsManagerService.getMyFollowingsWithInviteFlag(userId, folderId);
    }


}
