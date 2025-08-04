package com.Kkrap.ElasticSearch;

import com.Kkrap.Entity.Folders;
import com.Kkrap.Entity.Links;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDateTime;

@Document(indexName = "folders")
@Getter
@Setter
@AllArgsConstructor
public class FoldersDocument {

    @Id
    private Long folderId;

    private String folderName;
    private String folderDescription;
    private String createTime;
    private Boolean visible;
    private boolean shared;

    private Long viewCount;
    private Long scrapCount;

    private Long userId;
    private String nickname;
    private String profileImage;

    private String thumbnailUrl;
    private String faviconUrl;


    private FoldersDocument(){}

    public static FoldersDocument from(Folders folder, Links firstLink) {
        return new FoldersDocument(
                folder.getFolderId(),
                folder.getFolderName(),
                folder.getFolderDescription(),
                folder.getCreateTime() != null ? folder.getCreateTime().toString() : null,
                folder.isVisible(),
                folder.isShared(),
                folder.getViewCount(),
                folder.getScrapCount(),
                folder.getUser().getUserId(),
                folder.getUser().getNickname(),
                folder.getUser().getProfile(),
                firstLink != null ? firstLink.getThumbnailUrl() : null,
                firstLink != null ? firstLink.getFaviconUrl() : null
        );
    }
}
