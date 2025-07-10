package com.doubleo.tenantservice.global.exception.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum TenantErrorCode implements BaseErrorCode {
    TENANT_NOT_FOUND(HttpStatus.NOT_FOUND, "tenant 정보를 찾을 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String errorClassName() {
        return this.name();
    }
}
