package com.kkrap.ResponseDTO;

import com.kkrap.ElasticSearch.FoldersDocument;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RankingResponse {
    private List<FoldersDocument> topViewCount;
    private List<FoldersDocument> topscrapCount;
}