package com.Kkrap.RequestDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FoldersCreateRequest {

    @Schema(description = "폴더이름", example = "부산 여행", required = true)
    private String folderName;

    @Schema(description = "폴더설명", example = "부산 여행 링크 모읍집입니다.", required = true)
    private String folderDescription;


    @Schema(description = "폴더공개여부", example = "true", required = true)
    private boolean visible;

    public static FoldersCreateRequest of(String folderName, String folderDescription, boolean visible){
        return new FoldersCreateRequest(folderName, folderDescription, visible);

    }


}
