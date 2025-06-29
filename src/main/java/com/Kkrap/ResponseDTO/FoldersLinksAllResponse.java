package com.Kkrap.ResponseDTO;
import com.Kkrap.Entity.Folders;
import com.Kkrap.Entity.Links;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class FoldersLinksAllResponse {
    private Long folderId;
    private String folderName;
    private String folderDescription;
    private boolean visible;
    private boolean defaultFolder;
    private Long viewCount;
    private Long scrapCount;
    private boolean share;

    private LocalDateTime createTime;
    private List<LinksResponse> links;

    private FoldersLinksAllResponse(){}

    public static  FoldersLinksAllResponse of(Folders folder, List<Links> linksList) {
        Long folderId = folder.getFolderId();
        String folderName = folder.getFolderName();
        String folderDescription = folder.getFolderDescription();
        boolean visible = folder.isVisible();
        boolean defaultFolder = folder.isDefaultFolder();
        Long viewCount = folder.getViewCount();
        Long scrapCount = folder.getScrapCount();
        boolean share = folder.isShared();

        LocalDateTime createTime = folder.getCreateTime();
        List<LinksResponse> links = linksList.stream()
                .map(LinksResponse::new)
                .collect(Collectors.toList());
        return new FoldersLinksAllResponse(folderId, folderName, folderDescription, visible, defaultFolder, viewCount, scrapCount, share, createTime, links);
    }
}
