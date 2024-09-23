package com.Kkrap.ResponseDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FolderUrlResponseDTO {
    private Long linkId;

    private String linkUrl;

    private LocalDateTime createTime;

    public FolderUrlResponseDTO(Long linkId, String linkUrl, LocalDateTime createTime){
        this.linkId =  linkId;
        this.linkUrl = linkUrl;
        this.createTime = createTime;

    }


}
