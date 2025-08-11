package com.Kkrap.Scheduler;


import com.Kkrap.Entity.ActivityFeed;
import com.Kkrap.ResponseDTO.FeedRedisDTO;
import com.Kkrap.Service.ActivityFeed.ActivityFeedService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FeedRedisFlushScheduler {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final ActivityFeedService activityFeedService;

    private static final Logger logger = LoggerFactory.getLogger(FeedRedisFlushScheduler.class);

    public FeedRedisFlushScheduler(
            RedisTemplate<String, String> redisTemplate,
            ObjectMapper objectMapper,
            ActivityFeedService activityFeedService
    )
    {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.activityFeedService = activityFeedService;
    }

    private static final String REDIS_KEY = "feed:buffer";

//    @Scheduled(fixedRate = 1000 * 60 * 60) // 1시간마다 실행
    public void flushFeedBufferToDB() {
        logger.info("[FeedFlush] 활동 스케줄러 시작됨");
        List<ActivityFeed> feedList = new ArrayList<>();

        while (Boolean.TRUE.equals(redisTemplate.hasKey(REDIS_KEY))) {
            String json = redisTemplate.opsForList().leftPop(REDIS_KEY);
            if (json == null) break;

            try {
                FeedRedisDTO dto = objectMapper.readValue(json, FeedRedisDTO.class);

                ActivityFeed feed = ActivityFeed.of(dto.getActorUserId(), dto.getFolderId(), dto.getCreatedAt());
                feedList.add(feed);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        if (!feedList.isEmpty()) {
            activityFeedService.saveAll(feedList);
            logger.info("[FeedFlush] 총 " + feedList.size() + "건 저장 완료");
        }
    }

}
