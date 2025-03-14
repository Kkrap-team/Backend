package com.Kkrap.ResponseDto;

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

    private LocalDateTime createTime;

    private String folderName;

    private String folderDescription;

    private boolean isPublic;
}
