package com.doubleo.tenantservice.domain.tenant.repository;

import com.doubleo.tenantservice.domain.tenant.domain.HospitalTenant;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalTenantRepository extends JpaRepository<HospitalTenant, Long> {
    Optional<HospitalTenant> findHospitalTenantByHospitalId(Long hospitalId);

    Optional<HospitalTenant> findByTenantId(String tenantId);
}
