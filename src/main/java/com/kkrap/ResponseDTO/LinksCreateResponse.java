package com.kkrap.ResponseDTO;

import com.kkrap.Entity.Links;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class LinksCreateResponse {

    private Long foldersId;

    private String linkUrl;

    private LocalDateTime createTime;

    private String linkName;

    private String thumbnailUrl;

    private String faviconUrl;

    private LinksCreateResponse() {}

    public static LinksCreateResponse of(Links links, Long foldersId){
        return new LinksCreateResponse(foldersId, links.getLinkUrl(), links.getCreateTime(), links.getLinkName(), links.getThumbnailUrl(), links.getFaviconUrl());
    }

}

