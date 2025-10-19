package com.source.application.service.ticket.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.source.application.model.cache.TicketDetailCache;
import com.source.domain.model.entity.TicketDetail;
import com.source.domain.service.TicketDetailDomainService;
import com.source.infrastructure.cache.redis.RedisInfrastructureService;
import com.source.infrastructure.persistence.distributed.redison.RedisDistributedLocker;
import com.source.infrastructure.persistence.distributed.redison.RedisDistributedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class TicketDetailCacheServiceRefactor {

    private final RedisDistributedService redisDistributedService;
    private final RedisInfrastructureService redisInfrastructureService;
    private final TicketDetailDomainService ticketDetailDomainService;

    // Use guava
    private final static Cache<Long, TicketDetailCache> ticketDetailLocalCache = CacheBuilder.newBuilder()
            .initialCapacity(100)
            .concurrencyLevel(12)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

    public boolean orderTicketByUser(Long ticketId) {
        ticketDetailLocalCache.invalidate(ticketId);   // Remove local cache
        redisInfrastructureService.delete(
                generateEventItemKey(ticketId)
        );
        return true;
    }

    /**
     * get ticket item by id in cache
     */
    public TicketDetailCache getTicketDetailCache(Long ticketId, Long version) {
        // 1 - get data from local cache
        TicketDetailCache ticketDetailCache = getTicketDetailLocalCache(ticketId);
        if (ticketDetailCache != null) {
            // User: Version, cache:version
            if (version == null) {
                log.info("01: GET TICKET FROM LOCAL CACHE: versionUser:{}, versionLocal:{}", version, ticketDetailCache.getVersion());
                return ticketDetailCache;
            }

            if (version.equals(ticketDetailCache.getVersion())) {
                log.info("02: GET TICKET FROM LOCAL CACHE: versionUser:{}, versionLocal:{}", version, ticketDetailCache.getVersion());
                return ticketDetailCache;
            }

            if (version < ticketDetailCache.getVersion()) {
                log.info("03: GET TICKET FROM LOCAL CACHE: versionUser:{}, versionLocal:{}", version, ticketDetailCache.getVersion());
                return ticketDetailCache;
            }

            return getTicketDetailDistributedCache(ticketId);
        }
        return getTicketDetailDistributedCache(ticketId);
    }

    /**
     * get ticket from distributed cache
     */
    public TicketDetailCache getTicketDetailDistributedCache(Long ticketId) {
        // 1 - Get data
        TicketDetailCache ticketDetailCache = redisInfrastructureService.getObject(
                generateEventItemKey(ticketId), TicketDetailCache.class
        );

        if (ticketDetailCache == null) {
            log.info("GET TICKET FROM DISTRIBUTED CACHE");
            ticketDetailCache = getTicketDetailDatabase(ticketId);
        }

        // 2 - Put data to local
        // 2.1 lock
        ticketDetailLocalCache.put(ticketId, ticketDetailCache); // -> Consistency cache
        // 2.2 unlock
        log.info("GET TICKET FROM DISTRIBUTED CACHE");
        return ticketDetailCache;
    }
    /**
     * get ticket from database
     */
    private TicketDetailCache getTicketDetailDatabase(Long ticketId) {
        RedisDistributedLocker locker = redisDistributedService.getDistributedLock(
                generateEventItemKeyLock(ticketId)
        );
        try {
            boolean isLock = locker.tryLock(1, 5, TimeUnit.SECONDS);
            if (!isLock) return null;
            // Get cache
            TicketDetailCache ticketDetailCache = redisInfrastructureService.getObject(
                    generateEventItemKey(ticketId), TicketDetailCache.class
            );
            // Yes
            if (ticketDetailCache != null) return ticketDetailCache;

            TicketDetail ticketDetail = ticketDetailDomainService.getTicketDetailById(ticketId);

            ticketDetailCache = new TicketDetailCache().withClone(ticketDetail).withVersion(System.currentTimeMillis());
            redisInfrastructureService.setObject(generateEventItemKey(ticketId), ticketDetailCache);
            return ticketDetailCache;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            locker.unlock();
        }
    }

    private TicketDetailCache getTicketDetailLocalCache(Long ticketId) {
        return ticketDetailLocalCache.getIfPresent(ticketId);
    }

    private String generateEventItemKey(Long ticketId) {
        return "PRO_TICKET:ITEM" + ticketId;
    }

    private String generateEventItemKeyLock(Long ticketId) {
        return "PRO_LOCK_KEY_ITEM" + ticketId;
    }
}
