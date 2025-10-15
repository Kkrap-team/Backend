package com.kkrap.Service.FolderLink;

import com.kkrap.Entity.Folders;
import com.kkrap.Entity.Users;
import com.kkrap.Exception.FoldersNotFoundException;
import com.kkrap.Exception.FoldersSameMoveException;
import com.kkrap.Repository.FoldersRepository;
import com.kkrap.RequestDTO.FoldersCreateRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FoldersService {

    private FoldersRepository foldersRepository;

    public FoldersService(FoldersRepository foldersRepository){
        this.foldersRepository = foldersRepository;
    }

    public Folders save(FoldersCreateRequest foldersCreateRequest, Users users){
        Folders folders = Folders.of(foldersCreateRequest, users);
        foldersRepository.save(folders);
        return folders;
    }

    public Folders save(Folders folders){
        foldersRepository.save(folders);
        return folders;
    }

    //사용자가 가진 모든 폴더 조회
    public List<Folders> findByUserUserId(Long userId){
        List<Folders> folders = foldersRepository.findByUserUserId(userId);
        if (folders.isEmpty()){
            throw FoldersNotFoundException.of("폴더가 존재하지 않습니다.");
        }
        return folders;
    }

    //하나 폴더 조회
    public Folders findById(Long folderId){
        return foldersRepository.findById(folderId).orElseThrow(() -> FoldersNotFoundException.of("폴더가 존재하지 않습니다."));
    }


    public void deleteById(Long folderId){
        foldersRepository.deleteById(folderId);
    }

    public void isOwnedByService(Folders folders ,Long userId) {
        if (!folders.isOwnedBy(userId)) {
            throw FoldersNotFoundException.of("권한이 없는 폴더 입니다.");
        }
    }

    public List<Folders> findAll(){
        return foldersRepository.findAll();
    }

    public List<Folders> findByUserIdAndVisibleTrue(Long userId) {
        return foldersRepository.findByUserUserIdAndVisibleTrue(userId);
    }





    public Long sumViewCountByUser(Users user) {
        return foldersRepository.sumViewCountByUser(user);
    }

    public Long sumScrapCountByUser(Users users){
        return foldersRepository.sumScrapCountByUser(users);
    }

    public List<Folders> findByVisibleTrue(){
        return foldersRepository.findByVisibleTrue();
    }


    void foldersSameMove(){
        throw FoldersSameMoveException.of("같은 폴더로 이동할 수 없습니다.");
    }


    void sameScrapfolders(){
        throw FoldersSameMoveException.of("내가 가진 폴더는 스크랩할 수 없습니다.");
    }

    public List<Folders> fetchVisibleFoldersPageGlobal(LocalDateTime cursorTime, Long cursorId, int limitPlusOne) {
        return foldersRepository.fetchVisibleFoldersPageGlobal(cursorTime, cursorId, limitPlusOne);
    }

}
