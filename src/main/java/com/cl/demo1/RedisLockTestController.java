package com.cl.demo1;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/redis-lock")
public class RedisLockTestController {

    @Autowired
    private RedisLockUtil redisLockUtil;

    // 用于跟踪并发请求
    private final ConcurrentHashMap<String, AtomicInteger> counters = new ConcurrentHashMap<>();

    @GetMapping("/test")
    public String test() {
        return "Hello, Redis Lock!";
    }

    /**
     * 测试基本锁功能
     */
    @GetMapping("/basic")
    public String testBasicLock() {
        String lockKey = "api:test:basic";
        String requestId = UUID.randomUUID().toString();
        long expireTime = 10;

        boolean locked = redisLockUtil.tryLock(lockKey, requestId, expireTime);
        if (locked) {
            try {
                // 模拟业务处理
                Thread.sleep(10000);
                return "获取锁成功，业务处理完成";
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "处理中断";
            } finally {
                redisLockUtil.releaseLock(lockKey, requestId);
            }
        } else {
            return "获取锁失败，锁已被占用";
        }
    }

    /**
     * 测试并发锁
     */
    @GetMapping("/concurrent/{counterName}")
    public String testConcurrentLock(@PathVariable String counterName) {
        String lockKey = "api:test:concurrent:" + counterName;
        String requestId = UUID.randomUUID().toString();
        long expireTime = 10;

        // 初始化计数器
        counters.putIfAbsent(counterName, new AtomicInteger(0));

        boolean locked = redisLockUtil.tryLock(lockKey, requestId, expireTime);
        if (locked) {
            try {
                // 原子性增加计数器
                int count = counters.get(counterName).incrementAndGet();
                // 模拟业务处理
                Thread.sleep(2000);
                return "获取锁成功，当前计数: " + count;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "处理中断";
            } finally {
                redisLockUtil.releaseLock(lockKey, requestId);
            }
        } else {
            return "获取锁失败，锁已被占用";
        }
    }

    /**
     * 获取计数器值
     */
    @GetMapping("/counter/{counterName}")
    public String getCounter(@PathVariable String counterName) {
        AtomicInteger counter = counters.get(counterName);
        if (counter != null) {
            return "计数器 " + counterName + " 的值: " + counter.get();
        } else {
            return "计数器 " + counterName + " 不存在";
        }
    }
}

