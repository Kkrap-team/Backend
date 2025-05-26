package com.Kkrap.Controller;

import com.Kkrap.RequestDTO.LinksCreateRequest;
import com.Kkrap.RequestDTO.LinksDeleteRequest;
import com.Kkrap.ResponseDto.LinksCreateResponse;
import com.Kkrap.Service.FolderLink.LinksService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
    @Operation(summary = "link 저장", description = "링크를 저장하게 되는데 사용자가 모든 저장 링크 폴더에만 저장을 하면 모든 저장 링크 폴더에만 링크가 저장됩니다. 만약 다른 폴더에서 저장하면 다른 폴더에서 링크가 생기고 모든 링크가 저장된 폴더에서도 링크가 생기게 됩니다.")
    @ResponseBody
    public ResponseEntity<LinksCreateResponse> createLink(
            @Parameter(name = "userId", description = "사용자 ID", required = true, example = "1")
            @PathVariable("userId") Long userId, @RequestBody LinksCreateRequest linksCreateRequest){
        return linksService.createLink(userId, linksCreateRequest);
    }

    //Delete
    //링크 삭제
    @DeleteMapping("/{userId}")
    @Operation(summary = "link 삭제", description = "만약 링크가 다른 폴더에 있을 때 모든 링크 폴더에서 삭제하면 다른 폴더에서도 삭제가 됩니다.")
    public ResponseEntity<LinksDeleteRequest> deleteLink(
            @Parameter(name = "userId", description = "사용자 ID", required = true, example = "1")
            @PathVariable("userId") Long userId, @RequestBody LinksDeleteRequest linksDeleteRequest){
        return linksService.DeleteLink(userId, linksDeleteRequest);
    }

}
