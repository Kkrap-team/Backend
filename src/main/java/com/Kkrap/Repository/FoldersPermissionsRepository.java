package com.Kkrap.Repository;


import com.Kkrap.Entity.Folders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.Kkrap.Entity.FoldersPermissions;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoldersPermissionsRepository extends JpaRepository<FoldersPermissions, Long> {

    // 중복 체크용: 폴더 + 초대된 사용자 ID 조합이 이미 존재하는지
    boolean existsByFolderAndInvitedUserId(Folders folder, Long invitedUserId);

    // 특정 사용자가 공유받은 폴더 목록 조회
    List<FoldersPermissions> findByInvitedUserId(Long invitedUserId);

    // 특정 폴더가 누구와 공유되었는지 조회
    List<FoldersPermissions> findByFolder(Folders folder);

    Optional<FoldersPermissions> findByFolderAndInvitedUserId(Folders folder, Long invitedUserId);
}