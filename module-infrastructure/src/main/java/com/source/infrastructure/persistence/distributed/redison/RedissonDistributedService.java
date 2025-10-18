package com.source.infrastructure.persistence.distributed.redison;

public interface RedissonDistributedService {
    RedisDistributedLocker getRedisDistributedLock(String lockKey);
}
