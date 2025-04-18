package com.Kkrap.ResponseDto;


import com.Kkrap.Entity.Folders;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class FoldersResponse {
    private Long folderId;
    private Long userId;
    private String folderName;
    private String folderDescription;
    private LocalDateTime createTime;
    private boolean visible;

    private FoldersResponse() {}


    public static FoldersResponse from(Folders folders){
        return new FoldersResponse(folders.getFolderId(), folders.getFolderId(), folders.getFolderName(), folders.getFolderDescription(), folders.getCreateTime(), folders.isVisible());
    }

}
