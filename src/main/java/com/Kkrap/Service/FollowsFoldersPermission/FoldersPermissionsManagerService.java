package com.Kkrap.Service.FollowsFoldersPermission;


import com.Kkrap.Entity.Folders;
import com.Kkrap.Entity.FoldersPermissions;
import com.Kkrap.Entity.Follows;
import com.Kkrap.Entity.Users;
import com.Kkrap.RequestDTO.FoldersPermissionsCreateRequest;
import com.Kkrap.RequestDTO.FoldersPermissionsDeleteRequest;
import com.Kkrap.ResponseDTO.FollowInviteCandidateResponse;
import com.Kkrap.Service.FolderLink.FoldersService;
import com.Kkrap.Service.Users.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

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
        return ResponseEntity.ok(request);

    }

    // 내가 팔로우 중인 사람들 + 이 폴더에 이미 초대됐는지 여부
    @Transactional(readOnly = true)
    public ResponseEntity<List<FollowInviteCandidateResponse>> getMyFollowingsWithInviteFlag(Long userId, Long folderId) {
        Users owner = usersService.findById(userId);
        Folders folder = foldersService.findById(folderId);
        foldersService.isOwnedByService(folder, userId); // 소유자 체크

        // 내가 팔로우 중인 유저들
        List<Follows> followings = followsService.findByFollower(owner);

        // 이미 이 폴더에 초대된 userId 집합
        Set<Long> invitedIds = foldersPermissionsService.findInvitedUserIdsByFolderId(folderId);

        List<FollowInviteCandidateResponse> result = followings.stream()
                .map(f -> FollowInviteCandidateResponse.of(f, invitedIds.contains(f.getFollowingId())))
                .toList();

        return ResponseEntity.ok(result);
    }

}
