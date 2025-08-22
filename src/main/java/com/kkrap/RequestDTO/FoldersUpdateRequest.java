package com.kkrap.RequestDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FoldersUpdateRequest {
    private Long folderId;
    private String folderName;
    private String folderDescription;
    private boolean visible;
}
