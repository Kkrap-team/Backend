package com.Kkrap.RequestDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LinksCreateRequest {
    private Long userId;

    private String linkUrl;

    private Long foldersId;

    private String linkName;

    private Long defaultFoldersId;
}
