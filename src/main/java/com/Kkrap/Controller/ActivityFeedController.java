package com.Kkrap.Controller;

import com.Kkrap.Controller.Spec.ActivityFeedAPISpec;
import com.Kkrap.ResponseDTO.FeedFolderResponse;
import com.Kkrap.Service.ActivityFeed.ActivityFeedService;
import com.Kkrap.Service.ActivityFeed.ActivityManagerService;
import com.Kkrap.Service.Users.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/activityfeed")
public class ActivityFeedController implements ActivityFeedAPISpec {

    private final ActivityManagerService activityManagerService;
    private final UsersService usersService;

    public ActivityFeedController(ActivityManagerService activityManagerService,
                                  UsersService usersService){
        this.activityManagerService = activityManagerService;
        this.usersService = usersService;
    }

    @Override
    public ResponseEntity<List<FeedFolderResponse>> getFeedForUser(Long userId) {
        return activityManagerService.getFeedForUser(userId);
    }
}
