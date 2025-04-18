package com.Kkrap.Controller;

import com.Kkrap.RequestDTO.FoldersCreateRequest;

import com.Kkrap.RequestDTO.FoldersDeleteRequest;
import com.Kkrap.ResponseDto.FoldersResponse;
import com.Kkrap.ResponseDto.FoldersLinksAllResponse;
import com.Kkrap.Service.FoldersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RestController
@RequestMapping("/folders")
public class FoldersController {
    @Autowired
    private FoldersService foldersService;

    //Selected
    //사용자가 가지고 있는 모든 폴더와 내안 있는 링크들 같이 조회
    @GetMapping("/{userId}")
    public ResponseEntity<List<FoldersLinksAllResponse>> getFoldersAll(@PathVariable("userId") Long userId){
        return foldersService.getFoldersAll(userId);
    }

    //Create
    // 1. 폴더를 만드는 api
    @PostMapping("/{userId}")
    public ResponseEntity<FoldersResponse> createFolder(@PathVariable("userId") Long userId, @RequestBody FoldersCreateRequest foldersCreateRequest) {
        return foldersService.createFolder(userId, foldersCreateRequest);
    }

    //Delete
    @DeleteMapping("/{userId}")
    public ResponseEntity<FoldersResponse> deleteFolder(@PathVariable("userId") Long userId, @RequestBody FoldersDeleteRequest foldersDeleteRequest)
    {
        return foldersService.deleteFolder(userId, foldersDeleteRequest);
    }

}
