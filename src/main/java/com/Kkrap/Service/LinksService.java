package com.Kkrap.Service;

import com.Kkrap.Entity.Folders;
import com.Kkrap.Entity.FoldersLinks;
import com.Kkrap.Entity.Links;
import com.Kkrap.Entity.Users;
import com.Kkrap.Exception.ErrorCode;
import com.Kkrap.Exception.LinksNotFoundException;
import com.Kkrap.Repository.LinksRepository;
import com.Kkrap.RequestDTO.LinksCreateRequest;
import com.Kkrap.RequestDTO.LinksDeleteRequest;
import com.Kkrap.Util.LinkMetadataExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class LinksService {

    @Autowired
    private UsersService usersService;

    @Autowired
    private FoldersService foldersService;

    @Autowired
    private FoldersLinksService foldersLinksService;
    @Autowired
    private LinksRepository linksRepository;



    public ResponseEntity<LinksCreateRequest> createLink(Long userId, LinksCreateRequest linksCreateRequest){
        Users users = usersService.findById(userId);
        //폴더가 있는지 체크
        Folders folders = foldersService.findById(linksCreateRequest.getFoldersId());
        //기본 폴더가 있는지 체크
        Folders defaultfolders = foldersService.findById(linksCreateRequest.getDefaultFoldersId());

        //1. 링크 URL 추출
        String linkUrl = linksCreateRequest.getLinkUrl();

        //2. Jsoup 기반 메타 정보 추출
        LinkMetadataExtractor.Metadata meta = LinkMetadataExtractor.extract(linkUrl);
        String linkName = (meta.title != null && !meta.title.isBlank()) ? meta.title : null;
        String thumbnailUrl = (meta.thumbnailUrl != null && !meta.thumbnailUrl.isBlank()) ? meta.thumbnailUrl : null;
        String faviconUrl = (meta.faviconUrl != null && !meta.faviconUrl.isBlank()) ? meta.faviconUrl : null;

        //3. 링크 저장
        Links links = save(Links.of(linkUrl, linkName, thumbnailUrl, faviconUrl));

        //폴더 링크에 삽입 -> folders_links에 넣어야 됨
        foldersLinksService.save(FoldersLinks.of(folders, links, userId));

        //모든 링크 폴더에도 추가를 해주어야함
        //만약 foldersId랑 deafultFoldersId랑 같으면 삽입이 필요없고 다르면 넣어야됨
        if (linksCreateRequest.getFoldersId() != linksCreateRequest.getDefaultFoldersId()){
            foldersLinksService.save(FoldersLinks.of(defaultfolders, links, userId));
        }
        return ResponseEntity.ok(linksCreateRequest);
    }

    public ResponseEntity<LinksDeleteRequest> DeleteLink(Long userId, LinksDeleteRequest linksDeleteRequest) {
        // 1. 사용자 확인
        usersService.findById(userId);

        // 2. 없는 link일 경우
        List<Long> deleteLinkIdList = linksDeleteRequest.getLinkId();
        for (Long linkId : deleteLinkIdList){ findById(linkId); }

        Long defaultFodersId = linksDeleteRequest.getDefaultFoldersId();
        Long foldersId = linksDeleteRequest.getFoldersId();

        if (defaultFodersId == foldersId) //defaultFoldersId가 foldersId랑 같으면 다른 폴더에 있는 것들도 전부 삭제해야함.
        {
            //사용자가 가진 folderId다 들고 오기
            List<Folders> folders = foldersService.findByUserUserId(userId);
            for (Folders folder : folders){
                List<FoldersLinks> foldersLinksList= foldersLinksService.findByFolders(folder);
                //삭제 대상만 필터링
                List<FoldersLinks> toDelete = foldersLinksList.stream()
                        .filter(fl -> deleteLinkIdList.contains(fl.getLinks().getLinkId()))
                        .collect(Collectors.toList());
                foldersLinksService.deleteAll(toDelete);
            }

            //링크들 삭제
            for (Long linkId : deleteLinkIdList){ deleteById(linkId); }
            return ResponseEntity.ok(LinksDeleteRequest.of(defaultFodersId, foldersId, deleteLinkIdList));
        }
        else // 다를 경우 foldersId에 있는
        {
            Folders folder = foldersService.findById(foldersId);

            // 해당 폴더에 연결된 folders_links 모두 조회
            List<FoldersLinks> folderLinksList = foldersLinksService.findByFolders(folder);


            for (FoldersLinks fl : folderLinksList){
                Links links = fl.getLinks();
                Long linkId = links.getLinkId();
                if (deleteLinkIdList.contains(linkId)) {
                    foldersLinksService.delete(fl);// folders_links 삭제
                }
            }
            return ResponseEntity.ok(LinksDeleteRequest.of(defaultFodersId, foldersId, deleteLinkIdList));
        }
    }

    public Links save(Links links){
        return linksRepository.save(links);
    }
    public Links findById(Long linkId){
        return linksRepository.findById(linkId).orElseThrow(() -> LinksNotFoundException.of("링크를 찾을 수 없습니다", ErrorCode.LINKS_NOT_FOUND));
    }
    public void deleteById(Long linkId){
        linksRepository.deleteById(linkId);
    }


}
