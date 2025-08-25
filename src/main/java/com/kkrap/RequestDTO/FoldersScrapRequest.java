package com.kkrap.RequestDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FoldersScrapRequest {

    private Long sourceFolderId;
    private String folderName;
    private String folderDescription;
    private boolean visible;

    private FoldersScrapRequest(){}

    public static FoldersScrapRequest of(){
        return new FoldersScrapRequest();

    }
}
