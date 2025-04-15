package com.Kkrap.ResponseDto;
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
    private boolean isPublic;
    private LocalDateTime createTime;
    private List<LinksResponse> links;

    private FoldersLinksAllResponse(){}

    public static  FoldersLinksAllResponse of(Folders folder, List<Links> linksList) {
        Long folderId = folder.getFolderId();
        String folderName = folder.getFolderName();
        String folderDescription = folder.getFolderDescription();
        boolean isPublic = folder.getIsPublic();
        LocalDateTime createTime = folder.getCreateTime();
        List<LinksResponse> links = linksList.stream()
                .map(LinksResponse::new)
                .collect(Collectors.toList());
        return new FoldersLinksAllResponse(folderId, folderName, folderDescription, isPublic, createTime, links);
    }
}
