package com.doubleo.tenantservice.domain.tenant.controller;

import com.doubleo.tenantservice.domain.tenant.service.TenantService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    @PostMapping("/update")
    public ResponseEntity<Void> TokensUpdate(@RequestBody Map<String, String> tokenMap) {
        tenantService.updateTokens(tokenMap);
        return ResponseEntity.ok().build();
    }
}
