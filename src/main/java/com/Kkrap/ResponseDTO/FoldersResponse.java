package com.Kkrap.ResponseDTO;


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
    private Long viewCount;

    private FoldersResponse() {}


    public static FoldersResponse from(Folders folders, Long userId){
        return new FoldersResponse(folders.getFolderId(), userId, folders.getFolderName(), folders.getFolderDescription(), folders.getCreateTime(), folders.isVisible(), folders.getViewCount());
    }

}
