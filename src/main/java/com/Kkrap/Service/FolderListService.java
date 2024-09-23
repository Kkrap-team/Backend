package com.Kkrap.Service;

import com.Kkrap.Entity.FolderList;
import com.Kkrap.Entity.Folders;
import com.Kkrap.Entity.Links;
import com.Kkrap.Entity.Users;
import com.Kkrap.Repository.FolderListRepository;
import com.Kkrap.Repository.FoldersRepository;
import com.Kkrap.Repository.LinksRepository;
import com.Kkrap.Repository.UsersRepository;
import com.Kkrap.RequestDTO.FolderInsertUrlRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FolderListService {
    @Autowired
    private FolderListRepository folderListRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private FoldersRepository foldersRepository;

    @Autowired
    private LinksRepository linksRepository;

    //폴더에 url을 넣는 api
    public ResponseEntity<FolderInsertUrlRequestDTO> InsertUrlFolder(FolderInsertUrlRequestDTO folderInsertUrlRequestDTO)
    {
        System.out.println(folderInsertUrlRequestDTO.getFolderId()+ " " + folderInsertUrlRequestDTO.getUserId());
//        //먼저 folderId와 userId를 가진 folder가 있는지 체크
//        Optional<Folders> folder = foldersRepository.findById(folderInsertUrlRequestDTO.getFolderId());
//        if (folder.isEmpty())
//        {
//            return new ResponseEntity("Folder does not exist", HttpStatus.NOT_FOUND);
//        }
        //사용자가 있는지 검사
        Optional<Users> user = usersRepository.findById(
                folderInsertUrlRequestDTO.getUserId());

        if (user.isEmpty())
        {
            return new ResponseEntity("User does not exist", HttpStatus.NOT_FOUND);
        }

        //사용자가 폴더를 가지고 있는지 검사
        Optional<Folders> folder = foldersRepository.findByFolderIdAndUserId(
                folderInsertUrlRequestDTO.getFolderId(),
                folderInsertUrlRequestDTO.getUserId());
        if (folder.isEmpty())
        {
            return new ResponseEntity("Folder does not exist", HttpStatus.NOT_FOUND);
        }

        List<Long> linkIds = folderInsertUrlRequestDTO.getLinkId();
        linkIds.stream()
                .map(linkId -> {
                    FolderList folderList = new FolderList();
                    folderList.setFolder(folder.get());
                    Optional<Links> links = linksRepository.findById(linkId);
                    if (links.isPresent()) {
                        folderList.setLink(links.get());
                        return folderListRepository.save(folderList);
                    } else {
                        return new ResponseEntity<>("Url does not exist", HttpStatus.NOT_FOUND);
                    }
                }).collect(Collectors.toList());
        return ResponseEntity.ok(folderInsertUrlRequestDTO);
    }
}
