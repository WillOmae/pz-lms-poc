package com.wilburomae.pezeshalms.security;

import com.wilburomae.pezeshalms.common.routers.PermissionChecker;
import org.apache.coyote.BadRequestException;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import java.io.IOException;

@Component
public class AuthenticationRouter {

    @Bean
    public RouterFunction<ServerResponse> authenticationRoutes(SecurityService securityService, PasswordService passwordService) {
        String commonUrl = "/authentication";

        return RouterFunctions.route()
                .POST(commonUrl + "/login", req -> {
                    PermissionChecker.validatePermission("LOGIN");
                    DBUserDetails userDetails = (DBUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                    return securityService.login(userDetails).toServerResponse();
                })
                .POST(commonUrl + "/password", req -> {
                    PermissionChecker.validatePermission("WRITE_CREDENTIALS");
                    PasswordChangeRequest request;
                    try {
                        request = req.body(PasswordChangeRequest.class);
                    } catch (IOException e) {
                        throw new BadRequestException(e);
                    }
                    return passwordService.changePassword(request).toServerResponse();
                })
                .build();
    }
}
