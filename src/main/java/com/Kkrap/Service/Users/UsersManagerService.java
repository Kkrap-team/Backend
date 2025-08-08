package com.Kkrap.Service.Users;


import com.Kkrap.Entity.Folders;
import com.Kkrap.Entity.Links;
import com.Kkrap.Entity.Users;
import com.Kkrap.ResponseDTO.FoldersUserProfileResponse;
import com.Kkrap.ResponseDTO.MessageResponse;
import com.Kkrap.ResponseDTO.UsersProfileResponse;
import com.Kkrap.Service.FolderLink.FoldersLinksService;
import com.Kkrap.Service.FolderLink.FoldersService;
import com.Kkrap.Service.FoldersDocument.FoldersDocumentService;
import com.Kkrap.Service.FollowsFoldersPermission.FollowsService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersManagerService {

    private final UsersService usersService;

    private final FoldersService foldersService;

    private final FoldersDocumentService foldersDocumentService;

    private final FollowsService followsService;

    private final FoldersLinksService foldersLinksService;

    private static final Logger logger = LoggerFactory.getLogger(UsersManagerService.class);

    public UsersManagerService(UsersService usersService, FoldersService foldersService, FoldersDocumentService foldersDocumentService,
                               FollowsService followsService, FoldersLinksService foldersLinksService){
        this.usersService = usersService;
        this.foldersService = foldersService;
        this.foldersDocumentService = foldersDocumentService;
        this.followsService = followsService;
        this.foldersLinksService = foldersLinksService;
    }

    public ResponseEntity<UsersProfileResponse> getUserProfile(Long userId){
        Users users = usersService.findById(userId);
        return ResponseEntity.ok(UsersProfileResponse.of(users.getUserId(),users.getEmail(), users.getNickname(), users.getProfile(), users.getBio()));
    }

    public ResponseEntity<FoldersUserProfileResponse> getFoldersUserProfile(Long userId){
        Users user = usersService.findById(userId);

        Long totalViewCount = foldersService.sumViewCountByUser(user);
        Long totalScrapCount = foldersService.sumScrapCountByUser(user);
        Long followingCount = followsService.countFollower(user.getUserId());

        return ResponseEntity.ok(
                FoldersUserProfileResponse.of(user, totalViewCount, totalScrapCount ,followingCount)
        );
 }

    @Transactional
    public ResponseEntity<UsersProfileResponse> updateUserProfile(Long userId, String newNickname, String bio){
        Users users = usersService.findById(userId);
        users.setNickname(newNickname);
        users.setBio(bio);
        usersService.save(users);

        //색인 업데이트
        List<Folders> userFolders = foldersService.findByUserIdAndVisibleTrue(userId);
        userFolders.forEach(folder -> {
            Links link = foldersLinksService.getFirstLinkByFolder(folder).orElse(null);
            foldersDocumentService.updateUserInfoInFolderDocuments(users, folder, link);
        });
        logger.info("[Elasticsearch] 사용자 정보 변경으로 색인 업데이트 완료: userId=" + users.getUserId());


        return ResponseEntity.ok(UsersProfileResponse.from(users));
    }

    //사진만 업로드
//    public ResponseEntity<UsersProfileResponse> uploadUserProfileImage(Long userId, MultipartFile file){
//        Users user = usersService.findById(userId);
//        // 파일 저장
//        String savedFileName = FileStorageUtil.storeProfileImage(file);
//        // DB에 접근 URL 경로만 저장
//        user.setProfile("/profile/" + savedFileName);
//        Users users = usersService.save(user);
//
//        return ResponseEntity.ok(UsersProfileResponse.from(users));
//    }
//    public ResponseEntity<UsersProfileResponse> uploadUserProfileImage(Long userId, MultipartFile file){
//        Users user = usersService.findById(userId);
//
//        // (선택) 기존 이미지 삭제
//        String oldUrl = user.getProfile();
//        if (oldUrl != null && oldUrl.contains("amazonaws.com")) {
//            s3Service.deleteObjectByUrl(oldUrl);
//        }
//
//        // S3 업로드
//        String imageUrl = s3Service.uploadProfileImage(userId, file);
//
//        // DB에는 접근 가능한 절대 URL 저장
//        user.setProfile(imageUrl);
//        Users saved = usersService.save(user);
//
//        return ResponseEntity.ok(UsersProfileResponse.from(saved));
//    }


    //닉네임 중복확인
    public ResponseEntity<MessageResponse> checkNicknameAvailable(String nickname){
        usersService.findByNickname(nickname);
        return ResponseEntity.ok(MessageResponse.of(200, "사용 가능한 닉네임입니다."));
    }




}
