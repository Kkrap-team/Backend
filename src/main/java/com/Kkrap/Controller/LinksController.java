package com.Kkrap.Controller;

import com.Kkrap.RequestDTO.LinksDTO;
import com.Kkrap.RequestDTO.LinksDeleteDTO;
import com.Kkrap.ResponseDto.LinksResponseDTO;
import com.Kkrap.ResponseDto.MessageResponseDTO;
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

    @GetMapping("/links")
    public List<LinksResponseDTO> SelectedMemberLinks(@RequestParam(name = "userId") Long user_id){
        return linksService.SelectedMemberLinks(user_id);
    }

    @PostMapping("/links/create")
    @ResponseBody
    public ResponseEntity<MessageResponseDTO> createLink(@RequestBody LinksDTO linksDTO){
        linksService.createLink(linksDTO.getUserId(), linksDTO.getLinkUrl());
        MessageResponseDTO responseDTO = new MessageResponseDTO("create Link Success");
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/links/delete")
    public ResponseEntity<LinksDeleteDTO> deleteLink(@RequestBody LinksDeleteDTO linksDeleteDTO) {
        System.out.println("UserId: " + linksDeleteDTO.getUserId());

        // LinkId가 null이 아닌지 확인한 후 출력
        if (linksDeleteDTO.getLinkId() != null) {
            for (Long linkId : linksDeleteDTO.getLinkId()) {
                System.out.println("LinkId: " + linkId);
            }
        } else {
            System.out.println("LinkId 리스트가 비어있습니다.");
        }

        linksService.deleteLink(linksDeleteDTO);
        //삭제 된 걸 보내주자
        return ResponseEntity.ok(linksDeleteDTO);
    }

}
