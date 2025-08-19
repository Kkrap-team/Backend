package com.Kkrap.RequestDTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FoldersCreateRequest {

    @NotNull
    private String folderName;

    @NotNull
    private String folderDescription;

    private boolean visible;

    private boolean defaultFolder;

    public static FoldersCreateRequest of(String folderName, String folderDescription, boolean visible, boolean defaultFolder){
        return new FoldersCreateRequest(folderName, folderDescription, visible, defaultFolder);
    }
}
