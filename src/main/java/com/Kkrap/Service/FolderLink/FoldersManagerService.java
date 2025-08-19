package com.Kkrap.Service.FolderLink;

import com.Kkrap.ElasticSearch.FoldersDocument;
import com.Kkrap.Entity.*;
import com.Kkrap.Kafka.FolderCreateProducer;
import com.Kkrap.RequestDTO.*;
import com.Kkrap.ResponseDTO.*;
import com.Kkrap.Service.ActivityFeed.ActivityFeedService;
import com.Kkrap.Service.FeedRedisService;
import com.Kkrap.Service.FoldersDocument.FoldersDocumentService;
import com.Kkrap.Service.FollowsFoldersPermission.FoldersPermissionsService;
import com.Kkrap.Service.Users.UsersService;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
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

    private final StringRedisTemplate redisTemplate;

    private final FolderCreateProducer folderCreateProducer;

    private final FoldersDocumentService foldersDocumentService;

    private final FoldersPermissionsService foldersPermissionsService;

    private final FeedRedisService feedRedisService;

    private final ActivityFeedService activityFeedService;


    // 상수
    private static final int PAGE_SIZE = 20;      // 한 번에 내려줄 개수
    private static final int SNAPSHOT_SIZE = 200; // 새 글 동기화 시 ES에서 비교할 상위 개수
    private static final Duration TTL = Duration.ofHours(24);
    private String listKey(Long userId) { return "feed:" + userId + ":list"; }
    private String cursorKey(Long userId) { return "feed:" + userId + ":cursor"; }



    private static final Logger logger = LoggerFactory.getLogger(FoldersManagerService.class);

    public FoldersManagerService(FoldersService foldersService, UsersService usersService, FoldersLinksService foldersLinksService,
                                 LinksService linksService, StringRedisTemplate redisTemplate,
                                 FolderCreateProducer folderCreateProducer,
                                 FoldersDocumentService foldersDocumentService,
                                 FoldersPermissionsService foldersPermissionsService,
                                 FeedRedisService feedRedisService,
                                 ActivityFeedService activityFeedService
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
        this.activityFeedService = activityFeedService;
    }



    List<Folders> conncatFolders(List<Folders> defaultFolderList, List<Folders> otherOwnFolders){
        return  Stream.concat(defaultFolderList.stream(), otherOwnFolders.stream())
                .toList();
    }


    List<FoldersLinksAllResponse> AllOwnerFolderslinksSelectTop1linksByCreateTime(List<Folders> folders){
        return folders.stream()
                .map(folder -> {
                    List<FoldersLinks> folderLinksList = foldersLinksService.findByFolders(folder);
                    List<Links> linksList = linksService.selectTop1LinksByCreateTime(folderLinksList);
                    return FoldersLinksAllResponse.of(folder, linksList);
                })
                .collect(Collectors.toList());
    }

    List<FoldersLinksAllResponse> AllOwnerFolderslinksSelectTop4linksByCreateTime(List<Folders> folders){
        return folders.stream()
                .map(folder -> {
                    List<FoldersLinks> folderLinksList = foldersLinksService.findByFolders(folder);
                    List<Links> linksList = linksService.selectTop4LinksByCreateTime(folderLinksList);
                    return FoldersLinksAllResponse.of(folder, linksList);
                })
                .collect(Collectors.toList());
    }

    List<SharedFoldersLinksAllResponse> AllSharedFolderslinksSelectTop1linksByCreateTime(List<Folders> folders){
        return folders.stream()
                .map(folder -> {
                    List<FoldersLinks> folderLinksList = foldersLinksService.findByFolders(folder);
                    List<Links> linksList = linksService.selectTop1LinksByCreateTime(folderLinksList);
                    return SharedFoldersLinksAllResponse.of(folder, linksList);
                })
                .collect(Collectors.toList());
    }

    List<Folders> notSharedFoldersSelect(List<Folders> allMyFolders){
        return allMyFolders.stream()
                .filter(folder -> !folder.isShared())
                .sorted(Comparator.comparing(Folders::getCreateTime).reversed())
                .toList();
    }

    @Transactional(readOnly = true)
    public ResponseEntity<UserFoldersWithSharedResponse> getAllFoldersWithLinks(Long userId, Long targetUserId) {
        usersService.findById(userId);
        usersService.findById(targetUserId);
        List<Folders> allMyFolders, defaultFolderList, otherOwnFolders, ownFolders, mySharedFolders, invitedSharedFolders, allSharedFolders;
        List<FoldersPermissions> sharedPermissions;
        List<SharedFoldersLinksAllResponse> sharedFolderResponses;
        List<FoldersLinksAllResponse> ownFolderResponses;
        if (userId == targetUserId){
            allMyFolders = foldersService.findByUserUserId(userId);
            log.info("here1");
//            defaultFolderList = allMyFolders.stream()
//                    .filter(folder -> !folder.isShared() && folder.isDefaultFolder())
//                    .toList();
//            otherOwnFolders = notSharedFoldersSelect(allMyFolders);
            ownFolders = allMyFolders.stream()
                    .filter(folder -> !folder.isShared())
                    .sorted(Comparator.comparing(Folders::getCreateTime))
                    .toList();

//            ownFolders = conncatFolders(defaultFolderList, otherOwnFolders);
            mySharedFolders = allMyFolders.stream()
                    .filter(Folders::isShared)
                    .sorted(Comparator.comparing(Folders::getCreateTime))
                    .collect(Collectors.toList());
            sharedPermissions = foldersPermissionsService.findByInvitedUserId(userId);
            invitedSharedFolders = sharedPermissions.stream()
                    .map(permission -> foldersService.findById(permission.getFolder().getFolderId()))
                    .collect(Collectors.toList());
            allSharedFolders = conncatFolders(mySharedFolders, invitedSharedFolders);
            ownFolderResponses = AllOwnerFolderslinksSelectTop4linksByCreateTime(ownFolders);
            sharedFolderResponses = AllSharedFolderslinksSelectTop1linksByCreateTime(allSharedFolders);
            return ResponseEntity.ok(UserFoldersWithSharedResponse.of(ownFolderResponses, sharedFolderResponses));

        }
        else {
            allMyFolders = foldersService.findByUserUserId(targetUserId);
            log.info("here2");
//            defaultFolderList = allMyFolders.stream()
//                    .filter(folder -> !folder.isShared() && !folder.isDefaultFolder() && folder.isVisible() )
//                    .toList();
            ownFolders = allMyFolders.stream()
                    .filter(folder -> !folder.isShared() && folder.isVisible())
                    .sorted(Comparator.comparing(Folders::getCreateTime))
                    .toList();

//            otherOwnFolders = notSharedFoldersSelect(allMyFolders);
//            ownFolders = conncatFolders(defaultFolderList, otherOwnFolders);
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
            sharedFolderResponses = AllSharedFolderslinksSelectTop1linksByCreateTime(allSharedFolders);
            return ResponseEntity.ok(UserFoldersWithSharedResponse.of(ownFolderResponses, sharedFolderResponses));
        }

    }







    //상대방 모든 거 조회할 때
//    @Transactional(readOnly = true)
//    public ResponseEntity<UserFoldersWithSharedResponse> getAllFoldersWithTop1LinksByUser(Long userId, FoldersAllLinksViewRequest request) {
//        usersService.findById(userId);
//        Long targetUserId = request.getTargetUserId();;
//        usersService.findById(targetUserId);
//
//        List<Folders> allMyFolders = foldersService.findByUserUserId(targetUserId);

        // visible = true 필터
        // defaultFolder == true인 폴더 (딱 하나라고 가정)
//        List<Folders> defaultFolderList = allMyFolders.stream()
//                .filter(folder -> !folder.isShared() && folder.isDefaultFolder() && !folder.isDefaultFolder())
//                .toList();

        // 나머지 공유되지 않은 폴더 중 defaultFolder == false 인 것들
//        List<Folders> otherOwnFolders = notSharedFoldersSelect(allMyFolders);

        // shared 컬럼으로 분리
//        List<Folders> ownFolders = conncatFolders(defaultFolderList, otherOwnFolders);

//        List<Folders> mySharedFolders = allMyFolders.stream()
//                .filter(folder -> folder.isShared() && folder.isVisible())
//                .sorted(Comparator.comparing(Folders::getCreateTime).reversed())
//                .collect(Collectors.toList());

        // 초대받은 공유 폴더
//        List<FoldersPermissions> sharedPermissions = foldersPermissionsService.findByInvitedUserId(targetUserId);
//        List<Folders> invitedSharedFolders = sharedPermissions.stream()
//                .map(permission -> foldersService.findById(permission.getFolder().getFolderId()))
//                .filter(Folders::isVisible)  // 초대받은 것도 visible만
//                .collect(Collectors.toList());

        // 모든 공유 폴더
//        List<Folders> allSharedFolders = conncatFolders(mySharedFolders, invitedSharedFolders);

//        List<FoldersLinksAllResponse> ownFolderResponses = AllOwnerFolderslinksSelectTop1linksByCreateTime(ownFolders);
//        List<SharedFoldersLinksAllResponse> sharedFolderResponses = AllSharedFolderslinksSelectTop1linksByCreateTime(allSharedFolders);
//        UserFoldersWithSharedResponse response = UserFoldersWithSharedResponse.of(ownFolderResponses, sharedFolderResponses);
//        return ResponseEntity.ok(response);
//    }
//
//
//    public ResponseEntity<UserFoldersWithSharedResponse> getMeAllFoldersWithTop4Links(Long userId){
//        usersService.findById(userId);
//        List<Folders> allMyFolders = foldersService.findByUserUserId(userId);

        // defaultFolder == true인 폴더 (딱 하나라고 가정)
//        List<Folders> defaultFolderList = allMyFolders.stream()
//                .filter(folder -> !folder.isShared() && folder.isDefaultFolder())
//                .toList();

        // 나머지 공유되지 않은 폴더 중 defaultFolder == false 인 것들
//        List<Folders> otherOwnFolders = notSharedFoldersSelect(allMyFolders);

        // shared 컬럼으로 분리
//        List<Folders> ownFolders = conncatFolders(defaultFolderList, otherOwnFolders);

//        List<Folders> mySharedFolders = allMyFolders.stream()
//                .filter(Folders::isShared)
//                .sorted(Comparator.comparing(Folders::getCreateTime).reversed())
//                .collect(Collectors.toList());

        // 공유받은 폴더 (권한 테이블 기준)
//        List<FoldersPermissions> sharedPermissions = foldersPermissionsService.findByInvitedUserId(userId);
//        List<Folders> invitedSharedFolders = sharedPermissions.stream()
//                .map(permission -> foldersService.findById(permission.getFolder().getFolderId()))
//                .collect(Collectors.toList());

        // 공유 폴더 합치기
//        List<Folders> allSharedFolders = conncatFolders(mySharedFolders, invitedSharedFolders);

        //변환
//        List<FoldersLinksAllResponse> ownFolderResponses = AllOwnerFolderslinksSelectTop4linksByCreateTime(ownFolders);
//        List<SharedFoldersLinksAllResponse> sharedFolderResponses = AllSharedFolderslinksSelectTop1linksByCreateTime(allSharedFolders);
//        UserFoldersWithSharedResponse response = UserFoldersWithSharedResponse.of(ownFolderResponses, sharedFolderResponses);
//        return ResponseEntity.ok(response);
//    }
//
    @Transactional(readOnly = true)
    public ResponseEntity<FoldersLinksAllResponse> getOneFolderWithLinks(Long userId, Long folderId, Long targetUserId){
        usersService.findById(userId);
        usersService.findById(targetUserId);
        Folders folder = foldersService.findById(folderId);
        if (userId == targetUserId){
            List<FoldersLinks> folderLinksList = foldersLinksService.findByFolders(folder);
            List<Links> linksList = linksService.selectLinksByCreateTime(folderLinksList);
            return ResponseEntity.ok(FoldersLinksAllResponse.of(folder, linksList));
        }
        else {
            foldersService.isVisibleBy(folder);
            String redisKey = "view:" + userId + ":" + folderId;
            redisTemplate.opsForValue().set(redisKey, String.valueOf(folderId), Duration.ofMinutes(5));

            List<FoldersLinks> folderLinksList = foldersLinksService.findByFolders(folder);
            List<Links> linksList = linksService.selectLinksByCreateTime(folderLinksList);

            return ResponseEntity.ok(FoldersLinksAllResponse.of(folder, linksList));
        }

    }

    //조회수 -> Redis -> Kafka
//    @Transactional(readOnly = true)
//    public ResponseEntity<FoldersLinksAllResponse> getOneFolderWithLinksByUser(Long userId, OneFoldersLinksDetailViewRequest request) {
////        usersService.findById(request.getTargetUserId());
//
//        Long folderId = request.getFolderId();
//        Folders folder = foldersService.findById(folderId);
//        foldersService.isVisibleBy(folder);
//
//        String redisKey = "view:" + userId + ":" + folderId;
//        redisTemplate.opsForValue().set(redisKey, String.valueOf(folderId), Duration.ofMinutes(5));
//
//        List<FoldersLinks> folderLinksList = foldersLinksService.findByFolders(folder);
//        List<Links> linksList = linksService.selectLinksByCreateTime(folderLinksList);
//
//        return ResponseEntity.ok(FoldersLinksAllResponse.of(folder, linksList));
//    }
//
//
//    public ResponseEntity<FoldersLinksAllResponse> getMyOneFolderWithLinks(Long folderId){
//        //폴더가 있는지 검사
////        Folders folder = foldersService.findById(folderId);
//        //해당 폴더의 FoldersLinks 조회
//        List<FoldersLinks> folderLinksList = foldersLinksService.findByFolders(folder);
//        List<Links> linksList = linksService.selectLinksByCreateTime(folderLinksList);
//        // Folder + Links 리스트를 Response DTO로 변환
//        return ResponseEntity.ok(FoldersLinksAllResponse.of(folder, linksList));
//    }


    //Create - 폴더를 만들기
    public ResponseEntity<FoldersResponse> createUserFolder(Long userId, FoldersCreateRequest foldersCreateRequest)
    {
        Users users = usersService.findById(userId);
        // "", " ", "\t", "\d" , "\n"




        Folders folders = foldersService.save(foldersCreateRequest, users);
        if (Boolean.TRUE.equals(folders.isVisible())) {
//            feedRedisService.pushFeedToRedis(users, folders);

            activityFeedService.save(ActivityFeed.of(users.getUserId(), folders.getFolderId(), LocalDateTime.now()));

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

        foldersService.isOwnedByService(folders, userId);


        List<FoldersLinks> folderLinksList = foldersLinksService.findByFolders(folders);

        // FoldersLinks 테이블에서 해당 folderId를 가진 데이터 삭제
        foldersLinksService.deleteAll(folderLinksList);

        // folders 삭제
        foldersService.deleteById(folderId);

        //색인 업데이트
        foldersDocumentService.deleteFolderDocument(folders);


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

        if (folders.isVisible()) {
            Optional<Links> link = foldersLinksService.getFirstLinkByFolder(folders);
            foldersDocumentService.indexNewFolder(folders, link.orElse(null));
        } else {
            foldersDocumentService.deleteFolderDocument(folders);
        }

        return ResponseEntity.ok(FoldersResponse.from(folders, users.getUserId()));
    }

    @Transactional
    public ResponseEntity<FoldersLinksAllResponse> scrapFolder(Long userId, FoldersScrapRequest foldersScrapRequest) {
        Users user = usersService.findById(userId);

        Folders sourceFolder = foldersService.findById(foldersScrapRequest.getSourceFolderId());

        if (sourceFolder.isOwnedBy(userId)) {
            foldersService.sameScrapfolders();
        }

        Folders newFolder = foldersService.save(Folders.of(foldersScrapRequest, user));
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

    /**
     * 최신 스냅샷으로 헤드 동기화:
     * - ES 상위 SNAPSHOT_SIZE를 가져와 Redis 리스트의 앞부분과 비교
     * - 아직 없는 새 ID만 LPUSH(역순으로 push해서 최종 순서를 ES 순서대로 유지)
     * - 커서는 새로 끼어든 개수만큼 + (사용자가 보던 지점 유지)
     * - 너무 길면 LTRIM으로 제한(선택)
     */
    private void refreshFeedSnapshot(Long userId) {
        final String lKey = listKey(userId);
        final String cKey = cursorKey(userId);

        // ES 최신 상위 N개 ID 스냅샷
        List<Long> latestIds = foldersDocumentService
                .findTopNByOrderByCreateTimeDesc(SNAPSHOT_SIZE)
                .stream().map(FoldersDocument::getFolderId).toList();

        Long lenLong = redisTemplate.opsForList().size(lKey);
        int listLen = (lenLong == null) ? 0 : lenLong.intValue();

        // 리스트 비어있으면 초기화만
        if (listLen == 0) {
            if (!latestIds.isEmpty()) {
                redisTemplate.opsForList().rightPushAll(
                        lKey, latestIds.stream().map(String::valueOf).toArray(String[]::new)
                );
                redisTemplate.expire(lKey, TTL);
                if (redisTemplate.opsForValue().get(cKey) == null) {
                    redisTemplate.opsForValue().set(cKey, "0", TTL);
                }
            }
            return;
        }

        // 현재 헤드(최대 SNAPSHOT_SIZE) 읽기
        int end = Math.min(SNAPSHOT_SIZE - 1, listLen - 1);
        List<String> head = redisTemplate.opsForList().range(lKey, 0, end);
        if (head == null) head = List.of();
        Set<Long> headSet = head.stream().map(Long::valueOf).collect(Collectors.toSet());

        // 최신 목록에서 아직 없는 ID만 추려서 (원래 순서 유지)
        List<Long> newIds = latestIds.stream().filter(id -> !headSet.contains(id)).toList();
        if (newIds.isEmpty()) {
            // TTL만 갱신
            redisTemplate.expire(lKey, TTL);
            return;
        }

        // 새 ID를 헤드에 선삽입(LPUSH) — 역순으로 push해야 최종 순서가 latestIds 순서를 보존
        List<String> reversed = new ArrayList<>(newIds.stream().map(String::valueOf).toList());
        Collections.reverse(reversed);
        for (String idStr : reversed) {
            redisTemplate.opsForList().leftPush(lKey, idStr);
        }

        // 커서를 새로 끼어든 개수만큼 앞으로 밀기 (사용자 위치 보존)
        String cursorStr = redisTemplate.opsForValue().get(cKey);
        int cursor = (cursorStr == null) ? 0 : Integer.parseInt(cursorStr);
        int nextCursor = cursor + newIds.size();
        redisTemplate.opsForValue().set(cKey, String.valueOf(nextCursor), TTL);

        // 과도 성장 방지 (선택)
        int maxKeep = 1000;
        redisTemplate.opsForList().trim(lKey, 0, maxKeep - 1);

        redisTemplate.expire(lKey, TTL);
    }

    /**
     * /init-scroll
     * - ES에서 최신 상위 목록을 통째로 저장
     * - 커서=0 → 첫 페이지(0..PAGE_SIZE-1) 내려주고, 커서를 pageSize만큼 전진
     * - 응답 직전에 페이지 내부만 랜덤 섞기
     * - 매번 멱등(기존 키 삭제 후 재설정)
     */
    public ResponseEntity<List<ScrollFolderResponse>> initFeed(Long userId) {
        final String lKey = listKey(userId);
        final String cKey = cursorKey(userId);

        List<FoldersDocument> latest = foldersDocumentService.findTopNByOrderByCreateTimeDesc(SNAPSHOT_SIZE);
        List<Long> allIds = latest.stream().map(FoldersDocument::getFolderId).toList();

        // 멱등화
        redisTemplate.delete(List.of(lKey, cKey));

        if (!allIds.isEmpty()) {
            redisTemplate.opsForList().rightPushAll(
                    lKey, allIds.stream().map(String::valueOf).toArray(String[]::new)
            );
            redisTemplate.expire(lKey, TTL);
        }

        // 첫 페이지 산출
        int endIdx = Math.min(PAGE_SIZE, allIds.size());
        List<FoldersDocument> firstPageDocs = latest.subList(0, endIdx);

        // 커서 = 가져간 개수(다음 /scroll은 이어서 시작)
        redisTemplate.opsForValue().set(cKey, String.valueOf(endIdx), TTL);

        // 응답 내부 랜덤 섞기
        List<FoldersDocument> shuffled = new ArrayList<>(firstPageDocs);
        Collections.shuffle(shuffled);

        List<ScrollFolderResponse> body = shuffled.stream()
                .map(ScrollFolderResponse::from).toList();

        return ResponseEntity.ok(body);
    }

    /**
     * /scroll
     * - 키 없으면 204 → 프론트가 /init-scroll 호출
     * - 호출마다 refreshFeedSnapshot으로 새 글 헤드 반영 + 커서 이동
     * - 원형 슬라이스(끝이면 처음으로 래핑), 응답 직전 페이지 내부만 랜덤 섞기
     */
    public ResponseEntity<List<ScrollFolderResponse>> scrollFeed(Long userId) {
        final String lKey = listKey(userId);
        final String cKey = cursorKey(userId);

        // 새 글 동기화(헤드 반영 + 커서 이동)
        refreshFeedSnapshot(userId);

        Long lenLong = redisTemplate.opsForList().size(lKey);
        String cursorStr = redisTemplate.opsForValue().get(cKey);

        int listLen = (lenLong == null) ? 0 : lenLong.intValue();
        if (listLen == 0 || cursorStr == null) {
            return ResponseEntity.noContent().build(); // 프론트가 /init-scroll 하도록
        }

        int cursor = Integer.parseInt(cursorStr);
        if (cursor >= listLen) cursor = cursor % listLen;

        // 현재 cursor부터 끝까지 1차 슬라이스
        int end1 = Math.min(cursor + PAGE_SIZE - 1, listLen - 1);
        List<String> slice1 = redisTemplate.opsForList().range(lKey, cursor, end1);
        int fetched = (slice1 == null) ? 0 : slice1.size();

        // 래핑 필요 시 처음부터 추가
        List<String> idStrs = new ArrayList<>();
        if (slice1 != null) idStrs.addAll(slice1);

        if (fetched < PAGE_SIZE && listLen > 0) {
            int need = PAGE_SIZE - fetched;
            int end2 = Math.min(need - 1, listLen - 1);
            List<String> slice2 = redisTemplate.opsForList().range(lKey, 0, end2);
            if (slice2 != null) {
                idStrs.addAll(slice2);
                fetched += slice2.size();
            }
        }

        if (idStrs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // ES 조회 + 순서 보존 → 응답 직전 섞기
        List<Long> ids = idStrs.stream().map(Long::valueOf).toList();
        List<FoldersDocument> docs = foldersDocumentService.findByFolderIdIn(ids);

        Map<Long, FoldersDocument> map = docs.stream()
                .collect(Collectors.toMap(FoldersDocument::getFolderId, d -> d));

        List<FoldersDocument> ordered = ids.stream()
                .map(map::get)
                .filter(Objects::nonNull)
                .toList();

        List<FoldersDocument> shuffled = new ArrayList<>(ordered);
        Collections.shuffle(shuffled);

        // 커서 전진(원형)
        int nextCursor = (cursor + fetched) % listLen;
        redisTemplate.opsForValue().set(cKey, String.valueOf(nextCursor), TTL);
        redisTemplate.expire(lKey, TTL);

        List<ScrollFolderResponse> res = shuffled.stream()
                .map(ScrollFolderResponse::from).toList();

        return ResponseEntity.ok(res);
    }

    public void checkAccessPermission(Long folderId, Long userId) {
        Folders folder = foldersService.findById(folderId);

        if (!folder.isOwnedBy(userId)) {
            foldersPermissionsService.existsByFolderFolderIdAndInvitedUserId(folderId, userId);
        }
    }




}
