package com.kkrap.RequestDTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FoldersPermissionsCreateRequest {
    private Long folderId;                // 공유할 폴더 ID
    private List<Long> invitedUserIds;   // 공유 대상 사용자 ID들
}
