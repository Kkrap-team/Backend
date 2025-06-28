package com.Kkrap.Service.FoldersDocument;

import com.Kkrap.ElasticSearch.FoldersDocument;
import com.Kkrap.Entity.Folders;
import com.Kkrap.Entity.Users;
import com.Kkrap.Repository.FoldersDocumentRepository;
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
    public void indexNewFolder(Folders folder) {
        FoldersDocument doc = FoldersDocument.from(folder);
        save(doc);
    }

    //색인 삭제
    public void deleteFolderDocument(Folders folder) {
        deleteById(folder.getFolderId());
    }

    @Transactional
    public void updateUserInfoInFolderDocuments(Users users, List<Folders> userFolders) {
        // 색인 업데이트
        userFolders.forEach(folder -> {
            indexNewFolder(folder);
        });

        System.out.println("[Elasticsearch] 사용자 정보 변경으로 색인 업데이트 완료: userId=" + users.getUserId());
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



}
