package com.Kkrap.Repository;

import com.Kkrap.Entity.FolderList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderListRepository extends JpaRepository<FolderList, Long> {
    // 특정 folderId와 linkId에 해당하는 FolderList 조회
    List<FolderList> findByFolderFolderIdAndLinkLinkId(Long folderId, Long linkId);

    // 특정 folderId에 포함된 모든 링크 조회
    List<FolderList> findByFolderFolderId(Long folderId);

//    //folderId를 가지고 url링크 조회
//    List<FolderList> findByFolderId(Long folderId);
}
