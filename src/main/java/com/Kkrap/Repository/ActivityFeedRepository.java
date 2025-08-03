package com.Kkrap.Repository;

import com.Kkrap.Entity.ActivityFeed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActivityFeedRepository extends JpaRepository<ActivityFeed, Long> {

    // 내가 팔로우한 유저들의 활동 피드 조회
    List<ActivityFeed> findByActorUserIdInAndCreatedAtAfterOrderByCreatedAtDesc(List<Long> actorIds, LocalDateTime after);

    void deleteAllByFolderId(Long folderId);
}
