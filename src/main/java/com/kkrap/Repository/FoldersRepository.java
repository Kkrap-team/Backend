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

    @Query("UPDATE Folders f SET f.viewCount = f.viewCount + 1 WHERE f.folderId = :folderId")
    void incrementViewCountById(@Param("folderId") Long folderId);

    @Query("UPDATE Folders f SET f.scrapCount = f.scrapCount + 1 WHERE f.folderId = :folderId")
    void incrementScrapCountById(@Param("folderId") Long folderId);

    List<Folders> findByUserUserIdAndVisibleTrue(Long userId);

    @Query("SELECT COALESCE(SUM(f.viewCount), 0) FROM Folders f WHERE f.user = :user")
    Long sumViewCountByUser(@Param("user") Users user);

    @Query("SELECT COALESCE(SUM(f.scrapCount), 0) FROM Folders f WHERE f.user = :user")
    Long sumScrapCountByUser(@Param("user") Users user);



    List<Folders> findByVisibleTrue();

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


    //스크랩순, 조회수순
    @Query(value = """
    SELECT f.*
    FROM folders f
    WHERE f.visible = true
      AND f.create_time >= :since
    ORDER BY f.view_count DESC, f.folder_id DESC
    LIMIT :limit
    """, nativeQuery = true)
    List<Folders> findTopByViewCountSince(
            @Param("since") java.time.LocalDateTime since,
            @Param("limit") int limit
    );

    @Query(value = """
    SELECT f.*
    FROM folders f
    WHERE f.visible = true
      AND f.create_time >= :since
    ORDER BY f.scrap_count DESC, f.folder_id DESC
    LIMIT :limit
    """, nativeQuery = true)
    List<Folders> findTopByScrapCountSince(
            @Param("since") java.time.LocalDateTime since,
            @Param("limit") int limit
    );

    @Query(value = """
    SELECT *
    FROM folders
    WHERE visible = true
      AND LOWER(folder_name) LIKE LOWER(CONCAT('%', :keyword, '%'))
    ORDER BY create_time DESC, folder_id DESC
    LIMIT 10
    """, nativeQuery = true)
    List<Folders> searchTop10VisibleByFolderName(@Param("keyword") String keyword);

    @Query(value = """
    SELECT *
    FROM folders
    WHERE visible = true
      AND LOWER(folder_name) LIKE LOWER(CONCAT('%', :keyword, '%'))
    ORDER BY create_time DESC, folder_id DESC
    """, nativeQuery = true)
    List<Folders> searchAllVisibleByFolderNameLike(@Param("keyword") String keyword);




}
