package com.Kkrap.Controller;

import com.Kkrap.RequestDTO.LinksCreateRequest;
import com.Kkrap.RequestDTO.LinksDeleteRequest;
import com.Kkrap.Service.LinksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/links")
public class LinksController {
    @Autowired
    private LinksService linksService;

    //Create
    //링크 저장
    @PostMapping("/{userId}")
    @ResponseBody
    public ResponseEntity<LinksCreateRequest> createLink(@PathVariable("userId") Long userId, @RequestBody LinksCreateRequest linksCreateRequest){
        return linksService.createLink(userId, linksCreateRequest);
    }

    //Delete
    //링크 삭제
    @DeleteMapping("/{userId}")
    public ResponseEntity<LinksDeleteRequest> deleteLink(@PathVariable("userId") Long userId, @RequestBody LinksDeleteRequest linksDeleteRequest){
        return linksService.DeleteLink(userId, linksDeleteRequest);
    }

}
