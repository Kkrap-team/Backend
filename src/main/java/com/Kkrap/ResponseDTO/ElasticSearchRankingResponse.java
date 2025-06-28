package com.Kkrap.ResponseDTO;

import com.Kkrap.ElasticSearch.FoldersDocument;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ElasticSearchRankingResponse {
    private List<FoldersDocument> topViewCount;
    private List<FoldersDocument> topLikesCount;
}