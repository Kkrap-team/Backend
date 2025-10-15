package com.kkrap.Service.FolderLink;

import com.kkrap.Entity.Folders;
import com.kkrap.Entity.FoldersLinks;
import com.kkrap.Entity.Links;
import com.kkrap.RequestDTO.LinksCreateRequest;
import com.kkrap.RequestDTO.LinksDeleteRequest;
import com.kkrap.RequestDTO.LinksTitleUpdateRequest;
import com.kkrap.RequestDTO.MoveLinkToAnotherFolders;
import com.kkrap.ResponseDTO.LinksCreateResponse;
import com.kkrap.ResponseDTO.LinksResponse;
import com.kkrap.Service.Users.UsersService;
import com.kkrap.Util.LinkMetadataExtractor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public ResponseEntity<LinksCreateResponse> createLinkAndAssignToFolders(Long userId, LinksCreateRequest linksCreateRequest){
        usersService.findById(userId);
        Folders folders = foldersService.findById(linksCreateRequest.getFoldersId());
        String linkUrl = linksCreateRequest.getLinkUrl(); // 1. 링크 URL 추출
        Links links = LinkMetadataExtractor.extractAndBuildLink(linkUrl); // 2. 메타 정보 추출 + 링크 객체 생성까지 유틸에서 처리
        links = linksService.save(links); // 3. 링크 저장
        foldersLinksService.save(FoldersLinks.of(folders, links, userId)); //폴더 링크에 삽입 -> folders_links에 넣어야 됨


        return ResponseEntity.ok(LinksCreateResponse.of(links, linksCreateRequest.getFoldersId()));
    }


    @Transactional
    public ResponseEntity<LinksDeleteRequest> deleteLinksWithFolderMapping(Long userId, LinksDeleteRequest linksDeleteRequest) {
        usersService.findById(userId);
        List<Long> deleteLinkIdList = linksDeleteRequest.getLinkId();
        linksService.validateAllExistByIds(deleteLinkIdList);

        Long foldersId = linksDeleteRequest.getFoldersId();

        Folders folder = foldersService.findById(foldersId);
        List<FoldersLinks> folderLinksList = foldersLinksService.findByFolders(folder);
        for (FoldersLinks fl : folderLinksList){
            Links links = fl.getLinks();
            Long linkId = links.getLinkId();
            if (deleteLinkIdList.contains(linkId)) {
                foldersLinksService.delete(fl);// folders_links 삭제
            }
        }

        return ResponseEntity.ok(LinksDeleteRequest.of(foldersId, deleteLinkIdList));
    }


    public ResponseEntity<LinksResponse> updateLinkTitle(Long userId, LinksTitleUpdateRequest linksTitleUpdateRequest) {
        usersService.findById(userId);
        Links link = linksService.findById(linksTitleUpdateRequest.getLinkId());
        link.setLinkName(linksTitleUpdateRequest.getLinkName());
        linksService.save(link);
        return ResponseEntity.ok(LinksResponse.of(link));
    }

    @Transactional
    public ResponseEntity<LinksResponse> moveLinkToAnotherFolders(Long userId, MoveLinkToAnotherFolders request) {

        usersService.findById(userId);
        Links link = linksService.findById(request.getLinkId());
        Folders source = foldersService.findById(request.getSourceFolderId());
        Folders target = foldersService.findById(request.getTargetFolderId());

        if (source.getFolderId() == target.getFolderId()) {
            foldersService.foldersSameMove();
        }

        foldersService.isOwnedByService(source, userId);
        foldersService.isOwnedByService(target, userId);

        FoldersLinks sourceRow = foldersLinksService
                .findByUserIdAndFoldersFolderIdAndLinksLinkId(userId, source.getFolderId(), link.getLinkId());

        boolean targetExists = foldersLinksService
                .existsByUserIdAndFoldersFolderIdAndLinksLinkId(userId, target.getFolderId(), link.getLinkId());


        if (targetExists) {
            foldersLinksService.deleteByUserIdAndFoldersFolderIdAndLinksLinkId(userId, source.getFolderId(), link.getLinkId());
        } else {
            sourceRow.setFolders(target);
        }

        return ResponseEntity.ok(LinksResponse.of(link));
    }


}