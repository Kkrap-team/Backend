package com.Kkrap.Controller;

import com.Kkrap.Controller.Spec.FoldersAPISpec;
import com.Kkrap.RequestDTO.FoldersCreateRequest;

import com.Kkrap.RequestDTO.FoldersDeleteRequest;
import com.Kkrap.RequestDTO.FoldersScrapRequest;
import com.Kkrap.RequestDTO.FoldersUpdateRequest;
import com.Kkrap.ResponseDTO.*;
import com.Kkrap.Service.FolderLink.FoldersManagerService;
import io.micrometer.core.annotation.Timed;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<UserFoldersWithSharedResponse> getUserAllFoldersWithLinks(Long userId) {
        return foldersManagerService.getUserAllFoldersWithLinks(userId);
    }

    @Override
    public ResponseEntity<UserFoldersWithSharedResponse> getAllFoldersWithTop4LinksByUser(Long userId) {
        return foldersManagerService.getAllFoldersWithTop4LinksByUser(userId);
    }

    @Override
    public ResponseEntity<UserFoldersWithSharedResponse> getMeAllFoldersWithTop4Links(Long userId) {
        return foldersManagerService.getMeAllFoldersWithTop4Links(userId);
    }

    @Override
    public ResponseEntity<FoldersLinksAllResponse> getOneFolderWithLinksByUser(Long userId, Long folderId) {
        return foldersManagerService.getOneFolderWithLinksByUser(userId, folderId);
    }

    @Override
    public ResponseEntity<FoldersLinksAllResponse> getMyOneFolderWithLinks(Long folderId) {
        return foldersManagerService.getMyOneFolderWithLinks(folderId);
    }

    @Override
    public ResponseEntity<FoldersResponse> createUserFolder(Long userId, FoldersCreateRequest foldersCreateRequest) {
        return foldersManagerService.createUserFolder(userId, foldersCreateRequest);
    }

    @Override
    public ResponseEntity<FoldersResponse> deleteUserFolder(Long userId, FoldersDeleteRequest foldersDeleteRequest) {
        return foldersManagerService.deleteUserFolder(userId, foldersDeleteRequest);
    }

    @Override
    public ResponseEntity<FoldersResponse> updateFolderMetadata(FoldersUpdateRequest request) {
        return foldersManagerService.updateFolderMetadata(request);
    }

    @Override
    public ResponseEntity<FoldersLinksAllResponse> scrapFolder(Long userId, FoldersScrapRequest request) {
        return foldersManagerService.scrapFolder(userId, request);
    }

    @Override
    public ResponseEntity<List<ScrollFolderResponse>> initFeed(Long userId) {
        return foldersManagerService.initFeed(userId);
    }

    @Override
    public ResponseEntity<List<ScrollFolderResponse>> scrollFeed(Long userId) {
        return foldersManagerService.scrollFeed(userId);
    }
}
