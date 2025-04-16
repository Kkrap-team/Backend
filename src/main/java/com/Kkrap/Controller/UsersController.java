package com.Kkrap.Controller;

import com.Kkrap.Repository.FoldersRepository;
import com.Kkrap.Repository.UsersRepository;
import com.Kkrap.RequestDTO.NicknameRequest;
import com.Kkrap.ResponseDto.UsersProfileResponse;
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
    public ResponseEntity<UsersProfileResponse> getUserProfile(@PathVariable("userId") Long userId)
    {
        return usersService.getUserProfile(userId);
    }

    //닉네임 변경
    @PostMapping("/{userId}/nickname")
    @ResponseBody
    public ResponseEntity<UsersProfileResponse> updateNickname(@PathVariable("userId") Long userId, @RequestBody NicknameRequest request)
    {
        return usersService.updateNickName(userId, request.getNickname());
    }

}
