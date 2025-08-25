package com.kkrap.Repository;

import com.kkrap.Entity.Folders;
import com.kkrap.Entity.FoldersLinks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoldersLinksRepository extends JpaRepository<FoldersLinks, Long> {

    List<FoldersLinks> findByFolders(Folders folders);

    Optional<FoldersLinks> findFirstByFoldersOrderByLinksCreateTimeDesc(Folders folders);

    Optional<FoldersLinks> findByUserIdAndFoldersFolderIdAndLinksLinkId(
            Long userId, Long folderId, Long linkId
    );

    boolean existsByUserIdAndFoldersFolderIdAndLinksLinkId(
            Long userId, Long folderId, Long linkId
    );

    void deleteByUserIdAndFoldersFolderIdAndLinksLinkId(
            Long userId, Long folderId, Long linkId
    );

}
