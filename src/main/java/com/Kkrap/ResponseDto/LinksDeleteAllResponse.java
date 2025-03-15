package com.Kkrap.ResponseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class LinksDeleteAllResponse {
    private Long folderId;
    private Long userId;
    private String folderName;
    private String folderDescription;
    private boolean isPublic;
    private List<LinksResponse> deletedLinks;

    public LinksDeleteAllResponse(Long folderId, Long userId, String folderName,
                                  String folderDescription, boolean isPublic,
                                  List<LinksResponse> deletedLinks) {
        this.folderId = folderId;
        this.userId = userId;
        this.folderName = folderName;
        this.folderDescription = folderDescription;
        this.isPublic = isPublic;
        this.deletedLinks = deletedLinks;
    }
}
