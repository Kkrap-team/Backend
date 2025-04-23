package com.Kkrap.ResponseDto;

import com.Kkrap.Entity.Links;
import com.Kkrap.RequestDTO.LinksCreateRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class LinksCreateResponse {

    private Long foldersId;

    private Long defaultFoldersId;

    private String linkUrl;

    private LocalDateTime createTime;

    private String linkName;

    private String thumbnailUrl;

    private String faviconUrl;

    private LinksCreateResponse() {}

    public static LinksCreateResponse of(Links links, Long defaultFoldersId, Long foldersId){
        return new LinksCreateResponse(foldersId, defaultFoldersId, links.getLinkUrl(), links.getCreateTime(), links.getLinkName(), links.getThumbnailUrl(), links.getFaviconUrl());
    }

}

