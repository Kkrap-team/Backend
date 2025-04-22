package com.Kkrap.RequestDTO;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class LinksDeleteRequest {

    @Schema(description = "모든 링크 폴더 Id", example = "3", required = true)
    private Long defaultFoldersId;

    @Schema(description = "링크를 삭제할 폴더 Id", example = "4", required = true)
    private Long foldersId;

    @Schema(description = "삭제할 링크들은 list로 만들어서 보내주시면 됩니다.", example = "[1,2,3,4]", required = true)
    private List<Long> linkId;

    private LinksDeleteRequest() {}

    public static LinksDeleteRequest of(Long defaultFodersId, Long foldersId, List<Long> deleteLinkIdList ){
        return new LinksDeleteRequest(defaultFodersId, foldersId, deleteLinkIdList);
    }

}
