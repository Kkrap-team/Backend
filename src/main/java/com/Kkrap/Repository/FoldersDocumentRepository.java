package com.Kkrap.Repository;

import com.Kkrap.ElasticSearch.FoldersDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface FoldersDocumentRepository extends ElasticsearchRepository<FoldersDocument, Long> {
    List<FoldersDocument> findAll();

    Page<FoldersDocument> findAll(Pageable pageable);


    void deleteAll();

    List<FoldersDocument> findByFolderNameContainingIgnoreCase(String keyword);

    List<FoldersDocument> findTop10ByFolderNameContainingIgnoreCase(String keyword);


    // 지난 7일 이내 + viewCount 내림차순
    @Query("""
    {
      "bool": {
        "filter": [
          { "range": { "createTime": { "gte": "now-7d/d" } } }
        ]
      }
    }
    """)
    List<FoldersDocument> findTop10ByCreateTimeInLastWeekOrderByViewCountDesc();

    // 지난 7일 이내 + ScrapCount 내림차순
    @Query("""
    {
      "bool": {
        "filter": [
          { "range": { "createTime": { "gte": "now-7d/d" } } }
        ]
      }
    }
    """)
    List<FoldersDocument> findTop10ByCreateTimeInLastWeekOrderByScrapCountDesc();

    List<FoldersDocument> findTop40ByOrderByCreateTimeDesc();

    List<FoldersDocument> findByFolderIdIn(List<Long> folderIds);




}
