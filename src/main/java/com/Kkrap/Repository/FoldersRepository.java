package com.Kkrap.Repository;

import com.Kkrap.Entity.Folders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoldersRepository extends JpaRepository<Folders, Long> {
    List<Folders> findByUserId(Long userid);
    // userId로 folderId만 조회하는 메서드
//    List<FolderIdOnly> findByUserId(Long userId);

    //folderId와 userId를 같이 찾는 메서드
    Optional<Folders> findByFolderIdAndUserId(Long folderId, Long userId);


}
