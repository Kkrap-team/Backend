package com.Kkrap.RequestDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FoldersCreateRequest {

    private String folderName;

    private String folderDescription;

    private boolean visible;

    private boolean defaultFolder;

    public static FoldersCreateRequest of(String folderName, String folderDescription, boolean visible, boolean defaultFolder){
        return new FoldersCreateRequest(folderName, folderDescription, visible, defaultFolder);
    }
}
