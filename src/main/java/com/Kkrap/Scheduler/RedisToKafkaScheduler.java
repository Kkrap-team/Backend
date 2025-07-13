package com.Kkrap.Scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class RedisToKafkaScheduler {

    private final StringRedisTemplate redisTemplate;
    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String VIEW_TOPIC = "folder-view-topic";
    private static final String SCRAP_TOPIC = "folder-scrap-topic";


    // 5분마다 실행
//    @Scheduled(fixedRate = 300000)  // 300,000ms = 5분
    @Scheduled(fixedRate = 300000)
    public void flushViewsToKafka() {

        Set<String> viewKeys = redisTemplate.keys("view:*");
        if (viewKeys != null) {
            for (String key : viewKeys) {
                String folderId = redisTemplate.opsForValue().get(key);
                if (folderId != null) {
                    kafkaTemplate.send(VIEW_TOPIC, folderId);
                }
                redisTemplate.delete(key);
            }
        }

        Set<String> scrapKeys = redisTemplate.keys("scrap:*");
        if (scrapKeys != null) {
            for (String key : scrapKeys) {
                String folderId = key.split(":")[1];
                String count = redisTemplate.opsForValue().get(key);
                if (count != null) {
                    String message = folderId + ":" + count;
                    kafkaTemplate.send(SCRAP_TOPIC, message);
                }
                redisTemplate.delete(key);
            }
        }


    }
}