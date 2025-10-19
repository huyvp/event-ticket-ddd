package com.source.infrastructure.persistence.distributed.redison;

public interface RedisDistributedService {
    RedisDistributedLocker getDistributedLock(String lockKey);
}
