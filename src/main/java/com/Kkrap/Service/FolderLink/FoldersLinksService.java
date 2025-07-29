package com.Kkrap.Service.FolderLink;

import com.Kkrap.Entity.Folders;
import com.Kkrap.Entity.FoldersLinks;
import com.Kkrap.Entity.Links;
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


}
