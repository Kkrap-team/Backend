package com.Kkrap.RequestDTO;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FoldersUpdateRequest {
    private Long folderId;
    private Long userId;
    private String folderName;
    private String folderDescription;
    private boolean visible;
}
