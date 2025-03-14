package com.Kkrap.Service;

import com.Kkrap.Entity.Folders;
import com.Kkrap.Entity.FoldersLinks;
import com.Kkrap.Entity.Links;
import com.Kkrap.Entity.Users;
import com.Kkrap.Repository.FoldersLinksRepository;
import com.Kkrap.Repository.FoldersRepository;
import com.Kkrap.Repository.LinksRepository;
import com.Kkrap.Repository.UsersRepository;
import com.Kkrap.RequestDTO.FoldersCreateRequest;
import com.Kkrap.RequestDTO.FolderDeleteDTO;
import com.Kkrap.ResponseDto.FoldersLinksAllResponse;
import com.Kkrap.ResponseDto.FoldersResponse;
import com.Kkrap.ResponseDto.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
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

    public ResponseEntity<List<FoldersLinksAllResponse>> getFoldersAll(Long userId){
        Optional<Users> userOptional = usersRepository.findById(userId);
        //사용자가 없으면 빈 리스트
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.emptyList());
        }
        // userId를 통해 전부 가져오기
        List<Folders> folders = foldersRepository.findByUserUserId(userId);

        List<FoldersLinksAllResponse> responseList = folders.stream().map(folder -> {
            // 해당 폴더의 FoldersLinks 조회 (folder_id 기준)
            // 특정 폴더(folder_id)에 해당하는 FoldersLinks를 조회
            List<FoldersLinks> folderLinksList = foldersLinksRepository.findByFolders(folder);

            // 각 FoldersLinks에서 link_id 추출하여 Links 조회
            List<Links> linksList = folderLinksList.stream()
                    .map(folderLink -> linksRepository.findById(folderLink.getLinks().getLinkId()).orElse(null))
                    .filter(Objects::nonNull) // 존재하는 Links만 리스트에 추가
                    .collect(Collectors.toList());

            // Folder + Links 리스트를 Response DTO로 변환
            return new FoldersLinksAllResponse(folder, linksList);
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseList);
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
    public ResponseEntity<Object> CreateFolder(Long userId, FoldersCreateRequest foldersCreateRequest)
    {
        Optional<Users> usersOptional = usersRepository.findById(userId);
        if (usersOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(404,"사용자를 찾을 수 없음"));
        }


        String folderName = foldersCreateRequest.getFolderName();

        String folderDescription = foldersCreateRequest.getFolderDescription();
        boolean isPublic = foldersCreateRequest.getisPublic();

        Folders folder = new Folders(usersOptional.get(), folderName, folderDescription, isPublic);
        foldersRepository.save(folder);

        return ResponseEntity.ok(foldersCreateRequest);
    }

    //Delete
    //폴더 삭제
    public ResponseEntity<MessageResponse> DeleteFolder(FolderDeleteDTO folderDeleteDTO)
    {
        Long folderId = folderDeleteDTO.getFolderId();

        Optional<Folders> folder = foldersRepository.findById(folderId);
        if (folder.isPresent())
        {
            foldersRepository.deleteById(folderId);
            MessageResponse message = new MessageResponse(404, "delete Folder Success");
            return ResponseEntity.ok(message);
        }
        else
        {
            return new ResponseEntity("Folder does not exist", HttpStatus.NOT_FOUND);
        }


    }






}
