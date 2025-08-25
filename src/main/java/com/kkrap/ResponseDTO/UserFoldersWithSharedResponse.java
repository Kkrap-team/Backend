package com.kkrap.ResponseDTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UserFoldersWithSharedResponse {
    private List<FoldersLinksAllResponse> ownFolders;
    private List<SharedFoldersLinksAllResponse> sharedFolders;


    private UserFoldersWithSharedResponse() {}

    public static UserFoldersWithSharedResponse of(List<FoldersLinksAllResponse> ownFolders, List<SharedFoldersLinksAllResponse> sharedFolders){
        return new UserFoldersWithSharedResponse(ownFolders, sharedFolders);
    }

}