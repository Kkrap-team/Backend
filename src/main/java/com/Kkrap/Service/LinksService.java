package com.Kkrap.Service;

import com.Kkrap.Entity.Folders;
import com.Kkrap.Entity.FoldersLinks;
import com.Kkrap.Entity.Links;
import com.Kkrap.Entity.Users;
import com.Kkrap.Repository.FoldersLinksRepository;
import com.Kkrap.Repository.FoldersRepository;
import com.Kkrap.Repository.LinksRepository;
import com.Kkrap.Repository.UsersRepository;
import com.Kkrap.RequestDTO.LinksCreateRequest;
import com.Kkrap.ResponseDto.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LinksService {

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private LinksRepository linksRepository;

    @Autowired
    private FoldersRepository foldersRepository;

    @Autowired
    private FoldersLinksRepository foldersLinksRepository;


    //유저별 링크 조회
//    public List<LinksResponseDTO> SelectedMemberLinks(Long user_id){
//        List<Links> links = linksRepository.findByUsers_UserId(user_id);
//        return links.stream()
//                .map(link -> new LinksResponseDTO(link.getLinkId(), link.getLinkUrl(), link.getUsers().getUserId()))
//                .collect(Collectors.toList());
//    }


    public ResponseEntity<Object> CreateLink(Long userId, LinksCreateRequest linksCreateRequest){
        Optional<Users> optionalUsers = usersRepository.findById(userId);
        if (optionalUsers.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(404, "사용자를 찾을 수 없음"));
        }

        //폴더가 있는지 체크
        Optional<Folders> optionalFolders = foldersRepository.findById(linksCreateRequest.getFoldersId());
        if (optionalFolders.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(404, "폴더를 찾을 수 없음"));
        }

        //기본 폴더가 있는지 체크
        Optional<Folders> optionalDefaultFolders = foldersRepository.findById(linksCreateRequest.getDefaultFoldersId());
        if (optionalDefaultFolders.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(404, "기본 폴더를 찾을 수 없음"));
        }

        // 링크 저장
        String linkUrl = linksCreateRequest.getLinkUrl();
        String linkName = linksCreateRequest.getLinkName();
        Links link = new Links(linkUrl,linkName);
        linksRepository.save(link);

        //폴더 링크에 삽입 -> folders_links에 넣어야 됨
        FoldersLinks foldersLinks = new FoldersLinks(optionalFolders.get(), link, userId);
        foldersLinksRepository.save(foldersLinks);

        //모든 링크 폴더에도 추가를 해주어야함
        //만약 foldersId랑 deafultFoldersId랑 같으면 삽입이 필요없고 다르면 넣어야됨
        if (linksCreateRequest.getFoldersId() != linksCreateRequest.getDefaultFoldersId()){
            FoldersLinks foldersDefaultLinks = new FoldersLinks(optionalDefaultFolders.get(), link, userId);
            foldersLinksRepository.save(foldersDefaultLinks);
        }
        return ResponseEntity.ok(linksCreateRequest);
    }

//    public void deleteLink(LinksDeleteDTO linksDeleteDTO){
//        List<Links> userLinks = linksRepository.findByUsers_UserId(linksDeleteDTO.getUserId());
//        if (userLinks == null || userLinks.isEmpty())
//        {
//            throw new IllegalArgumentException("해당 사용자에 대한 링크가 없습니다.");
//        }
//
//
//        List<Links> linksToDelete = userLinks.stream() //userLinks는 Link 객체들의 리스트이다. 여기서 .stream() 메서드는 리스트를 스트림(Stream)으로 변환한다. 스트림을 사용하면 컬렉션에 대해 여러 가지 처리를 더 간결하고 효율적으로 할 수 있다
//                .filter(links -> linksDeleteDTO.getLinkId().contains(links.getLinkId()))//
//                .collect(Collectors.toList());
//
//        if (linksToDelete.isEmpty()){
//            throw new IllegalArgumentException("삭제할 링크 ID 값이 존재하지 않습니다.");
//        }
//
//        linksRepository.deleteAll(linksToDelete);
//    }


}
