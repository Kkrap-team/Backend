package com.Kkrap.Controller;

import com.Kkrap.Controller.Spec.FoldersPermissionsAPISpec;
import com.Kkrap.RequestDTO.FoldersPermissionsCreateRequest;
import com.Kkrap.RequestDTO.FoldersPermissionsDeleteRequest;
import com.Kkrap.ResponseDTO.FollowInviteCandidateResponse;
import com.Kkrap.Service.FollowsFoldersPermission.FoldersPermissionsManagerService;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
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
            Long userId,
            FoldersPermissionsCreateRequest request) {
        return foldersPermissionsManagerService.shareFolderWithUsers(userId, request);
    }

    @Override
    public ResponseEntity<FoldersPermissionsDeleteRequest> revokeFolderPermission(
            Long userId,
            FoldersPermissionsDeleteRequest request) {
        return foldersPermissionsManagerService.revokeFolderPermission(userId, request);
    }

    @Override
    public ResponseEntity<List<FollowInviteCandidateResponse>> getFollowingsWithInviteFlag(
            Long userId,
            Long folderId
    ) {
        return foldersPermissionsManagerService.getMyFollowingsWithInviteFlag(userId, folderId);
    }


}
