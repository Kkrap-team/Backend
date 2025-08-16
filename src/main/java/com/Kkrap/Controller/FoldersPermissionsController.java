package com.Kkrap.Controller;

import com.Kkrap.Controller.Spec.FoldersPermissionsAPISpec;
import com.Kkrap.RequestDTO.FoldersPermissionsCreateRequest;
import com.Kkrap.RequestDTO.FoldersPermissionsDeleteRequest;
import com.Kkrap.ResponseDTO.FollowInviteCandidateResponse;
import com.Kkrap.ResponseDTO.FollowInviteListResponse;
import com.Kkrap.Service.FollowsFoldersPermission.FoldersPermissionsManagerService;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
