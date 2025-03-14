package com.Kkrap.Service;

import com.Kkrap.Entity.Folders;
import com.Kkrap.Entity.Users;
import com.Kkrap.Repository.FoldersLinksRepository;
import com.Kkrap.Repository.FoldersRepository;
import com.Kkrap.Repository.LinksRepository;
import com.Kkrap.Repository.UsersRepository;
import com.Kkrap.RequestDTO.FolderCreateRequest;
import com.Kkrap.RequestDTO.FolderDeleteDTO;
import com.Kkrap.ResponseDto.FoldersResponse;
import com.Kkrap.ResponseDto.MessageResponseDTO;
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
    private FoldersLinksRepository foldersLinksRepository;

    @Autowired
    private LinksRepository linksRepository;

    public List<FoldersResponse> getFoldersAll(Long userId){
        Optional<Users> userOptional = usersRepository.findById(userId);

        if (userOptional.isEmpty()) {
            return null; // 사용자 없음
        }

        List<Folders> folders = foldersRepository.findByUserUserId(userId);
        // Folders -> FolderResponseDTO 변환
        return folders.stream()
                .map(folder -> new FoldersResponse(folder.getFolderId(), folder.getUser().getUserId(), folder.getCreateTime(), folder.getFolderName(), folder.getFolderDescription(), folder.isPublic()))
                .collect(Collectors.toList());

//        List<Long> folderIds = foldersList.stream()
//                    .map(Folders::getFolderId)
//                    .collect(Collectors.toList());
//        return folderIds;
    }


//    public List<FolderUrlResponseDTO> SelectedFolderUrl(Long folderId, Long userId){
//        // folderId로 모든 FolderList 조회
//        List<FolderList> folderLists = folderListRepository.findByFolderFolderId(folderId);
//
//        // folderLists에서 linkId 목록 추출
//        List<Long> linkIds = folderLists.stream()
//                .map(folderList -> folderList.getLink().getLinkId())
//                .collect(Collectors.toList());
//
//        // linkId 목록으로 Links 조회
//        List<Links> links = linksRepository.findByLinkIdIn(linkIds);
//
//        List<FolderUrlResponseDTO> responseDTOs = links.stream()
//                .map(link -> new FolderUrlResponseDTO(link.getLinkId(), link.getLinkUrl(), link.getCreateTime()) )
//                .collect(Collectors.toList());
//
//        return responseDTOs;
//    }

    //Create
    //폴더를 만드는 것
    public ResponseEntity<MessageResponseDTO> CreateFolder(FolderCreateRequest foldersCreateRequest)
    {
        String folderName = foldersCreateRequest.getFolderName();
        Long userId = foldersCreateRequest.getUserId();
        String folderDescription = foldersCreateRequest.getFolderDescription();
        boolean isPublic = foldersCreateRequest.getisPublic();

        Folders folder = new Folders(userId, folderName, folderDescription, isPublic);
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
