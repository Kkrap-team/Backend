package com.Kkrap.Service.ActivityFeed;

import com.Kkrap.Entity.Follows;
import org.springframework.stereotype.Service;


import com.Kkrap.Entity.ActivityFeed;
import com.Kkrap.Entity.Folders;
import com.Kkrap.Entity.Users;
import com.Kkrap.Repository.ActivityFeedRepository;
import com.Kkrap.Repository.FoldersRepository;
import com.Kkrap.Repository.UsersRepository;
import com.Kkrap.ResponseDTO.FeedFolderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<ActivityFeed> findByActorUserIdInAndCreatedAtAfterOrderByCreatedAtDesc(List<Long> followingIds, LocalDateTime oneWeekAgo){
        return activityFeedRepository.findByActorUserIdInAndCreatedAtAfterOrderByCreatedAtDesc(followingIds, oneWeekAgo);
    }
}
