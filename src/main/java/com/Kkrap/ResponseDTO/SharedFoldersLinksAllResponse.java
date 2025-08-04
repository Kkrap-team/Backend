package com.Kkrap.ResponseDTO;

import com.Kkrap.Entity.Folders;
import com.Kkrap.Entity.Links;
import com.Kkrap.Entity.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class SharedFoldersLinksAllResponse {
    private Long folderId;
    private String folderName;
    private String folderDescription;
    private boolean visible;
    private boolean defaultFolder;
    private Long viewCount;
    private Long scrapCount;
    private boolean share;

    private LocalDateTime createTime;
    private Long ownerUserId;
    private String ownerNickname;
    private List<LinksResponse> links;

    private SharedFoldersLinksAllResponse() {}

    public static SharedFoldersLinksAllResponse of(Folders folder, List<Links> linksList) {
        Users owner = folder.getUser();  // Folders → Users (소유자)
        return new SharedFoldersLinksAllResponse(
                folder.getFolderId(),
                folder.getFolderName(),
                folder.getFolderDescription(),
                folder.isVisible(),
                folder.isDefaultFolder(),
                folder.getViewCount(),
                folder.getScrapCount(),
                folder.isShared(),
                folder.getCreateTime(),
                owner.getUserId(),
                owner.getNickname(),
                linksList.stream().map(LinksResponse::new).collect(Collectors.toList())
        );
    }
}
