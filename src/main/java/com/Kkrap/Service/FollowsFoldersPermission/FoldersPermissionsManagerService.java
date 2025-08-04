package com.Kkrap.Service.FollowsFoldersPermission;


import com.Kkrap.Entity.Folders;
import com.Kkrap.Entity.FoldersPermissions;
import com.Kkrap.Entity.Follows;
import com.Kkrap.Entity.Users;
import com.Kkrap.RequestDTO.FoldersPermissionsCreateRequest;
import com.Kkrap.RequestDTO.FoldersPermissionsDeleteRequest;
import com.Kkrap.ResponseDTO.FollowInviteCandidateResponse;
import com.Kkrap.ResponseDTO.FollowInviteListResponse;
import com.Kkrap.ResponseDTO.UsersProfileResponse;
import com.Kkrap.Service.FolderLink.FoldersService;
import com.Kkrap.Service.Users.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FoldersPermissionsManagerService {

    private final UsersService usersService;

    private final FoldersPermissionsService foldersPermissionsService;

    private final FoldersService foldersService;

    private final FollowsService followsService;

    public FoldersPermissionsManagerService(UsersService usersService, FoldersPermissionsService foldersPermissionsService,
                                            FoldersService foldersService, FollowsService followsService){
        this.usersService = usersService;
        this.foldersPermissionsService = foldersPermissionsService;
        this.foldersService = foldersService;
        this.followsService = followsService;
    }

    public ResponseEntity<FoldersPermissionsCreateRequest> shareFolderWithUsers(Long userId, FoldersPermissionsCreateRequest request) {
        Users owner = usersService.findById(userId);
        Folders folder = foldersService.findById(request.getFolderId());
        foldersService.isOwnedByService(folder, userId);

        for (Long invitedUserId : request.getInvitedUserIds()) {
            Users invitedUser = usersService.findById(invitedUserId);

            boolean exists = foldersPermissionsService.existsByFolderAndInvitedUserId(folder, invitedUserId);
            if (exists) {
                continue;
            }
            foldersPermissionsService.save(owner, folder, invitedUser);
        }
        folder.setShared(true);
        foldersService.save(folder);
        return ResponseEntity.ok(request);
    }

    public ResponseEntity<FoldersPermissionsDeleteRequest> revokeFolderPermission(Long ownerId, FoldersPermissionsDeleteRequest request) {
        usersService.findById(ownerId);
        Folders folder = foldersService.findById(request.getFolderId());
        foldersService.isOwnedByService(folder, ownerId);

        // 삭제할 권한 찾기
        FoldersPermissions permission = foldersPermissionsService.findByFolderAndInvitedUserId(folder, request.getInvitedUserId());
        foldersPermissionsService.delete(permission);

        boolean stillHasInvited = foldersPermissionsService.existsByFolder(folder);
        if (!stillHasInvited) {
            folder.setShared(false);
            foldersService.save(folder);
        }
        return ResponseEntity.ok(request);
    }

    // 내가 팔로우 중인 사람들 + 이 폴더에 이미 초대됐는지 여부
    @Transactional(readOnly = true)
    public ResponseEntity<FollowInviteListResponse> getMyFollowingsWithInviteFlag(Long userId, Long folderId) {
        Users me = usersService.findById(userId);
        Folders folder = foldersService.findById(folderId);

        Users owner = folder.getUser();

        if (!folder.isOwnedBy(userId)) {
            foldersPermissionsService.existsByFolderFolderIdAndInvitedUserId(folderId, userId);
        }

        // 내가 팔로우한 사람 중 owner 제외
        List<Follows> followings = followsService.findByFollower(me).stream()
                .filter(f -> !f.getFollowingId().equals(owner.getUserId())) // root 제외
                .toList();

        // 초대된 사람 목록
        Set<Long> invitedIds = foldersPermissionsService.findInvitedUserIdsByFolderId(folderId);

//        List<FollowInviteCandidateResponse> candidates = followings.stream()
//                .map(f -> FollowInviteCandidateResponse.of(f, invitedIds.contains(f.getFollowingId())))
//                .toList();

        // 내가 팔로우한 사람 중 owner 제외 후 → 초대 여부 포함 응답 생성
        List<FollowInviteCandidateResponse> candidates = followsService.findByFollower(me).stream()
                .filter(f -> !f.getFollowingId().equals(owner.getUserId()))
                .map(f -> FollowInviteCandidateResponse.of(f, invitedIds.contains(f.getFollowingId())))
                .toList();

        // partitioningBy로 분리
        Map<Boolean, List<FollowInviteCandidateResponse>> partitioned =
                candidates.stream().collect(Collectors.partitioningBy(FollowInviteCandidateResponse::isInvited));

        List<FollowInviteCandidateResponse> invited = partitioned.get(true);
        List<FollowInviteCandidateResponse> notInvited = partitioned.get(false);



        FollowInviteListResponse response = FollowInviteListResponse.of(
                UsersProfileResponse.from(owner),
                candidates,
                invited,
                notInvited
        );
        return ResponseEntity.ok(response);
    }

}
