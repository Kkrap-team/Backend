package com.Kkrap.Service;

import com.Kkrap.Entity.Folders;
import com.Kkrap.Entity.FoldersLinks;
import com.Kkrap.Repository.FoldersLinksRepository;
import com.Kkrap.Repository.FoldersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoldersLinksService {
    @Autowired
    private FoldersLinksRepository foldersLinksRepository;

    @Autowired
    private FoldersRepository foldersRepository;

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

}
