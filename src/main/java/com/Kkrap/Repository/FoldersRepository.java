package com.Kkrap.Repository;

import com.Kkrap.Entity.Folders;
import com.Kkrap.Entity.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoldersRepository extends JpaRepository<Folders, Long> {

    List<Folders> findByUserUserId(Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE Folders f SET f.viewCount = f.viewCount + 1 WHERE f.folderId = :folderId")
    void incrementViewCountById(@Param("folderId") Long folderId);

    List<Folders> findByUserUserIdAndVisibleTrue(Long userId);

    @Query("SELECT COALESCE(SUM(f.viewCount), 0) FROM Folders f WHERE f.user = :user")
    Long sumViewCountByUser(@Param("user") Users user);

    @Modifying
    @Query("UPDATE Folders f SET f.scrapCount = f.scrapCount + :increment WHERE f.folderId = :folderId")
    void incrementScrapCount(@Param("folderId") Long folderId, @Param("increment") Long increment);
}
