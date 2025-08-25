package com.kkrap.ResponseDTO;

import com.kkrap.Entity.Folders;
import com.kkrap.Entity.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class FeedRedisDTO {
    private Long actorUserId;
    private Long folderId;
    private LocalDateTime createdAt;

    private FeedRedisDTO(){}

    public static FeedRedisDTO from(Users actor, Folders folder){
        return new FeedRedisDTO(actor.getUserId(), folder.getFolderId(), LocalDateTime.now());
    }
}
