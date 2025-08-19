package com.Kkrap.Service.FolderLink;

import com.Kkrap.Entity.Folders;
import com.Kkrap.Entity.FoldersLinks;
import com.Kkrap.Entity.Links;
import com.Kkrap.Exception.FoldersNotFoundLinksException;
import com.Kkrap.Repository.FoldersLinksRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FoldersLinksService {

    private final FoldersLinksRepository foldersLinksRepository;

    public FoldersLinksService(FoldersLinksRepository foldersLinksRepository){
        this.foldersLinksRepository = foldersLinksRepository;
    }

    public FoldersLinks save(FoldersLinks foldersLinks){
        return foldersLinksRepository.save(foldersLinks);
    }

    //폴더에 대해 연결된 모든 링크다 주기
    public List<FoldersLinks> findByFolders(Folders folders){
        return foldersLinksRepository.findByFolders(folders);
    }

    public void deleteAll(List<FoldersLinks> toDelete){
        foldersLinksRepository.deleteAll(toDelete);
    }

    public void delete(FoldersLinks fl){
        foldersLinksRepository.delete(fl);
    }

    public Optional<FoldersLinks> findFirstByFoldersOrderByLinksCreateTimeDesc(Folders folders){
        return foldersLinksRepository.findFirstByFoldersOrderByLinksCreateTimeDesc(folders);
    }


    // folderId 기준으로 가장 최근 링크 하나 조회
    public Optional<Links> getFirstLinkByFolder(Folders folder) {
        return findFirstByFoldersOrderByLinksCreateTimeDesc(folder).map(FoldersLinks::getLinks);
    }

    FoldersLinks findByUserIdAndFoldersFolderIdAndLinksLinkId(Long userId, Long folderId, Long linkId)
    {
        return foldersLinksRepository.findByUserIdAndFoldersFolderIdAndLinksLinkId(userId, folderId, linkId).orElseThrow(() ->  FoldersNotFoundLinksException.of("소스 폴더에 해당 링크가 없습니다."));
    }

    boolean existsByUserIdAndFoldersFolderIdAndLinksLinkId(
            Long userId, Long folderId, Long linkId
    ){
        return foldersLinksRepository.existsByUserIdAndFoldersFolderIdAndLinksLinkId(userId, folderId, linkId);
    }

    void deleteByUserIdAndFoldersFolderIdAndLinksLinkId(
            Long userId, Long folderId, Long linkId)
    {
        foldersLinksRepository.deleteByUserIdAndFoldersFolderIdAndLinksLinkId(userId, folderId, linkId);
    }


}
