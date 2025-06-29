package com.Kkrap.Service.FollowsFoldersPermission;

import com.Kkrap.Entity.Folders;
import com.Kkrap.Entity.FoldersPermissions;
import com.Kkrap.Entity.Users;
import com.Kkrap.Exception.FoldersPermissionNotFoundException;
import com.Kkrap.Repository.FoldersPermissionsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoldersPermissionsService {

    private final FoldersPermissionsRepository foldersPermissionsRepository;

    public FoldersPermissionsService(FoldersPermissionsRepository foldersPermissionsRepository){
        this.foldersPermissionsRepository = foldersPermissionsRepository;
    }

    public boolean existsByFolderAndInvitedUserId(Folders folder, Long invitedUserId){
        return foldersPermissionsRepository.existsByFolderAndInvitedUserId(folder, invitedUserId);
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

}
