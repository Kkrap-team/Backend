package com.kkrap.Repository;

import com.kkrap.Entity.ActivityFeed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActivityFeedRepository extends JpaRepository<ActivityFeed, Long> {

    // 내가 팔로우한 유저들의 활동 피드 조회
    List<ActivityFeed> findByActorUserIdInAndCreatedAtAfterOrderByCreatedAtDesc(
            List<Long> actorUserIds,
            LocalDateTime createdAt,
            Pageable pageable
    );
    void deleteAllByFolderId(Long folderId);
}
