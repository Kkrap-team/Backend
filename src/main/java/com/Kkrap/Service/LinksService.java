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
import com.Kkrap.RequestDTO.LinksDeleteRequest;
import com.Kkrap.ResponseDto.MessageResponse;
import com.Kkrap.Util.LinkMetadataExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


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

        //1. 링크 URL 추출
        String linkUrl = linksCreateRequest.getLinkUrl();

        //2. Jsoup 기반 메타 정보 추출
        LinkMetadataExtractor.Metadata meta = LinkMetadataExtractor.extract(linkUrl);

        String linkName = (meta.title != null && !meta.title.isBlank()) ? meta.title : null;
        String thumbnailUrl = (meta.thumbnailUrl != null && !meta.thumbnailUrl.isBlank()) ? meta.thumbnailUrl : null;
        String faviconUrl = (meta.faviconUrl != null && !meta.faviconUrl.isBlank()) ? meta.faviconUrl : null;

        //3. 링크 저장
        Links link = new Links();
        link.setLinkUrl(linkUrl);
        link.setLinkName(linkName);
        link.setThumbnailUrl(thumbnailUrl);
        link.setFaviconUrl(faviconUrl);
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

    public ResponseEntity<Object> DeleteLink(Long userId, LinksDeleteRequest linksDeleteRequest) {
        // 1. 사용자 확인
        Optional<Users> optionalUsers = usersRepository.findById(userId);
        if (optionalUsers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(404, "사용자를 찾을 수 없음"));
        }

        // 2. 없는 link일 경우
        List<Long> deleteLinkIdList = linksDeleteRequest.getLinkId();
        for (Long linkId : deleteLinkIdList){
            Optional<Links> link = linksRepository.findById(linkId);
            if (link.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new MessageResponse(404, "링크를 찾을 수 없음"));
            }
        }


        Long defaultFodersId = linksDeleteRequest.getDefaultFoldersId();
        Long foldersId = linksDeleteRequest.getFoldersId();

        if (defaultFodersId == foldersId) //defaultFoldersId가 foldersId랑 같으면 다른 폴더에 있는 것들도 전부 삭제해야함.
        {
            //사용자가 가진 folderId다 들고 오기
            List<Folders> folders = foldersRepository.findByUserUserId(userId);

            for (Folders folder : folders){
                List<FoldersLinks> foldersLinksList = foldersLinksRepository.findByFolders(folder);

                //삭제 대상만 필터링
                List<FoldersLinks> toDelete = foldersLinksList.stream()
                        .filter(fl -> deleteLinkIdList.contains(fl.getLinks().getLinkId()))
                        .collect(Collectors.toList());
                foldersLinksRepository.deleteAll(toDelete);
            }

            //링크들 삭제
            for (Long linkId : deleteLinkIdList){
                linksRepository.deleteById(linkId);
            }

            return ResponseEntity.ok(new LinksDeleteRequest(defaultFodersId, foldersId, deleteLinkIdList));
        }
        else // 다를 경우 foldersId에 있는
        {
            Optional<Folders> folders = foldersRepository.findById(foldersId);
            Folders folder = folders.get();

            // 해당 폴더에 연결된 folders_links 모두 조회
            List<FoldersLinks> folderLinksList = foldersLinksRepository.findByFolders(folder);


            for (FoldersLinks fl : folderLinksList){
                Long linkId = fl.getLinks().getLinkId();
                if (deleteLinkIdList.contains(linkId)) {
                    foldersLinksRepository.delete(fl); // folders_links 삭제
                }
            }
            return ResponseEntity.ok(new LinksDeleteRequest(defaultFodersId, foldersId, deleteLinkIdList));
        }

    }


}
