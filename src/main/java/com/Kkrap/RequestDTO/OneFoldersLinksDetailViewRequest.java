package com.Kkrap.RequestDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OneFoldersLinksDetailViewRequest {
    private Long targetUserId; // 상대방 userId
    private Long folderId;     // 조회하려는 폴더 id
}
