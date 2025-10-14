package com.kkrap.Repository;

import com.kkrap.ElasticSearch.FoldersDocument;
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




//    List<FoldersDocument> findTop10ByFolderNameContainingIgnoreCase(String keyword);

    /**
     * simple_query_string 으로 안전하게 검색.
     * - default_operator AND: 공백으로 구분된 모든 토큰이 포함되도록
     * - analyze_wildcard: 우리가 붙인 * 도 분석되게
     */
    @Query("""
    {
      "simple_query_string": {
        "query": "?0",
        "fields": ["folderName^2"],
        "default_operator": "AND",
        "analyze_wildcard": true
      }
    }
    """)
    Page<FoldersDocument> searchByKeywordSimple(String normalizedQuery, Pageable pageable);


    // 지난 7일 이내 + viewCount 내림차순
    @Query("""
    {
      "bool": {
        "filter": [
          { "range": { "createTime": { "gte": "now-90d/d" } } }
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
          { "range": { "createTime": { "gte": "now-90d/d" } } }
        ]
      }
    }
    """)
    List<FoldersDocument> findTop10ByCreateTimeInLastWeekOrderByScrapCountDesc();

    List<FoldersDocument> findByFolderIdIn(List<Long> folderIds);


    List<FoldersDocument> findTop25ByOrderByViewCountDesc();


    List<FoldersDocument> findTop25ByOrderByScrapCountDesc();





}
