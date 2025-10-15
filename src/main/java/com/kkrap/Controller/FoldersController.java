package com.kkrap.Controller;

import com.kkrap.Controller.Spec.FoldersAPISpec;
import com.kkrap.RequestDTO.*;

import com.kkrap.ResponseDTO.*;
import com.kkrap.Service.FolderLink.FoldersManagerService;
import io.micrometer.core.annotation.Timed;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RestController
@Timed(value = "http.controller", extraTags = {"controller","Folders"})
@RequestMapping("/folders")
public class FoldersController implements FoldersAPISpec {
    private final FoldersManagerService foldersManagerService;

    public FoldersController(FoldersManagerService foldersManagerService){
        this.foldersManagerService = foldersManagerService;
    }

    @Override
    public ResponseEntity<UserFoldersWithSharedResponse> getAllFoldersWithLinks(Authentication authentication, Long targetUserId) {
        Long userId = Long.parseLong(authentication.getName());
        return foldersManagerService.getAllFoldersWithLinks(userId, targetUserId);
    }


    @Override
    public ResponseEntity<FoldersLinksAllResponse> getOneFolderWithLinks(Authentication authentication, Long folderId, Long targetUserId) {
        Long userId = Long.parseLong(authentication.getName());
        return foldersManagerService.getOneFolderWithLinks(userId, folderId, targetUserId);
    }

    @Override
    public ResponseEntity<FoldersLinksAllResponse> getNoAuthOneFolderWithLinks(Long folderId, Long targetUserId) {
        return foldersManagerService.getNoAuthOneFolderWithLinks(folderId, targetUserId);
    }

    @Override
    public ResponseEntity<FoldersResponse> createUserFolder(Authentication authentication, FoldersCreateRequest foldersCreateRequest) {
        Long userId = Long.parseLong(authentication.getName());
        return foldersManagerService.createUserFolder(userId, foldersCreateRequest);
    }

    @Override
    public ResponseEntity<FoldersResponse> deleteUserFolder(Authentication authentication, FoldersDeleteRequest foldersDeleteRequest) {
        Long userId = Long.parseLong(authentication.getName());
        return foldersManagerService.deleteUserFolder(userId, foldersDeleteRequest);
    }

    @Override
    public ResponseEntity<FoldersResponse> updateFolderMetadata(Authentication authentication, FoldersUpdateRequest request) {
        Long userId = Long.parseLong(authentication.getName());
        return foldersManagerService.updateFolderMetadata(userId, request);
    }

    @Override
    public ResponseEntity<FoldersLinksAllResponse> scrapFolder(Authentication authentication, FoldersScrapRequest request) {
        Long userId = Long.parseLong(authentication.getName());
        return foldersManagerService.scrapFolder(userId, request);
    }

    @Override
    public ResponseEntity<List<ScrollFolderResponse>> initFeed(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return foldersManagerService.initFeed(userId);
    }

    @Override
    public ResponseEntity<List<ScrollFolderResponse>> scrollFeed(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return foldersManagerService.scrollFeed(userId);
    }
}
