package com.wilburomae.pezeshalms.users;

import com.wilburomae.pezeshalms.common.routers.CrudRouteBuilder;
import com.wilburomae.pezeshalms.common.routers.PermissionChecker;
import com.wilburomae.pezeshalms.security.DBUserDetails;
import com.wilburomae.pezeshalms.security.SecurityService;
import com.wilburomae.pezeshalms.users.data.repositories.RoleRepository;
import com.wilburomae.pezeshalms.users.data.repositories.UserRepository;
import com.wilburomae.pezeshalms.users.dtos.*;
import com.wilburomae.pezeshalms.users.services.PasswordService;
import com.wilburomae.pezeshalms.users.services.RolesUpsertService;
import com.wilburomae.pezeshalms.users.services.UsersUpsertService;
import org.apache.coyote.BadRequestException;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import java.io.IOException;

@Component
public class UsersRouter {

    @Bean
    public RouterFunction<ServerResponse> usersRoutes(CrudRouteBuilder builder, UserRepository repository, UsersUpsertService upsertService) {
        return builder.build("USERS", "User", "/users", UserRequest.class, User::from, repository, upsertService);
    }

    @Bean
    public RouterFunction<ServerResponse> rolesRoutes(CrudRouteBuilder builder, RoleRepository repository, RolesUpsertService upsertService) {
        return builder.build("ROLES", "Role", "/roles", RoleRequest.class, Role::from, repository, upsertService);
    }

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
