package com.kkrap.Service.FollowsFoldersPermission;

import com.kkrap.Entity.Folders;
import com.kkrap.Entity.FoldersPermissions;
import com.kkrap.Entity.Users;
import com.kkrap.Exception.FoldersPermissionNotFoundException;
import com.kkrap.Exception.FoldersVisibleException;
import com.kkrap.Repository.FoldersPermissionsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class FoldersPermissionsService {

    private final FoldersPermissionsRepository foldersPermissionsRepository;

    public FoldersPermissionsService(FoldersPermissionsRepository foldersPermissionsRepository){
        this.foldersPermissionsRepository = foldersPermissionsRepository;
    }

    public boolean existsByFolderAndInvitedUserId(Folders folder, Long invitedUserId){
        return foldersPermissionsRepository.existsByFolderAndInvitedUserId(folder, invitedUserId);
    }

    public void ensureReadable(Folders folder, Long userId) {
        // 공개 폴더면 OK
        if (folder.isVisible()) return;
        // 소유자면 OK
        if (folder.isOwnedBy(userId)) return;
        // 초대받은 사용자면 OK
        boolean invited = foldersPermissionsRepository.existsByFolderAndInvitedUserId(folder, userId);
        if (invited) return;
        throw FoldersVisibleException.of("접근 권한이 없습니다.");
    }

    public FoldersPermissions save(Users owner, Folders folders, Users invitedUser){
        return foldersPermissionsRepository.save(FoldersPermissions.of(owner, folders, invitedUser));
    }

    public void delete(FoldersPermissions permission){
        foldersPermissionsRepository.delete(permission);
    }

    public FoldersPermissions findByFolderAndInvitedUserId(Folders folders, Long invitedUserId){
        FoldersPermissions permission = foldersPermissionsRepository
                .findByFolderAndInvitedUserId(folders, invitedUserId)
                .orElseThrow(() -> FoldersPermissionNotFoundException.from("공유 권한이 존재하지 않습니다."));
        return permission;
    }

    public List<FoldersPermissions> findByInvitedUserId(Long invitedUserId) {
        return foldersPermissionsRepository.findByInvitedUserId(invitedUserId);
    }

    public Set<Long> findInvitedUserIdsByFolderId(Long folderId){
        return foldersPermissionsRepository.findInvitedUserIdsByFolderId(folderId);
    }

    public boolean existsByFolderFolderIdAndInvitedUserId(Long folderId, Long userId){
        boolean exists = foldersPermissionsRepository.existsByFolderFolderIdAndInvitedUserId(folderId, userId);
        if (!exists) {
            return false;
        }
        return true;
    }

    public boolean existsByFolder(Folders folder) {
        return foldersPermissionsRepository.existsByFolder(folder);
    }
}
