package com.Kkrap.Service.FoldersDocument;

import com.Kkrap.ElasticSearch.FoldersDocument;
import com.Kkrap.Repository.FoldersDocumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
public class FoldersDocumentService {
    private final FoldersDocumentRepository foldersDocumentRepository;

    public FoldersDocumentService(FoldersDocumentRepository foldersDocumentRepository){
        this.foldersDocumentRepository = foldersDocumentRepository;
    }

    public FoldersDocument save(FoldersDocument doc) {
        return foldersDocumentRepository.save(doc);
    }

    public List<FoldersDocument> findAll() {
        return foldersDocumentRepository.findAll();
    }
    public void deleteAll() {
        foldersDocumentRepository.deleteAll();
    }

    public List<FoldersDocument> searchByName(String keyword) {
        return foldersDocumentRepository.findByFolderNameContainingIgnoreCase(keyword);
    }


    public List<FoldersDocument> getTop10ViewCountLastWeek() {
        List<FoldersDocument> docs = foldersDocumentRepository.findTop10ByCreateTimeInLastWeekOrderByViewCountDesc();
        // 혹시 정렬이 부족하면 자바에서 한 번 더 안전하게
        return docs.stream()
                .sorted(Comparator.comparingLong(FoldersDocument::getViewCount).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    public List<FoldersDocument> getTop10LikesCountLastWeek() {
        List<FoldersDocument> docs = foldersDocumentRepository.findTop10ByCreateTimeInLastWeekOrderByLikesCountDesc();
        return docs.stream()
                .sorted(Comparator.comparingLong(FoldersDocument::getLikesCount).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }



}
