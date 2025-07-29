package com.Kkrap.Service.FolderLink;

import com.Kkrap.Entity.*;
import com.Kkrap.Kafka.FolderCreateProducer;
import com.Kkrap.RequestDTO.FoldersCreateRequest;
import com.Kkrap.RequestDTO.FoldersDeleteRequest;
import com.Kkrap.RequestDTO.FoldersScrapRequest;
import com.Kkrap.RequestDTO.FoldersUpdateRequest;
import com.Kkrap.ResponseDTO.FoldersLinksAllResponse;
import com.Kkrap.ResponseDTO.FoldersResponse;
import com.Kkrap.ResponseDTO.UserFoldersWithSharedResponse;
import com.Kkrap.Service.FeedRedisService;
import com.Kkrap.Service.FoldersDocument.FoldersDocumentService;
import com.Kkrap.Service.FollowsFoldersPermission.FoldersPermissionsService;
import com.Kkrap.Service.Users.UsersService;

import io.swagger.v3.oas.models.links.Link;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class FoldersManagerService {
    private final FoldersService foldersService;

    private final UsersService usersService;

    private final FoldersLinksService foldersLinksService;

    private final LinksService linksService;

    private final StringRedisTemplate redisTemplate;

    private final FolderCreateProducer folderCreateProducer;

    private final FoldersDocumentService foldersDocumentService;

    private final FoldersPermissionsService foldersPermissionsService;

    private final FeedRedisService feedRedisService;

    public FoldersManagerService(FoldersService foldersService, UsersService usersService, FoldersLinksService foldersLinksService,
                                 LinksService linksService, StringRedisTemplate redisTemplate,
                                 FolderCreateProducer folderCreateProducer,
                                 FoldersDocumentService foldersDocumentService,
                                 FoldersPermissionsService foldersPermissionsService,
                                 FeedRedisService feedRedisService
                                 ) {
        this.foldersService = foldersService;
        this.usersService = usersService;
        this.foldersLinksService = foldersLinksService;
        this.linksService = linksService;
        this.redisTemplate = redisTemplate;
        this.folderCreateProducer = folderCreateProducer;
        this.foldersDocumentService = foldersDocumentService;
        this.foldersPermissionsService = foldersPermissionsService;
        this.feedRedisService = feedRedisService;
    }


    @Transactional(readOnly = true)
    public ResponseEntity<UserFoldersWithSharedResponse> getUserAllFoldersWithLinks(Long userId){
        usersService.findById(userId);
        List<Folders> allMyFolders = foldersService.findByUserUserId(userId);

        // shared 컬럼으로 분리
        // defaultFolder == true인 폴더 (딱 하나라고 가정)
        List<Folders> defaultFolderList = allMyFolders.stream()
                .filter(folder -> !folder.isShared() && folder.isDefaultFolder())
                .toList();

        // 나머지 공유되지 않은 폴더 중 defaultFolder == false 인 것들
        List<Folders> otherOwnFolders = allMyFolders.stream()
                .filter(folder -> !folder.isShared() && !folder.isDefaultFolder())
                .sorted(Comparator.comparing(Folders::getCreateTime).reversed())
                .toList();

        // shared 컬럼으로 분리
        List<Folders> ownFolders = Stream.concat(defaultFolderList.stream(), otherOwnFolders.stream())
                .toList();

        List<Folders> mySharedFolders = allMyFolders.stream()
                .filter(Folders::isShared)
                .sorted(Comparator.comparing(Folders::getCreateTime).reversed())
                .collect(Collectors.toList());

        // 공유받은 폴더 (권한 테이블 기준)
        List<FoldersPermissions> sharedPermissions = foldersPermissionsService.findByInvitedUserId(userId);
        List<Folders> invitedSharedFolders = sharedPermissions.stream()
                .map(permission -> foldersService.findById(permission.getFolder().getFolderId()))
                .collect(Collectors.toList());

        // 공유 폴더 합치기
        List<Folders> allSharedFolders = Stream.concat(
                mySharedFolders.stream(),  // 내가 만든 공유 폴더
                invitedSharedFolders.stream()  // 내가 초대받은 공유 폴더
        ).toList();

        //변환
        List<FoldersLinksAllResponse> ownFolderResponses = ownFolders.stream()
                .map(folder -> {
                    List<FoldersLinks> folderLinksList = foldersLinksService.findByFolders(folder);
                    List<Links> linksList = linksService.selectLinksByCreateTime(folderLinksList);
                    return FoldersLinksAllResponse.of(folder, linksList);
                })
                .collect(Collectors.toList());

        List<FoldersLinksAllResponse> sharedFolderResponses = allSharedFolders.stream()
                .map(folder -> {
                    List<FoldersLinks> folderLinksList = foldersLinksService.findByFolders(folder);
                    List<Links> linksList = linksService.selectLinksByCreateTime(folderLinksList);
                    return FoldersLinksAllResponse.of(folder, linksList);
                })
                .collect(Collectors.toList());

        UserFoldersWithSharedResponse response = UserFoldersWithSharedResponse.of(ownFolderResponses, sharedFolderResponses);
        return ResponseEntity.ok(response);
    }

    //상대방 모든 거 조회할 때
    @Transactional(readOnly = true)
    public ResponseEntity<UserFoldersWithSharedResponse> getAllFoldersWithTop4LinksByUser(Long userId) {
        usersService.findById(userId);
        List<Folders> allMyFolders = foldersService.findByUserUserId(userId);

        // visible = true 필터
        // defaultFolder == true인 폴더 (딱 하나라고 가정)
        List<Folders> defaultFolderList = allMyFolders.stream()
                .filter(folder -> !folder.isShared() && folder.isDefaultFolder())
                .toList();

        // 나머지 공유되지 않은 폴더 중 defaultFolder == false 인 것들
        List<Folders> otherOwnFolders = allMyFolders.stream()
                .filter(folder -> !folder.isShared() && !folder.isDefaultFolder())
                .sorted(Comparator.comparing(Folders::getCreateTime).reversed())
                .toList();

        // shared 컬럼으로 분리
        List<Folders> ownFolders = Stream.concat(defaultFolderList.stream(), otherOwnFolders.stream())
                .toList();

        List<Folders> mySharedFolders = allMyFolders.stream()
                .filter(folder -> folder.isShared() && folder.isVisible())
                .sorted(Comparator.comparing(Folders::getCreateTime).reversed())
                .collect(Collectors.toList());

        // 초대받은 공유 폴더
        List<FoldersPermissions> sharedPermissions = foldersPermissionsService.findByInvitedUserId(userId);
        List<Folders> invitedSharedFolders = sharedPermissions.stream()
                .map(permission -> foldersService.findById(permission.getFolder().getFolderId()))
                .filter(Folders::isVisible)  // 초대받은 것도 visible만
                .collect(Collectors.toList());

        // 모든 공유 폴더
        List<Folders> allSharedFolders = Stream.concat(
                mySharedFolders.stream(),
                invitedSharedFolders.stream()
        ).toList();

        List<FoldersLinksAllResponse> ownFolderResponses = ownFolders.stream()
                .map(folder -> {
                    List<FoldersLinks> folderLinksList = foldersLinksService.findByFolders(folder);
                    List<Links> linksList = linksService.selectTop4LinksByCreateTime(folderLinksList);
                    return FoldersLinksAllResponse.of(folder, linksList);
                })
                .collect(Collectors.toList());

        List<FoldersLinksAllResponse> sharedFolderResponses = allSharedFolders.stream()
                .map(folder -> {
                    List<FoldersLinks> folderLinksList = foldersLinksService.findByFolders(folder);
                    List<Links> linksList = linksService.selectTop4LinksByCreateTime(folderLinksList);
                    return FoldersLinksAllResponse.of(folder, linksList);
                })
                .collect(Collectors.toList());

        UserFoldersWithSharedResponse response = UserFoldersWithSharedResponse.of(ownFolderResponses, sharedFolderResponses);
        return ResponseEntity.ok(response);
    }


    public ResponseEntity<UserFoldersWithSharedResponse> getMeAllFoldersWithTop4Links(Long userId){
        usersService.findById(userId);
        List<Folders> allMyFolders = foldersService.findByUserUserId(userId);

        // defaultFolder == true인 폴더 (딱 하나라고 가정)
        List<Folders> defaultFolderList = allMyFolders.stream()
                .filter(folder -> !folder.isShared() && folder.isDefaultFolder())
                .toList();

        // 나머지 공유되지 않은 폴더 중 defaultFolder == false 인 것들
        List<Folders> otherOwnFolders = allMyFolders.stream()
                .filter(folder -> !folder.isShared() && !folder.isDefaultFolder())
                .sorted(Comparator.comparing(Folders::getCreateTime).reversed())
                .toList();

        // shared 컬럼으로 분리
        List<Folders> ownFolders = Stream.concat(defaultFolderList.stream(), otherOwnFolders.stream())
                .toList();

        List<Folders> mySharedFolders = allMyFolders.stream()
                .filter(Folders::isShared)
                .sorted(Comparator.comparing(Folders::getCreateTime).reversed())
                .collect(Collectors.toList());

        // 공유받은 폴더 (권한 테이블 기준)
        List<FoldersPermissions> sharedPermissions = foldersPermissionsService.findByInvitedUserId(userId);
        List<Folders> invitedSharedFolders = sharedPermissions.stream()
                .map(permission -> foldersService.findById(permission.getFolder().getFolderId()))
                .collect(Collectors.toList());

        // 공유 폴더 합치기
        List<Folders> allSharedFolders = Stream.concat(
                mySharedFolders.stream(),  // 내가 만든 공유 폴더
                invitedSharedFolders.stream()  // 내가 초대받은 공유 폴더
        ).toList();

        //변환
        List<FoldersLinksAllResponse> ownFolderResponses = ownFolders.stream()
                .map(folder -> {
                    List<FoldersLinks> folderLinksList = foldersLinksService.findByFolders(folder);
                    List<Links> linksList = linksService.selectTop4LinksByCreateTime(folderLinksList);
                    return FoldersLinksAllResponse.of(folder, linksList);
                })
                .collect(Collectors.toList());

        List<FoldersLinksAllResponse> sharedFolderResponses = allSharedFolders.stream()
                .map(folder -> {
                    List<FoldersLinks> folderLinksList = foldersLinksService.findByFolders(folder);
                    List<Links> linksList = linksService.selectTop4LinksByCreateTime(folderLinksList);
                    return FoldersLinksAllResponse.of(folder, linksList);
                })
                .collect(Collectors.toList());

        UserFoldersWithSharedResponse response = UserFoldersWithSharedResponse.of(ownFolderResponses, sharedFolderResponses);
        return ResponseEntity.ok(response);
    }

    //조회수 -> Redis -> Kafka
    @Transactional(readOnly = true)
    public ResponseEntity<FoldersLinksAllResponse> getOneFolderWithLinksByUser(Long userId, Long folderId) {
        Folders folder = foldersService.findById(folderId);
        foldersService.isVisibleBy(folder);

        String redisKey = "view:" + userId + ":" + folderId;
        redisTemplate.opsForValue().set(redisKey, String.valueOf(folderId), Duration.ofMinutes(5));

        List<FoldersLinks> folderLinksList = foldersLinksService.findByFolders(folder);
        List<Links> linksList = linksService.selectLinksByCreateTime(folderLinksList);

        return ResponseEntity.ok(FoldersLinksAllResponse.of(folder, linksList));
    }


    public ResponseEntity<FoldersLinksAllResponse> getMyOneFolderWithLinks(Long folderId){
        //폴더가 있는지 검사
        Folders folder = foldersService.findById(folderId);
        //해당 폴더의 FoldersLinks 조회
        List<FoldersLinks> folderLinksList = foldersLinksService.findByFolders(folder);
        List<Links> linksList = linksService.selectLinksByCreateTime(folderLinksList);
        // Folder + Links 리스트를 Response DTO로 변환
        return ResponseEntity.ok(FoldersLinksAllResponse.of(folder, linksList));
    }


    //Create - 폴더를 만들기
    public ResponseEntity<FoldersResponse> createUserFolder(Long userId, FoldersCreateRequest foldersCreateRequest)
    {
        Users users = usersService.findById(userId);
        Folders folders = foldersService.save(foldersCreateRequest, users);
        if (Boolean.TRUE.equals(folders.isVisible())) {
            feedRedisService.pushFeedToRedis(users, folders);

            String eventPayload = String.format(
                    "{\"folderId\": %d}",
                    folders.getFolderId()
            );
            folderCreateProducer.sendFolderCreatedEvent(eventPayload);
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
        List<FoldersLinks> folderLinksList = foldersLinksService.findByFolders(folders);

        // FoldersLinks 테이블에서 해당 folderId를 가진 데이터 삭제
        foldersLinksService.deleteAll(folderLinksList);

        // folders 삭제
        foldersService.deleteById(folderId);

        //색인 업데이트
        foldersDocumentService.deleteFolderDocument(folders);

        //응답 데이터를 삭제된 링크들까지 포함 시켜서 해주어야함 - 정환행님한테 물어보기
        return ResponseEntity.ok(FoldersResponse.from(folders, userId));
    }

    @Transactional
    public ResponseEntity<FoldersResponse> updateFolderMetadata(FoldersUpdateRequest request){
        Users users = usersService.findById(request.getUserId());
        Folders folders = foldersService.findById(request.getFolderId());

        foldersService.isOwnedByService(folders, users.getUserId());
        folders.setFolderName(request.getFolderName());
        folders.setFolderDescription(request.getFolderDescription());
        folders.setVisible(request.isVisible());
        foldersService.save(folders);

        if (folders.isVisible()) {
            Optional<Links> link = foldersLinksService.getFirstLinkByFolder(folders);
            foldersDocumentService.indexNewFolder(folders, link.get());
        } else {
            foldersDocumentService.deleteFolderDocument(folders);
        }

        return ResponseEntity.ok(FoldersResponse.from(folders, users.getUserId()));
    }

    @Transactional
    public ResponseEntity<FoldersLinksAllResponse> scrapFolder(Long userId, FoldersScrapRequest foldersScrapRequest) {
        Users user = usersService.findById(userId);
        Folders newFolder = foldersService.save(Folders.of(foldersScrapRequest, user));

        Folders sourceFolder = foldersService.findById(foldersScrapRequest.getSourceFolderId());

        //Redis
        String redisKey = "scrap:" + sourceFolder.getFolderId();
        redisTemplate.opsForValue().increment(redisKey);

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
                FoldersLinksAllResponse.of(newFolder, newLinks)
        );
    }

}
