package com.Kkrap.Controller;

import com.Kkrap.RequestDTO.LinksCreateRequest;
import com.Kkrap.Service.LinksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v2/links")
public class LinksController {
    @Autowired
    private LinksService linksService;

//    @GetMapping("/links")
//    public List<LinksResponseDTO> SelectedMemberLinks(@RequestParam(name = "userId") Long user_id){
//        return linksService.SelectedMemberLinks(user_id);
//    }

    //Create
    //링크 저장
    @PostMapping("/{userId}/create")
    @ResponseBody
    public ResponseEntity<Object> createLink(@PathVariable("userId") Long userId, @RequestBody LinksCreateRequest linksCreateRequest){
        return linksService.CreateLink(userId, linksCreateRequest);
    }

//    @PostMapping("/links/delete")
//    public ResponseEntity<LinksDeleteDTO> deleteLink(@RequestBody LinksDeleteDTO linksDeleteDTO) {
//        System.out.println("UserId: " + linksDeleteDTO.getUserId());
//
//        // LinkId가 null이 아닌지 확인한 후 출력
//        if (linksDeleteDTO.getLinkId() != null) {
//            for (Long linkId : linksDeleteDTO.getLinkId()) {
//                System.out.println("LinkId: " + linkId);
//            }
//        } else {
//            System.out.println("LinkId 리스트가 비어있습니다.");
//        }
//
//        linksService.deleteLink(linksDeleteDTO);
//        //삭제 된 걸 보내주자
//        return ResponseEntity.ok(linksDeleteDTO);
//    }

}
