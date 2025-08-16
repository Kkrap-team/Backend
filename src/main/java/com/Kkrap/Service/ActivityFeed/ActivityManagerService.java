package com.Kkrap.Service.ActivityFeed;

import com.Kkrap.Entity.*;
import com.Kkrap.ResponseDTO.FeedFolderResponse;
import com.Kkrap.Service.FolderLink.FoldersLinksService;
import com.Kkrap.Service.FolderLink.FoldersService;
import com.Kkrap.Service.FolderLink.LinksService;
import com.Kkrap.Service.FollowsFoldersPermission.FollowsService;
import com.Kkrap.Service.Users.UsersService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ActivityManagerService {
    private final UsersService usersService;
    private final ActivityFeedService activityFeedService;
    private final FoldersService foldersService;
    private final FoldersLinksService foldersLinksService;
    private final LinksService linksService;

    private final FollowsService followsService;

    public ActivityManagerService(
            UsersService usersService,
            ActivityFeedService activityFeedService,
            FoldersService foldersService,
            FoldersLinksService foldersLinksService,
            LinksService linksService,
            FollowsService followsService
    ){
        this.usersService = usersService;
        this.activityFeedService = activityFeedService;
        this.foldersService = foldersService;
        this.foldersLinksService = foldersLinksService;
        this.linksService = linksService;
        this.followsService = followsService;
    }
    public ResponseEntity<List<FeedFolderResponse>> getFeedForUser(Long userId) {
        Users Userme = usersService.findById(userId);
        List<Long> followingIds = followsService.findFollowingIdsByFollowerId(Userme.getUserId());

        LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);
//        List<ActivityFeed> feeds = activityFeedService
//                .findByActorUserIdInAndCreatedAtAfterOrderByCreatedAtDesc(followingIds, oneWeekAgo);
        List<ActivityFeed> feeds = activityFeedService
                .findByActorUserIdInAndCreatedAtAfterOrderByCreatedAtDesc(
                        followingIds,
                        oneWeekAgo,
                        PageRequest.of(0, 50)
                );

        List<FeedFolderResponse> result = new ArrayList<>();

        for (ActivityFeed feed : feeds) {
            Long folderId = feed.getFolderId();
            Folders folder = foldersService.findById(folderId);

            Optional<Links> latestLinkOpt = foldersLinksService.getFirstLinkByFolder(folder);
            String thumbnail = latestLinkOpt.map(Links::getThumbnailUrl).orElse(null);
            String favicon = latestLinkOpt.map(Links::getFaviconUrl).orElse(null);

            FeedFolderResponse response = FeedFolderResponse.of(feed, folder, thumbnail, favicon);
            result.add(response);
        }

        return ResponseEntity.ok(result);
    }



}
