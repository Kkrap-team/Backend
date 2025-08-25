package com.kkrap.Service.FollowsFoldersPermission;


import com.kkrap.Entity.Folders;
import com.kkrap.Entity.FoldersPermissions;
import com.kkrap.Entity.Follows;
import com.kkrap.Entity.Users;
import com.kkrap.RequestDTO.FoldersPermissionsCreateRequest;
import com.kkrap.RequestDTO.FoldersPermissionsDeleteRequest;
import com.kkrap.ResponseDTO.FollowInviteCandidateResponse;
import com.kkrap.ResponseDTO.FollowInviteListResponse;
import com.kkrap.ResponseDTO.UsersProfileResponse;
import com.kkrap.Service.FolderLink.FoldersService;
import com.kkrap.Service.Users.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        usersService.findById(userId);
        Folders folder = foldersService.findById(folderId);

        Users owner = folder.getUser();
        boolean isOwner = folder.isOwnedBy(userId);

        // 권한 체크: 소유자가 아니면 초대받은 사용자여야 함 (exists 결과를 반드시 사용!)
//        if (!isOwner) {
//            foldersPermissionsService.existsByFolderFolderIdAndInvitedUserId(folderId, userId);
//        }
        boolean hasPermission = true;
        if (!isOwner) {
            hasPermission = foldersPermissionsService
                    .existsByFolderFolderIdAndInvitedUserId(folderId, userId);
        }

        // 후보 기준을 'owner가 팔로우한 사람'으로 변경
        List<Follows> ownerFollowings = followsService.findByFollower(owner).stream()
                .filter(f -> !f.getFollowingId().equals(owner.getUserId())) // owner 자신 제외 (혹시 몰라서)
                .toList();

        // 이미 초대한 사용자 id 집합
        Set<Long> invitedIds = foldersPermissionsService.findInvitedUserIdsByFolderId(folderId);

        // 후보 리스트 생성 (owner 팔로잉 기준)
        List<FollowInviteCandidateResponse> candidates = ownerFollowings.stream()
                .map(f -> FollowInviteCandidateResponse.of(
                        f, invitedIds.contains(f.getFollowingId())))
                .toList();

        // 초대/미초대 분리
        Map<Boolean, List<FollowInviteCandidateResponse>> partitioned =
                candidates.stream().collect(Collectors.partitioningBy(FollowInviteCandidateResponse::isInvited));

        List<FollowInviteCandidateResponse> invited = partitioned.getOrDefault(true, List.of());
        List<FollowInviteCandidateResponse> notInvited = partitioned.getOrDefault(false, List.of());

//        FollowInviteListResponse response = FollowInviteListResponse.of(
//                UsersProfileResponse.from(owner),
//                candidates,
//                invited,
//                notInvited
//        );
        FollowInviteListResponse response = FollowInviteListResponse.of(
                UsersProfileResponse.from(owner),
                candidates,
                invited,
                notInvited,
                hasPermission
        );
        return ResponseEntity.ok(response);
    }

}
