package com.kkrap.Service.Users;

import com.kkrap.Entity.Users;
import com.kkrap.ResponseDTO.FoldersUserProfileResponse;
import com.kkrap.ResponseDTO.MessageResponse;
import com.kkrap.ResponseDTO.UsersProfileResponse;
import com.kkrap.Service.FolderLink.FoldersService;
import com.kkrap.Service.FollowsFoldersPermission.FollowsService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UsersManagerService {

    private final UsersService usersService;

    private final FoldersService foldersService;


    private final FollowsService followsService;



    private static final Logger logger = LoggerFactory.getLogger(UsersManagerService.class);

    public UsersManagerService(UsersService usersService, FoldersService foldersService,
                               FollowsService followsService){
        this.usersService = usersService;
        this.foldersService = foldersService;
        this.followsService = followsService;
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
        try{
            usersService.findByNickname(nickname);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(MessageResponse.of(200, "사용 가능한 닉네임입니다."));
    }




}
