package com.Kkrap.Controller;

import com.Kkrap.RequestDTO.NicknameRequest;
import com.Kkrap.ResponseDto.MessageResponseDTO;
import com.Kkrap.ResponseDto.UserProfileResponse;
import com.Kkrap.Service.UsersService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/v2/users")
public class UsersController {
    @Autowired
    UsersService usersService;

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
    public ResponseEntity<MessageResponseDTO> updateNickname(@PathVariable("userId") Long userId, @RequestBody NicknameRequest request)
    {

        boolean updated = usersService.updateNickName(userId, request.getNickname());
        if (updated) {
            MessageResponseDTO message = new MessageResponseDTO("닉네임 변경 완료");
            return ResponseEntity.ok(message);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponseDTO("해당 ID의 사용자를 찾을 수 없습니다."));
        }
    }

}
