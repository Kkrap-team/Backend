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
    private Long userId;

    private String folderName;

    private String folderDescription;

    private boolean isPublic;

    public boolean getisPublic(){
        return isPublic;
    }

    public static FoldersCreateRequest of(Long userId, String folderName, String folderDescription, boolean isPublic){
        return new FoldersCreateRequest(userId, folderName, folderDescription, isPublic);

    }


}
