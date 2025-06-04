package com.Kkrap.Service.Users;


import com.Kkrap.Entity.Users;
import com.Kkrap.ResponseDto.MessageResponse;
import com.Kkrap.ResponseDto.UsersProfileResponse;
import com.Kkrap.Util.FileStorageUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsersManagerService {

    private final UsersService usersService;

    public UsersManagerService(UsersService usersService){
        this.usersService = usersService;
    }

    public ResponseEntity<UsersProfileResponse> getUserProfile(Long userId){
        Users users = usersService.findById(userId);
        return ResponseEntity.ok(UsersProfileResponse.of(users.getUserId(),users.getEmail(), users.getNickname(), users.getProfile(), users.getKakaoId(), users.getBio()));
    }

    public ResponseEntity<UsersProfileResponse> updateUserProfile(Long userId, String newNickname, String bio){
        Users users = usersService.findById(userId);
        users.setNickname(newNickname);
        users.setBio(bio);
        usersService.save(users);
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
