package com.Kkrap.ResponseDTO;

import com.Kkrap.ElasticSearch.FoldersDocument;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScrollFolderResponse {

    private Long folderId;
    private String folderName;
    private String folderDescription;
    private LocalDateTime createTime;
    private Boolean visible;
    private boolean shared;

    private Long viewCount;
    private Long scrapCount;

    private Long userId;
    private String nickname;
    private String profileImage;

    private String thumbnailUrl;
    private String faviconUrl;

    public static ScrollFolderResponse from(FoldersDocument doc) {
        return new ScrollFolderResponse(
                doc.getFolderId(),
                doc.getFolderName(),
                doc.getFolderDescription(),
                LocalDateTime.parse(doc.getCreateTime()),
                doc.getVisible(),
                doc.isShared(),
                doc.getViewCount(),
                doc.getScrapCount(),
                doc.getUserId(),
                doc.getNickname(),
                doc.getProfileImage(),
                doc.getThumbnailUrl(),
                doc.getFaviconUrl()
        );
    }
}
