package com.kkrap.Service.ActivityFeed;

import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import com.kkrap.Entity.ActivityFeed;
import com.kkrap.Repository.ActivityFeedRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityFeedService {

    private final ActivityFeedRepository activityFeedRepository;

    public void deleteAllByFolderId(Long folderId) {
        activityFeedRepository.deleteAllByFolderId(folderId);
    }

    public void saveAll(List<ActivityFeed> feedList){
        activityFeedRepository.saveAll(feedList);
    }

    public void save(ActivityFeed feed){
        activityFeedRepository.save(feed);
    }


    public List<ActivityFeed> findByActorUserIdInAndCreatedAtAfterOrderByCreatedAtDesc(List<Long> followingIds, LocalDateTime oneWeekAgo, Pageable pageable){
        return activityFeedRepository.findByActorUserIdInAndCreatedAtAfterOrderByCreatedAtDesc(followingIds, oneWeekAgo, pageable);
    }
}
