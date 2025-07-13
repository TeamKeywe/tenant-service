package com.doubleo.tenantservice.domain.tenant.service;

import com.doubleo.tenantservice.domain.tenant.domain.HospitalTenant;
import com.doubleo.tenantservice.domain.tenant.repository.HospitalTenantRepository;
import com.doubleo.tenantservice.global.exception.CommonException;
import com.doubleo.tenantservice.global.exception.errorcode.TenantErrorCode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {

    private final HospitalTenantRepository hospitalTenantRepository;

    @Override
    public void updateTokens(Map<String, String> tokenMap) {
        List<HospitalTenant> updatedTenants = new ArrayList<>();
        tokenMap.forEach(
                (tenantId, walletToken) -> {
                    HospitalTenant tenant =
                            hospitalTenantRepository
                                    .findByTenantId(tenantId)
                                    .orElseThrow(
                                            () ->
                                                    new CommonException(
                                                            TenantErrorCode.TENANT_NOT_FOUND));
                    tenant.setWalletToken(walletToken);
                    updatedTenants.add(tenant);
                });
        hospitalTenantRepository.saveAll(updatedTenants);
    }
}
