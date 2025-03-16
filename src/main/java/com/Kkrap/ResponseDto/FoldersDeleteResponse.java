package com.Kkrap.ResponseDto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FoldersDeleteResponse {
    private Long folderId;
    private Long userId;
    private String folderName;
    private String folderDescription;
    private boolean isPublic;
}
