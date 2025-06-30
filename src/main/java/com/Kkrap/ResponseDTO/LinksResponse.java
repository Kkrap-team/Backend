package com.Kkrap.ResponseDTO;

import com.Kkrap.Entity.Links;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LinksResponse {
    private Long linkId;
    private String linkUrl;
    private LocalDateTime createTime;
    private String linkName;
    private String thumbnailUrl;
    private String faviconUrl;

    public LinksResponse(Links links) {
        this.linkId = links.getLinkId();
        this.linkUrl = links.getLinkUrl();
        this.createTime = links.getCreateTime();
        this.linkName = links.getLinkName();
        this.thumbnailUrl = links.getThumbnailUrl();
        this.faviconUrl = links.getFaviconUrl();
    }


}
