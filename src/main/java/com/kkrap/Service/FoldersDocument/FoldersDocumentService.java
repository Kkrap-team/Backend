package com.kkrap.Service.FoldersDocument;

import com.kkrap.ElasticSearch.FoldersDocument;
import com.kkrap.Entity.Folders;
import com.kkrap.Entity.Links;
import com.kkrap.Entity.Users;
import com.kkrap.Repository.FoldersDocumentRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
public class FoldersDocumentService {
    private final FoldersDocumentRepository foldersDocumentRepository;

    public FoldersDocumentService(FoldersDocumentRepository foldersDocumentRepository){
        this.foldersDocumentRepository = foldersDocumentRepository;
    }

    //우리 백엔드에서 사용할 DB 전체를 색인
    //폴더 생성 후 -> 카프카에서 이걸 실행
    public void indexNewFolder(Folders folder, Links link) {
        FoldersDocument doc = FoldersDocument.from(folder, link);
        save(doc);
    }

    //색인 삭제
    public void deleteFolderDocument(Folders folder) {
        deleteById(folder.getFolderId());
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


//    public List<FoldersDocument> findTop10ByFolderNameContainingIgnoreCase(String keyword) {
//        return foldersDocumentRepository.findTop10ByFolderNameContainingIgnoreCase(keyword);
//    }

    List<FoldersDocument> searchByKeywordSimple(String normalizedQuery, Pageable size){
        return foldersDocumentRepository.searchByKeywordSimple(normalizedQuery, size).getContent();
    }



    public List<FoldersDocument> findByFolderNameContainingIgnoreCase(String keyword) {
        return foldersDocumentRepository.findByFolderNameContainingIgnoreCase(keyword);
    }

    public void deleteById(Long folderId){
        foldersDocumentRepository.deleteById(folderId);
    }


    public List<FoldersDocument> getTop10ViewCountLastWeek() {
        List<FoldersDocument> docs = foldersDocumentRepository.findTop10ByCreateTimeInLastWeekOrderByViewCountDesc();
        // 혹시 정렬이 부족하면 자바에서 한 번 더 안전하게
        return docs.stream()
                .sorted(Comparator.comparingLong(FoldersDocument::getViewCount).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    public List<FoldersDocument> getTop10ScrapCountLastWeek() {
        List<FoldersDocument> docs = foldersDocumentRepository.findTop10ByCreateTimeInLastWeekOrderByScrapCountDesc();
        return docs.stream()
                .sorted(Comparator.comparingLong(FoldersDocument::getScrapCount).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateUserInfoInFolderDocuments(Users users, Folders userFolder, Links link) {
        // 색인 업데이트
        indexNewFolder(userFolder, link);
    }


    public List<FoldersDocument> findByFolderIdIn(List<Long> folderIds) {
        return foldersDocumentRepository.findByFolderIdIn(folderIds);
    }

    public List<FoldersDocument> findTopNByOrderByCreateTimeDesc(int n) {
        Pageable pageable = PageRequest.of(0, n, Sort.by(Sort.Direction.DESC, "createTime"));
        return foldersDocumentRepository.findAll(pageable).getContent();
    }


    public List<FoldersDocument> getTop25ViewCountLastWeek() {
        List<FoldersDocument> docs = foldersDocumentRepository.findTop25ByCreateTimeInLastWeekOrderByViewCountDesc();
        return docs.stream()
                .sorted(Comparator.comparingLong(FoldersDocument::getViewCount).reversed())
                .limit(25)
                .toList();
    }

    public List<FoldersDocument> getTop25ScrapCountLastWeek() {
        List<FoldersDocument> docs = foldersDocumentRepository.findTop25ByCreateTimeInLastWeekOrderByScrapCountDesc();
        return docs.stream()
                .sorted(Comparator.comparingLong(FoldersDocument::getScrapCount).reversed())
                .limit(25)
                .toList();
    }






}
