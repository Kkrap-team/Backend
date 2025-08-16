package com.Kkrap.RequestDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FoldersAllLinksViewRequest {
    private Long targetUserId; // 상대방 userId
}