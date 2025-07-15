package com.Kkrap.ResponseDTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UserFoldersWithSharedResponse {
    private List<FoldersLinksAllResponse> ownFolders;
    private List<FoldersLinksAllResponse> sharedFolders;


    private UserFoldersWithSharedResponse() {}

    public static UserFoldersWithSharedResponse of(List<FoldersLinksAllResponse> ownFolders, List<FoldersLinksAllResponse> sharedFolders){
        return new UserFoldersWithSharedResponse(ownFolders, sharedFolders);
    }

}