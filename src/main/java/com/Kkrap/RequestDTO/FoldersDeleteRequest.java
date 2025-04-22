package com.Kkrap.RequestDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FoldersDeleteRequest {
    @Schema(description = "삭제할 폴더 Id", example = "2", required = true)
    private Long folderId;
}
