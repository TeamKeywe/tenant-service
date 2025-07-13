package com.doubleo.tenantservice.domain.tenant.service;

import java.util.Map;

public interface TenantService {
    void updateTokens(Map<String, String> tokenMap);
}
