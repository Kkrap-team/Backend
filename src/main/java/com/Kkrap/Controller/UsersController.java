package com.Kkrap.Controller;

import com.Kkrap.Repository.FoldersRepository;
import com.Kkrap.Repository.UsersRepository;
import com.Kkrap.RequestDTO.NicknameRequest;
import com.Kkrap.ResponseDto.MessageResponse;
import com.Kkrap.ResponseDto.UserProfileResponse;
import com.Kkrap.Service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UsersController {
    @Autowired
    UsersService usersService;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    FoldersRepository foldersRepository;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    //users 프로필 조회
    @GetMapping("/{userId}")
    @ResponseBody
    public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable("userId") Long userId)
    {
        UserProfileResponse userProfile = usersService.getUserProfile(userId);
        if (userProfile != null) {
            return ResponseEntity.ok(userProfile);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    //닉네임 변경
    @PostMapping("/{userId}/nickname")
    @ResponseBody
    public ResponseEntity<MessageResponse> updateNickname(@PathVariable("userId") Long userId, @RequestBody NicknameRequest request)
    {

        boolean updated = usersService.updateNickName(userId, request.getNickname());
        if (updated) {
            MessageResponse message = new MessageResponse(404,"닉네임 변경 완료");
            return ResponseEntity.ok(message);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(404,"해당 ID의 사용자를 찾을 수 없습니다."));
        }
    }

}
