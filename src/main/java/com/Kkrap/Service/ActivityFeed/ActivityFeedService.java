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
    private final UsersRepository usersRepository;
    private final FoldersRepository foldersRepository;

//    public List<FeedFolderResponse> getFeedForUser(Users me) {
//        // 1. 내가 팔로우한 사람들의 ID 목록
//        List<Long> followingIds = me.getFollowings().stream()
//                .map(f -> f.getFollowingId())
//                .collect(Collectors.toList());
//
//        if (followingIds.isEmpty()) return List.of();
//
//        // 2. 해당 유저들의 활동 피드 가져오기
//        List<ActivityFeed> feeds = activityFeedRepository.findByActorUserIdInOrderByCreatedAtDesc(followingIds);
//
//        // 3. JOIN 정보로 응답 DTO 구성
//        return feeds.stream().map(feed -> {
//            Users actor = usersRepository.findById(feed.getActorUserId()).orElseThrow();
//            Folders folder = foldersRepository.findById(feed.getFolderId()).orElseThrow();
//
//            return FeedFolderResponse.builder()
//                    .folderId(folder.getFolderId())
//                    .folderName(folder.getFolderName())
//                    .createdAt(feed.getCreatedAt())
//                    .actor(FeedFolderResponse.ActorInfo.builder()
//                            .userId(actor.getUserId())
//                            .nickname(actor.getNickname())
//                            .profileUrl(actor.getProfile())
//                            .build())
//                    .build();
//        }).toList();
//    }

    public void saveAll(List<ActivityFeed> feedList){
        activityFeedRepository.saveAll(feedList);
    }

    public List<ActivityFeed> findByActorUserIdInAndCreatedAtAfterOrderByCreatedAtDesc(List<Long> followingIds, LocalDateTime oneWeekAgo){
        return activityFeedRepository.findByActorUserIdInAndCreatedAtAfterOrderByCreatedAtDesc(followingIds, oneWeekAgo);
    }
}
