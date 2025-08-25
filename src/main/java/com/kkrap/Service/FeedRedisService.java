package com.kkrap.Service;

import com.kkrap.Entity.Folders;
import com.kkrap.Entity.Users;
import com.kkrap.ResponseDTO.FeedRedisDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class FeedRedisService {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public FeedRedisService(
            RedisTemplate<String, String> redisTemplate,
            ObjectMapper objectMapper)
    {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;

    }

    public void pushFeedToRedis(Users actor, Folders folder) {
        FeedRedisDTO dto = FeedRedisDTO.from(actor, folder);
        try {
            String json = objectMapper.writeValueAsString(dto);
            redisTemplate.opsForList().rightPush("feed:buffer", json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize FeedRedisDTO", e);
        }
    }
}
