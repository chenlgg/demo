package com.cl.demo1;

import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Component
public class RedisLockUtil {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 尝试获取分布式锁
     * @param lockKey 锁的 key
     * @param requestId 请求唯一标识（建议用 UUID）
     * @param expireSeconds 锁的过期时间（秒）
     * @return 是否获取成功
     */
    public boolean tryLock(String lockKey, String requestId, long expireSeconds) {
        Boolean success = stringRedisTemplate
                .opsForValue()
                .setIfAbsent(lockKey, requestId, expireSeconds, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(success);
    }

    /**
     * 释放分布式锁（使用 Lua 脚本保证原子性）
     * @param lockKey 锁的 key
     * @param requestId 请求唯一标识
     * @return 是否释放成功
     */
    public boolean releaseLock(String lockKey, String requestId) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

        RedisCallback<Long> callback = connection -> {
            byte[] scriptBytes = script.getBytes(StandardCharsets.UTF_8);
            byte[] keyBytes = lockKey.getBytes(StandardCharsets.UTF_8);
            byte[] valueBytes = requestId.getBytes(StandardCharsets.UTF_8);

            // ✅ 正确：ReturnType.INTEGER，因为脚本返回的是 0 或 1（Lua 中的数字）
            return (Long) connection.eval(
                    scriptBytes,           // 脚本字节数组
                    ReturnType.INTEGER,    // 返回类型：INTEGER（0 或 1）
                    1,                     // key 的数量
                    keyBytes,              // KEYS[1]
                    valueBytes             // ARGV[1]
            );
        };

        Long result = stringRedisTemplate.execute(callback);
        return result != null && result == 1;
    }
}