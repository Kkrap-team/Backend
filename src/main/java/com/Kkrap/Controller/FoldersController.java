package com.Kkrap.Controller;

import com.Kkrap.Controller.Spec.FoldersAPISpec;
import com.Kkrap.RequestDTO.*;

import com.Kkrap.ResponseDTO.*;
import com.Kkrap.Service.FolderLink.FoldersManagerService;
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
    public ResponseEntity<UserFoldersWithSharedResponse> getUserAllFoldersWithLinks(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return foldersManagerService.getUserAllFoldersWithLinks(userId);
    }

    @Override
    public ResponseEntity<UserFoldersWithSharedResponse> getAllFoldersWithTop4LinksByUser(Authentication authentication,
                                                                                          @RequestBody FoldersAllLinksViewRequest request) {
        Long userId = Long.parseLong(authentication.getName());
        return foldersManagerService.getAllFoldersWithTop4LinksByUser(userId, request);
    }

    @Override
    public ResponseEntity<UserFoldersWithSharedResponse> getMeAllFoldersWithTop4Links(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return foldersManagerService.getMeAllFoldersWithTop4Links(userId);
    }

    @Override
    public ResponseEntity<FoldersLinksAllResponse> getOneFolderWithLinksByUser(Authentication authentication,
                                                                               @RequestBody OneFoldersLinksDetailViewRequest request) {
        Long userId = Long.parseLong(authentication.getName());
        return foldersManagerService.getOneFolderWithLinksByUser(userId, request);
    }

    @Override
    public ResponseEntity<FoldersLinksAllResponse> getMyOneFolderWithLinks(Authentication authentication, Long folderId) {
        return foldersManagerService.getMyOneFolderWithLinks(folderId);
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
