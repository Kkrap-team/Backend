package com.Kkrap.Controller.Spec;


import com.Kkrap.RequestDTO.*;
import com.Kkrap.ResponseDTO.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "ActivityFeed", description = "사용자 폴더 생성 로그")
public interface ActivityFeedAPISpec {
    //토큰 관리 방식
    @GetMapping("/feed")
    @Operation(summary = "팔로잉 폴더 생성 폴더 확인 api", description = "사용자들의 폴더 생성 단, 내가 팔로잉한 친구들만")
    ResponseEntity<List<FeedFolderResponse>> getFeedForUser(
            Authentication authentication
    );

}
