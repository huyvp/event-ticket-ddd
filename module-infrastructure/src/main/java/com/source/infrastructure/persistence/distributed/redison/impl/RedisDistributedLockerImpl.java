package com.source.infrastructure.persistence.distributed.redison.impl;

import com.source.infrastructure.persistence.distributed.redison.RedisDistributedLocker;
import com.source.infrastructure.persistence.distributed.redison.RedissonDistributedService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisDistributedLockerImpl implements RedissonDistributedService {

    @Resource
    private RedissonClient redissonClient;

    @Override
    public RedisDistributedLocker getRedisDistributedLock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);

        return new RedisDistributedLocker() {
            @Override
            public boolean tryLock(long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException {
                return lock.tryLock(waitTime, leaseTime, unit);
            }

            @Override
            public void lock(long leaseTime, TimeUnit unit) {
                lock.lock(leaseTime, unit);
            }

            @Override
            public void unlock() {
                if (isLocked() && isHelpByCurrentThread()) lock.unlock();
            }

            @Override
            public boolean isLocked() {
                return lock.isLocked();
            }

            @Override
            public boolean isHeldByThread(long threadId) {
                return lock.isHeldByThread(threadId);
            }

            @Override
            public boolean isHelpByCurrentThread() {
                return lock.isHeldByCurrentThread();
            }
        };
    }
}
