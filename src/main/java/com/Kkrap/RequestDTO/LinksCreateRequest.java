package com.Kkrap.RequestDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LinksCreateRequest {

    @Schema(description = "저장할 링크 URL", example = "https://~~~~", required = true)
    private String linkUrl;

    @Schema(description = "저장할 폴더 Id", example = "3", required = true)
    private Long foldersId;

    @Schema(description = "모든 링크 폴더 Id", example = "4", required = true)
    private Long defaultFoldersId;
}
