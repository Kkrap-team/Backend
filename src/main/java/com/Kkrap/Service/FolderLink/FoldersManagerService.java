package com.Kkrap.Service.FolderLink;

import com.Kkrap.Entity.Folders;
import com.Kkrap.Entity.FoldersLinks;
import com.Kkrap.Entity.Links;
import com.Kkrap.Entity.Users;
import com.Kkrap.RequestDTO.FoldersCreateRequest;
import com.Kkrap.RequestDTO.FoldersDeleteRequest;
import com.Kkrap.RequestDTO.FoldersUpdateRequest;
import com.Kkrap.ResponseDto.FoldersLinksAllResponse;
import com.Kkrap.ResponseDto.FoldersResponse;
import com.Kkrap.Service.Users.UsersService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoldersManagerService {
    private final FoldersService foldersService;

    private final UsersService usersService;

    private final FoldersLinksService foldersLinksService;

    private final LinksService linksService;

    private final StringRedisTemplate redisTemplate;

    public FoldersManagerService(FoldersService foldersService, UsersService usersService, FoldersLinksService foldersLinksService,
                                 LinksService linksService, StringRedisTemplate redisTemplate) {
        this.foldersService = foldersService;
        this.usersService = usersService;
        this.foldersLinksService = foldersLinksService;
        this.linksService = linksService;
        this.redisTemplate = redisTemplate;
    }


    public ResponseEntity<List<FoldersLinksAllResponse>> getUserAllFoldersWithLinks(Long userId){
        usersService.findById(userId);
        List<Folders> folders = foldersService.findByUserUserId(userId);
        List<FoldersLinksAllResponse> responseList = folders.stream().map(folder -> {
            List<FoldersLinks> folderLinksList = foldersLinksService.findByFolders(folder);
            List<Links> linksList = linksService.selectLinksByCreateTime(folderLinksList);
            return FoldersLinksAllResponse.of(folder, linksList);
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseList);
    }

    public ResponseEntity<List<FoldersLinksAllResponse>> getUserAllFoldersWithTop4Links(Long userId){
        usersService.findById(userId);
        List<Folders> folders = foldersService.findByUserUserId(userId);
        List<FoldersLinksAllResponse> responseList = folders.stream().map(folder -> {
            List<FoldersLinks> folderLinksList = foldersLinksService.findByFolders(folder);
            List<Links> linksList = linksService.selectTop4LinksByCreateTime(folderLinksList);
            return FoldersLinksAllResponse.of(folder, linksList);
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseList);
    }

    //조회수 -> Redis -> Kafka
    public ResponseEntity<FoldersLinksAllResponse> getOneFolderWithLinksByUser(Long userId, Long folderId){
        Folders folder = foldersService.findById(folderId);
        // Redis에 조회 기록 저장 (userId와 folderId 기준으로 TTL 5분)
        String redisKey = "view:" + userId + ":" + folderId;
        redisTemplate.opsForValue().set(redisKey, String.valueOf(folderId), Duration.ofMinutes(5));

        //해당 폴더의 FoldersLinks 조회
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
        return ResponseEntity.ok(FoldersResponse.from(folders, users.getUserId()));
    }

    //Delete - 폴더 삭제
    public ResponseEntity<FoldersResponse> deleteUserFolder(Long userId, FoldersDeleteRequest foldersDeleteRequest)
    {
        //사용자 체크
        usersService.findById(userId);

        //FolderLinks에 해당 folderId에 속한 FolderLinks 리스트 조회
        Long folderId = foldersDeleteRequest.getFolderId();
        Folders folders = foldersService.findById(folderId);
        List<FoldersLinks> folderLinksList = foldersLinksService.findByFolders(folders);

        for (FoldersLinks folderLink : folderLinksList) {
            Links link = folderLink.getLinks();
            linksService.deleteById(link.getLinkId());
        }

        // FoldersLinks 테이블에서 해당 folderId를 가진 데이터 삭제
        foldersLinksService.deleteAll(folderLinksList);

        // folders 삭제
        foldersService.deleteById(folderId);

        //응답 데이터를 삭제된 링크들까지 포함 시켜서 해주어야함 - 정환행님한테 물어보기
        return ResponseEntity.ok(FoldersResponse.from(folders, userId));
    }

    public ResponseEntity<FoldersResponse> updateFolderMetadata(FoldersUpdateRequest request){
        Users users = usersService.findById(request.getUserId());
        return foldersService.updateFolderMetadata(request.getFolderId(), users.getUserId(), request.getFolderName(), request.getFolderDescription(), request.isVisible());
    }


}
