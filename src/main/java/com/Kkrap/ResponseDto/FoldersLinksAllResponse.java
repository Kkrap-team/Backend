package com.Kkrap.ResponseDto;
import com.Kkrap.Entity.Folders;
import com.Kkrap.Entity.Links;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class FoldersLinksAllResponse {
    private Long folderId;
    private String folderName;
    private String folderDescription;
    private boolean isPublic;
    private LocalDateTime createTime;
    private List<LinksResponse> links;

    public FoldersLinksAllResponse(Folders folder, List<Links> linksList) {
        this.folderId = folder.getFolderId();
        this.folderName = folder.getFolderName();
        this.folderDescription = folder.getFolderDescription();
        this.isPublic = folder.isPublic();
        this.createTime = folder.getCreateTime();
        this.links = linksList.stream()
                .map(LinksResponse::new)
                .collect(Collectors.toList());
    }
}
