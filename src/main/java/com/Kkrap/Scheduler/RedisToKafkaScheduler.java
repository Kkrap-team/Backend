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

    private static final String TOPIC_NAME = "folder-view-topic";

    // 5분마다 실행
//    @Scheduled(fixedRate = 300000)  // 300,000ms = 5분
    @Scheduled(fixedRate = 200000)
    public void flushViewsToKafka() {
        Set<String> keys = redisTemplate.keys("view:*");

        if (keys == null || keys.isEmpty()) return;

        for (String key : keys) {
            String folderId = redisTemplate.opsForValue().get(key);
            if (folderId != null) {
                kafkaTemplate.send(TOPIC_NAME, folderId);  // value만 전송
                redisTemplate.delete(key);
                System.out.println("[Scheduler] Kafka로 전송 (folderId=" + folderId + ") 및 Redis에서 삭제: " + key);
            }
        }
    }
}