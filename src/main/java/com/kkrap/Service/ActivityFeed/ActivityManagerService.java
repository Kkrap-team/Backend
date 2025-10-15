package com.kkrap.Service.ActivityFeed;

import com.kkrap.Entity.*;
import com.kkrap.ResponseDTO.FeedFolderResponse;
import com.kkrap.Service.FolderLink.FoldersLinksService;
import com.kkrap.Service.FolderLink.FoldersService;
import com.kkrap.Service.FolderLink.LinksService;
import com.kkrap.Service.FollowsFoldersPermission.FollowsService;
import com.kkrap.Service.Users.UsersService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ActivityManagerService {
    private final UsersService usersService;
    private final ActivityFeedService activityFeedService;
    private final FoldersService foldersService;
    private final FoldersLinksService foldersLinksService;

    private final FollowsService followsService;

    public ActivityManagerService(
            UsersService usersService,
            ActivityFeedService activityFeedService,
            FoldersService foldersService,
            FoldersLinksService foldersLinksService,
            FollowsService followsService
    ){
        this.usersService = usersService;
        this.activityFeedService = activityFeedService;
        this.foldersService = foldersService;
        this.foldersLinksService = foldersLinksService;
        this.followsService = followsService;
    }
    public ResponseEntity<List<FeedFolderResponse>> getFeedForUser(Long userId) {
        Users Userme = usersService.findById(userId);
        List<Long> followingIds = followsService.findFollowingIdsByFollowerId(Userme.getUserId());

        LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(90);
        List<ActivityFeed> feeds = activityFeedService
                .findByActorUserIdInAndCreatedAtAfterOrderByCreatedAtDesc(
                        followingIds,
                        oneWeekAgo,
                        PageRequest.of(0, 200)
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
