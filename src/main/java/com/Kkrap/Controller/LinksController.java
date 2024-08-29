package com.Kkrap.Controller;

import com.Kkrap.RequestDTO.LinksDTO;
import com.Kkrap.ResponseDto.LinksResponseDTO;
import com.Kkrap.ResponseDto.MessageResponseDTO;
import com.Kkrap.Entity.Links;
import com.Kkrap.Service.LinksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class LinksController {
    @Autowired
    private LinksService linksService;

    @GetMapping("/user")
    public List<LinksResponseDTO> SelectedMemberLinks(@RequestParam(name = "user_id") Long user_id){
        return linksService.SelectedMemberLinks(user_id);
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<MessageResponseDTO> createLink(@RequestBody LinksDTO linksDTO){
        linksService.createLink(linksDTO.getUserId(), linksDTO.getLinkUrl());
        MessageResponseDTO responseDTO = new MessageResponseDTO("create Link Success");
        return ResponseEntity.ok(responseDTO);
    }

}
