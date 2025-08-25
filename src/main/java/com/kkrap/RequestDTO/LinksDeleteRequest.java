package com.kkrap.RequestDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class LinksDeleteRequest {


    private Long foldersId;

    private List<Long> linkId;

    private LinksDeleteRequest() {}

    public static LinksDeleteRequest of(Long foldersId, List<Long> deleteLinkIdList ){
        return new LinksDeleteRequest(foldersId, deleteLinkIdList);
    }

}
