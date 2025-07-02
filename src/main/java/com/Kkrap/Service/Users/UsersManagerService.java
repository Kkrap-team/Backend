package com.Kkrap.Service.Users;


import com.Kkrap.Entity.Folders;
import com.Kkrap.Entity.Users;
import com.Kkrap.ResponseDTO.FoldersUserProfileResponse;
import com.Kkrap.ResponseDTO.MessageResponse;
import com.Kkrap.ResponseDTO.UsersProfileResponse;
import com.Kkrap.Service.FolderLink.FoldersService;
import com.Kkrap.Service.FoldersDocument.FoldersDocumentService;
import com.Kkrap.Service.FollowsFoldersPermission.FollowsService;
import com.Kkrap.Util.FileStorageUtil;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class UsersManagerService {

    private final UsersService usersService;

    private final FoldersService foldersService;

    private final FoldersDocumentService foldersDocumentService;

    private final FollowsService followsService;

    public UsersManagerService(UsersService usersService, FoldersService foldersService, FoldersDocumentService foldersDocumentService,
                               FollowsService followsService){
        this.usersService = usersService;
        this.foldersService = foldersService;
        this.foldersDocumentService = foldersDocumentService;
        this.followsService = followsService;
    }

    public ResponseEntity<UsersProfileResponse> getUserProfile(Long userId){
        Users users = usersService.findById(userId);
        return ResponseEntity.ok(UsersProfileResponse.of(users.getUserId(),users.getEmail(), users.getNickname(), users.getProfile(), users.getKakaoId(), users.getBio()));
    }

    public ResponseEntity<FoldersUserProfileResponse> getFoldersUserProfile(Long userId){
        Users user = usersService.findById(userId);

        Long totalViewCount = foldersService.sumViewCountByUser(user);
        Long followingCount = followsService.countFollower(user.getUserId());

        return ResponseEntity.ok(
                FoldersUserProfileResponse.of(user, totalViewCount, followingCount)
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
        foldersDocumentService.updateUserInfoInFolderDocuments(users, userFolders);

        return ResponseEntity.ok(UsersProfileResponse.from(users));
    }

    //사진만 업로드
    public ResponseEntity<UsersProfileResponse> uploadUserProfileImage(Long userId, MultipartFile file){
        Users user = usersService.findById(userId);
        // 파일 저장
        String savedFileName = FileStorageUtil.storeProfileImage(file);
        // DB에 접근 URL 경로만 저장
        user.setProfile("/profile/" + savedFileName);
        Users users = usersService.save(user);

        return ResponseEntity.ok(UsersProfileResponse.from(users));
    }

    //닉네임 중복확인
    public ResponseEntity<MessageResponse> checkNicknameAvailable(String nickname){
        usersService.findByNickname(nickname);
        return ResponseEntity.ok(MessageResponse.of(200, "사용 가능한 닉네임입니다."));
    }




}
