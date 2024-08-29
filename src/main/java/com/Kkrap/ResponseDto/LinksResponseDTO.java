package com.Kkrap.ResponseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LinksResponseDTO {
    private Long linkId;
    private String linkUrl;
    private Long userId;

    public LinksResponseDTO(Long linkId, String linkUrl, Long userId) {
        this.linkId = linkId;
        this.linkUrl = linkUrl;
        this.userId = userId;
    }
}
