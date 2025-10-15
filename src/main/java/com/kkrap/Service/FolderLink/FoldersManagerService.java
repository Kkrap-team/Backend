package com.kkrap.Service.FolderLink;

import com.kkrap.Data.KeysetCursor;
import com.kkrap.Entity.*;
import com.kkrap.RequestDTO.*;
import com.kkrap.ResponseDTO.*;
import com.kkrap.Service.ActivityFeed.ActivityFeedService;
import com.kkrap.Service.FollowsFoldersPermission.FoldersPermissionsService;
import com.kkrap.Service.Users.UsersService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class FoldersManagerService {
    private final FoldersService foldersService;

    private final UsersService usersService;

    private final FoldersLinksService foldersLinksService;

    private final LinksService linksService;

    private final FoldersPermissionsService foldersPermissionsService;


    private final ActivityFeedService activityFeedService;


    public FoldersManagerService(FoldersService foldersService, UsersService usersService, FoldersLinksService foldersLinksService,
                                 LinksService linksService,
                                 FoldersPermissionsService foldersPermissionsService,
                                 ActivityFeedService activityFeedService
                                 ) {
        this.foldersService = foldersService;
        this.usersService = usersService;
        this.foldersLinksService = foldersLinksService;
        this.linksService = linksService;
        this.foldersPermissionsService = foldersPermissionsService;
        this.activityFeedService = activityFeedService;
    }



    List<Folders> conncatFolders(List<Folders> defaultFolderList, List<Folders> otherOwnFolders){
        return  Stream.concat(defaultFolderList.stream(), otherOwnFolders.stream())
                .toList();
    }


    List<OneFoldersAllLinksResponse> AllOwnerFolderslinksSelectTop1linksByCreateTime(List<Folders> folders){
        return folders.stream()
                .map(folder -> {
                    List<FoldersLinks> folderLinksList = foldersLinksService.findByFolders(folder);
                    List<Links> linksList = linksService.selectTop1LinksByCreateTime(folderLinksList);
                    return OneFoldersAllLinksResponse.of(folder, linksList);
                })
                .collect(Collectors.toList());
    }


    List<SharedFoldersLinksAllResponse> AllSharedFolderslinksSelectTop4linksByCreateTime(List<Folders> folders){
        return folders.stream()
                .map(folder -> {
                    List<FoldersLinks> folderLinksList = foldersLinksService.findByFolders(folder);
                    List<Links> linksList = linksService.selectTop4LinksByCreateTime(folderLinksList);
                    return SharedFoldersLinksAllResponse.of(folder, linksList);
                })
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public ResponseEntity<UserFoldersWithSharedResponse> getAllFoldersWithLinks(Long userId, Long targetUserId) {
        usersService.findById(userId);
        usersService.findById(targetUserId);
        List<Folders> allMyFolders, ownFolders, mySharedFolders, invitedSharedFolders, allSharedFolders;
        List<FoldersPermissions> sharedPermissions;
        List<SharedFoldersLinksAllResponse> sharedFolderResponses;
        List<OneFoldersAllLinksResponse> ownFolderResponses;
        if (userId == targetUserId){
            allMyFolders = foldersService.findByUserUserId(userId);
            ownFolders = allMyFolders.stream()
                    .filter(folder -> !folder.isShared())
                    .sorted(Comparator.comparing(Folders::getCreateTime).reversed())
                    .toList();
            mySharedFolders = allMyFolders.stream()
                    .filter(Folders::isShared)
                    .sorted(Comparator.comparing(Folders::getCreateTime))
                    .collect(Collectors.toList());
            sharedPermissions = foldersPermissionsService.findByInvitedUserId(userId);
            invitedSharedFolders = sharedPermissions.stream()
                    .map(permission -> foldersService.findById(permission.getFolder().getFolderId()))
                    .collect(Collectors.toList());
            allSharedFolders = conncatFolders(mySharedFolders, invitedSharedFolders);
            ownFolderResponses = AllOwnerFolderslinksSelectTop1linksByCreateTime(ownFolders);
            sharedFolderResponses = AllSharedFolderslinksSelectTop4linksByCreateTime(allSharedFolders);
            return ResponseEntity.ok(UserFoldersWithSharedResponse.of(ownFolderResponses, sharedFolderResponses));

        }
        else {
            allMyFolders = foldersService.findByUserUserId(targetUserId);
            ownFolders = allMyFolders.stream()
                    .filter(folder -> !folder.isShared() && folder.isVisible())
                    .sorted(Comparator.comparing(Folders::getCreateTime).reversed())
                    .toList();
            mySharedFolders = allMyFolders.stream()
                    .filter(folder -> folder.isShared() && folder.isVisible())
                    .sorted(Comparator.comparing(Folders::getCreateTime))
                    .collect(Collectors.toList());
            sharedPermissions = foldersPermissionsService.findByInvitedUserId(targetUserId);
            invitedSharedFolders = sharedPermissions.stream()
                    .map(permission -> foldersService.findById(permission.getFolder().getFolderId()))
                    .filter(Folders::isVisible)  // 초대받은 것도 visible만
                    .collect(Collectors.toList());
            allSharedFolders = conncatFolders(mySharedFolders, invitedSharedFolders);

            ownFolderResponses = AllOwnerFolderslinksSelectTop1linksByCreateTime(ownFolders);
            sharedFolderResponses = AllSharedFolderslinksSelectTop4linksByCreateTime(allSharedFolders);
            return ResponseEntity.ok(UserFoldersWithSharedResponse.of(ownFolderResponses, sharedFolderResponses));
        }

    }



    @Transactional(readOnly = true)
    public ResponseEntity<OneFoldersAllLinksResponse> getOneFolderWithLinks(Long userId, Long folderId, Long targetUserId){
        usersService.findById(userId);
        usersService.findById(targetUserId);
        Folders folder = foldersService.findById(folderId);
        if (userId == targetUserId){
            List<FoldersLinks> folderLinksList = foldersLinksService.findByFolders(folder);
            List<Links> linksList = linksService.selectLinksByCreateTime(folderLinksList);
            return ResponseEntity.ok(OneFoldersAllLinksResponse.of(folder, linksList));
        }
        else {
            foldersPermissionsService.ensureReadable(folder, userId);

            List<FoldersLinks> folderLinksList = foldersLinksService.findByFolders(folder);
            List<Links> linksList = linksService.selectLinksByCreateTime(folderLinksList);

            return ResponseEntity.ok(OneFoldersAllLinksResponse.of(folder, linksList));
        }

    }

    @Transactional(readOnly = true)
    public ResponseEntity<OneFoldersAllLinksResponse> getNoAuthOneFolderWithLinks(Long folderId, Long targetUserId){
        usersService.findById(targetUserId);
        Folders folder = foldersService.findById(folderId);
        foldersPermissionsService.ensureReadable(folder, 0L);

        List<FoldersLinks> folderLinksList = foldersLinksService.findByFolders(folder);
        List<Links> linksList = linksService.selectLinksByCreateTime(folderLinksList);

        return ResponseEntity.ok(OneFoldersAllLinksResponse.of(folder, linksList));

    }


    //Create - 폴더를 만들기
    public ResponseEntity<FoldersResponse> createUserFolder(Long userId, FoldersCreateRequest foldersCreateRequest)
    {
        Users users = usersService.findById(userId);
        Folders folders = foldersService.save(foldersCreateRequest, users);
        if (Boolean.TRUE.equals(folders.isVisible())) {
            activityFeedService.save(ActivityFeed.of(users.getUserId(), folders.getFolderId(), LocalDateTime.now()));
        }
        return ResponseEntity.ok(FoldersResponse.from(folders, users.getUserId()));
    }

    //Delete - 폴더 삭제
    @Transactional
    public ResponseEntity<FoldersResponse> deleteUserFolder(Long userId, FoldersDeleteRequest foldersDeleteRequest)
    {
        //사용자 체크
        usersService.findById(userId);

        //FolderLinks에 해당 folderId에 속한 FolderLinks 리스트 조회


        Long folderId = foldersDeleteRequest.getFolderId();
        Folders folders = foldersService.findById(folderId);

        foldersService.isOwnedByService(folders, userId);


        List<FoldersLinks> folderLinksList = foldersLinksService.findByFolders(folders);

        // FoldersLinks 테이블에서 해당 folderId를 가진 데이터 삭제
        foldersLinksService.deleteAll(folderLinksList);

        // folders 삭제
        foldersService.deleteById(folderId);

        activityFeedService.deleteAllByFolderId(folderId);

        return ResponseEntity.ok(FoldersResponse.from(folders, userId));
    }

    @Transactional
    public ResponseEntity<FoldersResponse> updateFolderMetadata(Long userId, FoldersUpdateRequest request){
        Users users = usersService.findById(userId);
        Folders folders = foldersService.findById(request.getFolderId());

        foldersService.isOwnedByService(folders, users.getUserId());
        folders.setFolderName(request.getFolderName());
        folders.setFolderDescription(request.getFolderDescription());
        folders.setVisible(request.isVisible());
        foldersService.save(folders);

        return ResponseEntity.ok(FoldersResponse.from(folders, users.getUserId()));
    }

    @Transactional
    public ResponseEntity<OneFoldersAllLinksResponse> scrapFolder(Long userId, FoldersScrapRequest foldersScrapRequest) {
        Users user = usersService.findById(userId);

        Folders sourceFolder = foldersService.findById(foldersScrapRequest.getSourceFolderId());

        if (sourceFolder.isOwnedBy(userId)) {
            foldersService.sameScrapfolders();
        }

        Folders newFolder = foldersService.save(Folders.of(foldersScrapRequest, user));

        List<FoldersLinks> sourceFolderLinks = foldersLinksService.findByFolders(sourceFolder);


        List<Links> newLinks = sourceFolderLinks.stream()
                .map(foldersLink -> {
                    Links originalLink = foldersLink.getLinks();
                    return linksService.save(
                            Links.of(
                                    originalLink.getLinkUrl(),
                                    originalLink.getLinkName(),
                                    originalLink.getThumbnailUrl(),
                                    originalLink.getFaviconUrl()
                            )
                    );
                })
                .collect(Collectors.toList());

        newLinks.forEach(link -> {
            try {
                foldersLinksService.save(FoldersLinks.of(newFolder, link, userId));
            } catch (Exception e) {
                log.error("[Scrap] 링크 저장 실패 linkUrl={} reason={}", link.getLinkUrl(), e.getMessage(), e);
            }
        });

        return ResponseEntity.ok(
                OneFoldersAllLinksResponse.of(newFolder, newLinks)
        );
    }

    public ResponseEntity<List<OneFoldersAllLinksResponse>> scrollVisibleFolders(Long userId, Long size, String cursorStr) {
        usersService.findById(userId);

        Long limit = Math.max(1, Math.min(size, 50));
        KeysetCursor c = KeysetCursor.parse(cursorStr);
        LocalDateTime cursorTime = (c == null) ? null : c.getTime();
        Long cursorId = (c == null) ? null : c.getId();

        // 키셋으로 visible=true 내 폴더만
        List<Folders> folders = foldersService.fetchVisibleFoldersPageGlobal(cursorTime, cursorId, (int) (limit + 1));

        boolean hasNext = folders.size() > limit;
        if (hasNext) folders = folders.subList(0, Math.toIntExact(limit));

        // 폴더별 링크 1개 조회
        List<OneFoldersAllLinksResponse> res = folders.stream()
                .map(folder -> {
                    List<FoldersLinks> flist = foldersLinksService.findByFolders(folder);
                    List<Links> links = linksService.selectTop1LinksByCreateTime(flist);
                    return OneFoldersAllLinksResponse.of(folder, links);
                })
                .toList();

        return ResponseEntity.ok(res);
    }


    public ResponseEntity<FoldersRankingResponse> get90dFoldersRankings() {
        // 기준 시각: 현재로부터 90일 전
        LocalDateTime since = LocalDateTime.now().minusDays(90);
        int limit = 10;

        // 폴더 뽑기 (조회수 Top10 / 스크랩수 Top10)
        List<Folders> viewTop = foldersService.findTopByViewCountSince(since, limit);
        List<Folders> scrapTop = foldersService.findTopByScrapCountSince(since, limit);

        // 각 폴더별 링크 하나 추출
        List<OneFoldersAllLinksResponse> topViewDto = viewTop.stream()
                .map(folder -> {
                    List<FoldersLinks> flist = foldersLinksService.findByFolders(folder);
                    List<Links> links = linksService.selectTop1LinksByCreateTime(flist);
                    return OneFoldersAllLinksResponse.of(folder, links);
                })
                .toList();

        List<OneFoldersAllLinksResponse> topScrapDto = scrapTop.stream()
                .map(folder -> {
                    List<FoldersLinks> flist = foldersLinksService.findByFolders(folder);
                    List<Links> links = linksService.selectTop4LinksByCreateTime(flist);
                    return OneFoldersAllLinksResponse.of(folder, links);
                })
                .toList();

        // 응답 조립
        return ResponseEntity.ok(FoldersRankingResponse.of(topViewDto, topScrapDto));
    }





}
