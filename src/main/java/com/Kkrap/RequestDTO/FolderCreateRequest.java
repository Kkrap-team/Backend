package com.Kkrap.RequestDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FolderCreateRequest {
    private Long userId;

    private String folderName;

    private String folderDescription;

    private boolean isPublic;

    public boolean getisPublic(){
        return isPublic;
    }
}
