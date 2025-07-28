package com.Kkrap.Service.FolderLink;

import com.Kkrap.Entity.Folders;
import com.Kkrap.Entity.FoldersLinks;
import com.Kkrap.Entity.Links;
import com.Kkrap.RequestDTO.LinksCreateRequest;
import com.Kkrap.RequestDTO.LinksDeleteRequest;
import com.Kkrap.RequestDTO.LinksTitleUpdateRequest;
import com.Kkrap.ResponseDTO.LinksCreateResponse;
import com.Kkrap.ResponseDTO.LinksResponse;
import com.Kkrap.Service.Users.UsersService;
import com.Kkrap.Util.LinkMetadataExtractor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LinksManagerService {

    private final FoldersService foldersService;
    private final UsersService usersService;
    private final LinksService linksService;

    private final FoldersLinksService foldersLinksService;


    public LinksManagerService(UsersService usersService, FoldersService foldersService,
                               LinksService linksService, FoldersLinksService foldersLinksService){
        this.usersService = usersService;
        this.foldersService = foldersService;
        this.linksService = linksService;
        this.foldersLinksService = foldersLinksService;
    }

    public ResponseEntity<LinksCreateResponse> createLinkAndAssignToFolders(Long userId, LinksCreateRequest linksCreateRequest){
        usersService.findById(userId);
        Folders folders = foldersService.findById(linksCreateRequest.getFoldersId());
        Folders defaultfolders = foldersService.findById(linksCreateRequest.getDefaultFoldersId());
        String linkUrl = linksCreateRequest.getLinkUrl(); // 1. 링크 URL 추출
        Links links = LinkMetadataExtractor.extractAndBuildLink(linkUrl); // 2. 메타 정보 추출 + 링크 객체 생성까지 유틸에서 처리
        links = linksService.save(links); // 3. 링크 저장
        foldersLinksService.save(FoldersLinks.of(folders, links, userId)); //폴더 링크에 삽입 -> folders_links에 넣어야 됨

        //모든 링크 폴더에도 추가를 해주어야함
        //만약 foldersId랑 deafultFoldersId랑 같으면 삽입이 필요없고 다르면 넣어야됨
        if (linksCreateRequest.getFoldersId() != linksCreateRequest.getDefaultFoldersId()){
            foldersLinksService.save(FoldersLinks.of(defaultfolders, links, userId));
        }
        return ResponseEntity.ok(LinksCreateResponse.of(links, linksCreateRequest.getDefaultFoldersId(), linksCreateRequest.getFoldersId()));
    }


    public ResponseEntity<LinksDeleteRequest> deleteLinksWithFolderMapping(Long userId, LinksDeleteRequest linksDeleteRequest) {
        usersService.findById(userId);
        List<Long> deleteLinkIdList = linksDeleteRequest.getLinkId();
        linksService.validateAllExistByIds(deleteLinkIdList);

        Long defaultFodersId = linksDeleteRequest.getDefaultFoldersId();
        Long foldersId = linksDeleteRequest.getFoldersId();
        if (defaultFodersId == foldersId) //defaultFoldersId가 foldersId랑 같으면 다른 폴더에 있는 것들도 전부 삭제해야함.
        {
            List<Folders> folders = foldersService.findByUserUserId(userId); //사용자가 가진 folderId다 들고 오기
            for (Folders folder : folders){
                List<FoldersLinks> foldersLinksList= foldersLinksService.findByFolders(folder);
                List<FoldersLinks> toDelete = linksService.filterFoldersLinksByLinkIds(foldersLinksList, deleteLinkIdList);
                foldersLinksService.deleteAll(toDelete);
            }
            linksService.deleteAllByIds(deleteLinkIdList);
            return ResponseEntity.ok(LinksDeleteRequest.of(defaultFodersId, foldersId, deleteLinkIdList));
        }
        else // 다를 경우 foldersId에 있는
        {
            Folders folder = foldersService.findById(foldersId);
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

    public ResponseEntity<LinksResponse> updateLinkTitle(Long userId, LinksTitleUpdateRequest linksTitleUpdateRequest) {
        usersService.findById(userId);
        Links link = linksService.findById(linksTitleUpdateRequest.getLinkId());
        link.setLinkName(linksTitleUpdateRequest.getLinkName());
        linksService.save(link);
        return ResponseEntity.ok(LinksResponse.of(link));
    }

}