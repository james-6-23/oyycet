package com.cet.practice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * 缓存服务类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String CACHE_PREFIX = "cet:";

    /**
     * 设置缓存
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(CACHE_PREFIX + key, value);
    }

    /**
     * 设置缓存（带过期时间）
     */
    public void set(String key, Object value, Duration ttl) {
        redisTemplate.opsForValue().set(CACHE_PREFIX + key, value, ttl);
    }

    /**
     * 设置缓存（带过期时间）
     */
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(CACHE_PREFIX + key, value, timeout, unit);
    }

    /**
     * 获取缓存
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) redisTemplate.opsForValue().get(CACHE_PREFIX + key);
    }

    /**
     * 获取缓存（带类型）
     */
    public <T> T get(String key, Class<T> clazz) {
        Object value = redisTemplate.opsForValue().get(CACHE_PREFIX + key);
        if (value == null) {
            return null;
        }
        return clazz.cast(value);
    }

    /**
     * 删除缓存
     */
    public Boolean delete(String key) {
        return redisTemplate.delete(CACHE_PREFIX + key);
    }

    /**
     * 判断 key 是否存在
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(CACHE_PREFIX + key);
    }

    /**
     * 设置过期时间
     */
    public Boolean expire(String key, Duration ttl) {
        return redisTemplate.expire(CACHE_PREFIX + key, ttl);
    }

    /**
     * 获取过期时间
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(CACHE_PREFIX + key);
    }
}
