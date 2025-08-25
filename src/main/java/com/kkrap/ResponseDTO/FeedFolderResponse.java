package com.kkrap.ResponseDTO;

import com.kkrap.Entity.ActivityFeed;
import com.kkrap.Entity.Folders;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class FeedFolderResponse {
    Long folderId;
    LocalDateTime createdAt;
    private String folderName;
    private String folderDescription;
    private boolean visible;
    private Long viewCount;
    private Long scrapCount;
    private boolean shared;

    private String thumbnailUrl;
    private String faviconUrl;


    Long userId;
    String email;
    private String nickname;
    private String profile;
    private String bio;

    private FeedFolderResponse(){}

    public static FeedFolderResponse of(ActivityFeed feed, Folders folder, String thumbnail, String favicon){
        return new FeedFolderResponse(
                folder.getFolderId(),
                feed.getCreatedAt(),
                folder.getFolderName(),
                folder.getFolderDescription(),
                folder.isVisible(),
                folder.getViewCount(),
                folder.getScrapCount(),
                folder.isShared(),
                thumbnail,
                favicon,
                folder.getUser().getUserId(),
                folder.getUser().getEmail(),
                folder.getUser().getNickname(),
                folder.getUser().getProfile(),
                folder.getUser().getBio()
        );
    }

}
