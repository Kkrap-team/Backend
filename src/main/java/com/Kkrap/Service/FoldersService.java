package com.Kkrap.Service;

import com.Kkrap.Entity.FolderList;
import com.Kkrap.Entity.Folders;
import com.Kkrap.Entity.Links;
import com.Kkrap.Entity.Users;
import com.Kkrap.Exception.FoldersNotFoundException;
import com.Kkrap.Repository.FolderListRepository;
import com.Kkrap.Repository.FoldersRepository;
import com.Kkrap.Repository.LinksRepository;
import com.Kkrap.Repository.UsersRepository;
import com.Kkrap.RequestDTO.FolderCreateRequestDTO;
import com.Kkrap.RequestDTO.FolderDeleteDTO;
import com.Kkrap.RequestDTO.FolderInsertUrlRequestDTO;
import com.Kkrap.ResponseDto.FolderUrlResponseDTO;
import com.Kkrap.ResponseDto.MessageResponseDTO;
import org.apache.catalina.User;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FoldersService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private FoldersRepository foldersRepository;

    @Autowired
    private FolderListRepository folderListRepository;

    @Autowired
    private LinksRepository linksRepository;

    public List<Long> SelectedFoldersId(Long id){
        List<Folders> foldersList = foldersRepository.findByUserId(id);

        if (foldersList.isEmpty())
        {
            throw new FoldersNotFoundException("해당 사용자 ID로 폴더를 찾을 수 없습니다.");
        }

        List<Long> folderIds = foldersList.stream()
                    .map(Folders::getFolderId)
                    .collect(Collectors.toList());

//        // Response DTO 생성 및 설정
//        FoldersReponseDTO responseDTO = new FoldersReponseDTO();
//        responseDTO.setFoldersId(folderIds);

        return folderIds;
    }


    public List<FolderUrlResponseDTO> SelectedFolderUrl(Long folderId, Long userId){
        // folderId로 모든 FolderList 조회
        List<FolderList> folderLists = folderListRepository.findByFolderFolderId(folderId);

        // folderLists에서 linkId 목록 추출
        List<Long> linkIds = folderLists.stream()
                .map(folderList -> folderList.getLink().getLinkId())
                .collect(Collectors.toList());

        // linkId 목록으로 Links 조회
        List<Links> links = linksRepository.findByLinkIdIn(linkIds);

        List<FolderUrlResponseDTO> responseDTOs = links.stream()
                .map(link -> new FolderUrlResponseDTO(link.getLinkId(), link.getLinkUrl(), link.getCreateTime()) )
                .collect(Collectors.toList());

        return responseDTOs;
    }

    //Create
    //폴더를 만드는 것
    public ResponseEntity<MessageResponseDTO> CreateFolder(FolderCreateRequestDTO foldersCreateRequestDTO)
    {
        String folderName = foldersCreateRequestDTO.getFolderName();
        Long userId = foldersCreateRequestDTO.getUserId();

        Folders folder = new Folders(userId, folderName);
        foldersRepository.save(folder);

        MessageResponseDTO responseDTO = new MessageResponseDTO("create Folder Success");
        return ResponseEntity.ok(responseDTO);
    }

    //Delete
    //폴더 삭제
    public ResponseEntity<MessageResponseDTO> DeleteFolder(FolderDeleteDTO folderDeleteDTO)
    {
        Long folderId = folderDeleteDTO.getFolderId();

        Optional<Folders> folder = foldersRepository.findById(folderId);
        if (folder.isPresent())
        {
            foldersRepository.deleteById(folderId);
            MessageResponseDTO message = new MessageResponseDTO("delete Folder Success");
            return ResponseEntity.ok(message);
        }
        else
        {
            return new ResponseEntity("Folder does not exist", HttpStatus.NOT_FOUND);
        }


    }






}
