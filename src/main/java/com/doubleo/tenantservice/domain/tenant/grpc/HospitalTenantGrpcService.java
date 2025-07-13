package com.doubleo.tenantservice.domain.tenant.grpc;

import com.doubleo.tenantservice.domain.tenant.domain.HospitalTenant;
import com.doubleo.tenantservice.domain.tenant.repository.HospitalTenantRepository;
import com.doubleo.tenantservice.domain.tenant.service.HospitalTenantCacheService;
import com.doubleo.tenantservice.global.exception.CommonException;
import com.doubleo.tenantservice.global.exception.errorcode.TenantErrorCode;
import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@RequiredArgsConstructor
@GrpcService
@Slf4j
public class HospitalTenantGrpcService
        extends HospitalTenantServiceGrpc.HospitalTenantServiceImplBase {

    private final HospitalTenantRepository hospitalTenantRepository;
    private final HospitalTenantCacheService cacheService;

    @Override
    public void getTenantIdByHospitalId(
            HospitalIdToTenantIdRequest request,
            StreamObserver<HospitalIdToTenantIdResponse> responseObserver) {

        Long hospitalId = request.getHospitalId();

        String tenantId =
                cacheService
                        .getTenantIdFromCache(hospitalId)
                        .orElseGet(
                                () -> {
                                    HospitalTenant tenant =
                                            hospitalTenantRepository
                                                    .findHospitalTenantByHospitalId(hospitalId)
                                                    .orElseThrow(
                                                            () ->
                                                                    new CommonException(
                                                                            TenantErrorCode
                                                                                    .TENANT_NOT_FOUND));
                                    cacheService.setTenantIdToCache(
                                            hospitalId, tenant.getTenantId());
                                    return tenant.getTenantId();
                                });
        log.info("Hospital Id: {}", tenantId);

        HospitalIdToTenantIdResponse response =
                HospitalIdToTenantIdResponse.newBuilder()
                        .setHospitalId(hospitalId)
                        .setTenantId(tenantId)
                        .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getTokenByTenantId(
            GetTokensRequest request, StreamObserver<GetTokenResponse> responseObserver) {
        String tenantId = request.getTenantId();
        HospitalTenant tenant =
                hospitalTenantRepository
                        .findByTenantId(tenantId)
                        .orElseThrow(() -> new CommonException(TenantErrorCode.TENANT_NOT_FOUND));
        GetTokenResponse response =
                GetTokenResponse.newBuilder().setWalletToken(tenant.getWalletToken()).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateTokensByTenantId(
            UpdateTokensRequest request, StreamObserver<UpdateTokensResponse> responseObserver) {
        List<TenantWalletToken> tokens = request.getTokensList();
        List<HospitalTenant> updatedTenants = new ArrayList<>();
        for (TenantWalletToken token : tokens) {
            String tenantId = token.getTenantId();
            String walletToken = token.getWalletToken();

            HospitalTenant tenant =
                    hospitalTenantRepository
                            .findByTenantId(tenantId)
                            .orElseThrow(
                                    () -> new CommonException(TenantErrorCode.TENANT_NOT_FOUND));
            tenant.setWalletToken(walletToken);
            updatedTenants.add(tenant);
        }
        hospitalTenantRepository.saveAll(updatedTenants);

        UpdateTokensResponse response = UpdateTokensResponse.newBuilder().setStatus("OK").build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
