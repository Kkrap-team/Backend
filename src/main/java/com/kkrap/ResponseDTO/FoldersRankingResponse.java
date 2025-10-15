package com.kkrap.ResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class FoldersRankingResponse {
    private List<OneFoldersAllLinksResponse> topViewCount;
    private List<OneFoldersAllLinksResponse> topscrapCount;

    private FoldersRankingResponse() {}

    public static FoldersRankingResponse of(List<OneFoldersAllLinksResponse> topViewCount, List<OneFoldersAllLinksResponse> topscrapCount){
        return new FoldersRankingResponse(topViewCount, topscrapCount);
    }
}