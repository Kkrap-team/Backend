package com.kkrap.Repository;

import com.kkrap.Entity.Folders;
import com.kkrap.Entity.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    @Query("SELECT COALESCE(SUM(f.scrapCount), 0) FROM Folders f WHERE f.user = :user")
    Long sumScrapCountByUser(@Param("user") Users user);


    @Modifying
    @Query("UPDATE Folders f SET f.scrapCount = f.scrapCount + :increment WHERE f.folderId = :folderId")
    void incrementScrapCount(@Param("folderId") Long folderId, @Param("increment") Long increment);

    List<Folders> findByVisibleTrue();

    // com.kkrap.Repository.FoldersRepository
    @Query(value = """
    SELECT f.*
    FROM folders f
    WHERE f.visible = true
      AND (
            :cursorTime IS NULL
            OR f.create_time < :cursorTime
            OR (f.create_time = :cursorTime AND f.folder_id < :cursorId)
          )
    ORDER BY f.create_time DESC, f.folder_id DESC
    LIMIT :limitPlusOne
    """, nativeQuery = true)
    List<Folders> fetchVisibleFoldersPageGlobal(
            @Param("cursorTime") java.time.LocalDateTime cursorTime,
            @Param("cursorId") Long cursorId,
            @Param("limitPlusOne") int limitPlusOne
    );


}
