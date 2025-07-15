package com.Kkrap.Service.FollowsFoldersPermission;


import com.Kkrap.Entity.Folders;
import com.Kkrap.Entity.FoldersPermissions;
import com.Kkrap.Entity.Users;
import com.Kkrap.RequestDTO.FoldersPermissionsCreateRequest;
import com.Kkrap.RequestDTO.FoldersPermissionsDeleteRequest;
import com.Kkrap.Service.FolderLink.FoldersService;
import com.Kkrap.Service.Users.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class FoldersPermissionsManagerService {

    private final UsersService usersService;

    private final FoldersPermissionsService foldersPermissionsService;

    private final FoldersService foldersService;

    public FoldersPermissionsManagerService(UsersService usersService, FoldersPermissionsService foldersPermissionsService,
                                            FoldersService foldersService){
        this.usersService = usersService;
        this.foldersPermissionsService = foldersPermissionsService;
        this.foldersService = foldersService;
    }

    public ResponseEntity<FoldersPermissionsCreateRequest> shareFolderWithUsers(Long userId, FoldersPermissionsCreateRequest request) {
        Users owner = usersService.findById(userId);
        Folders folder = foldersService.findById(request.getFolderId());
        foldersService.isOwnedByService(folder, userId);

        for (Long invitedUserId : request.getInvitedUserIds()) {
            Users invitedUser = usersService.findById(invitedUserId);

            boolean exists = foldersPermissionsService.existsByFolderAndInvitedUserId(folder, invitedUserId);
            if (exists) continue;
            foldersPermissionsService.save(owner, folder, invitedUser);
        }
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
}
