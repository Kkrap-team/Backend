package com.Kkrap.Controller;

import com.Kkrap.Controller.Spec.ActivityFeedAPISpec;
import com.Kkrap.ResponseDTO.FeedFolderResponse;
import com.Kkrap.Service.ActivityFeed.ActivityManagerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/activityfeed")
public class ActivityFeedController implements ActivityFeedAPISpec {

    private final ActivityManagerService activityManagerService;

    public ActivityFeedController(ActivityManagerService activityManagerService){
        this.activityManagerService = activityManagerService;
    }

    @Override
    public ResponseEntity<List<FeedFolderResponse>> getFeedForUser(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return activityManagerService.getFeedForUser(userId);
    }
}
