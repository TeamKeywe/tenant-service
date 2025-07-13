package com.doubleo.tenantservice.domain.tenant.service;

import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HospitalTenantCacheService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String PREFIX = "tenant:";

    public Optional<String> getTenantIdFromCache(Long hospitalId) {
        String key = PREFIX + hospitalId;
        return Optional.ofNullable(redisTemplate.opsForValue().get(key));
    }

    public void setTenantIdToCache(Long hospitalId, String tenantId) {
        String key = PREFIX + hospitalId;
        redisTemplate.opsForValue().set(key, tenantId, Duration.ofHours(1)); // TTL
    }
}
