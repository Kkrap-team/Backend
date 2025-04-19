package com.Kkrap.RequestDTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class LinksDeleteRequest {
    private Long defaultFoldersId;

    private Long foldersId;

    private List<Long> linkId;

    private LinksDeleteRequest() {}

    public static LinksDeleteRequest of(Long defaultFodersId, Long foldersId, List<Long> deleteLinkIdList ){
        return new LinksDeleteRequest(defaultFodersId, foldersId, deleteLinkIdList);
    }

}
