package com.Kkrap.Service.FolderLink;

import com.Kkrap.Entity.Folders;
import com.Kkrap.Entity.Users;
import com.Kkrap.Exception.FoldersNotFoundException;
import com.Kkrap.Repository.FoldersRepository;
import com.Kkrap.RequestDTO.FoldersCreateRequest;
import com.Kkrap.ResponseDto.FoldersResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

    //폴더 수정
    public ResponseEntity<FoldersResponse> updateFolderMetadata(Long folderId, Long userId, String folderName, String folderDescription, boolean visible){
        Folders folders = findById(folderId);
        if (!folders.isOwnedBy(userId)) {
            throw FoldersNotFoundException.of("폴더가 존재하지 않습니다.");
        }
        folders.setFolderName(folderName);
        folders.setFolderDescription(folderDescription);
        folders.setVisible(visible);
        save(folders);
        return ResponseEntity.ok(FoldersResponse.from(folders, userId));
    }

    public void isOwnedByService(Folders folders ,Long userId) {
        if (!folders.isOwnedBy(userId)) {
            throw FoldersNotFoundException.of("폴더가 존재하지 않습니다.");
        }
    }



}
