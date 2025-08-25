package com.kkrap.RequestDTO;

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

    public static FoldersCreateRequest of(String folderName, String folderDescription, boolean visible){
        return new FoldersCreateRequest(folderName, folderDescription, visible);
    }
}
