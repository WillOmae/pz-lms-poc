package com.wilburomae.pezeshalms.users;

import com.wilburomae.pezeshalms.common.dtos.Response;
import com.wilburomae.pezeshalms.security.DBUserDetails;
import com.wilburomae.pezeshalms.security.SecurityService;
import com.wilburomae.pezeshalms.users.dtos.PasswordChangeRequest;
import com.wilburomae.pezeshalms.users.services.PasswordService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.wilburomae.pezeshalms.users.Permissions.*;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    private final SecurityService securityService;
    private final PasswordService passwordService;

    public AuthenticationController(SecurityService securityService, PasswordService passwordService) {
        this.securityService = securityService;
        this.passwordService = passwordService;
    }

    @RolesAllowed({LOGIN})
    @PostMapping(value = "/login", produces = "application/json")
    public ResponseEntity<Response<String>> login(@AuthenticationPrincipal DBUserDetails userDetails) {
        return securityService.login(userDetails).toEntity();
    }

    @RolesAllowed({WRITE_CREDENTIALS, READ_CREDENTIALS})
    @PutMapping(value = "/password", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Response<Void>> changePassword(@Valid @RequestBody PasswordChangeRequest request) {
        return passwordService.changePassword(request).toEntity();
    }
}
