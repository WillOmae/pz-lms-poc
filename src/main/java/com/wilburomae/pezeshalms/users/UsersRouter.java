package com.wilburomae.pezeshalms.users;

import com.wilburomae.pezeshalms.common.routers.CrudRouteBuilder;
import com.wilburomae.pezeshalms.users.data.repositories.RoleRepository;
import com.wilburomae.pezeshalms.users.data.repositories.UserRepository;
import com.wilburomae.pezeshalms.users.dtos.Role;
import com.wilburomae.pezeshalms.users.dtos.RoleRequest;
import com.wilburomae.pezeshalms.users.dtos.User;
import com.wilburomae.pezeshalms.users.dtos.UserRequest;
import com.wilburomae.pezeshalms.users.services.RolesUpsertService;
import com.wilburomae.pezeshalms.users.services.UsersUpsertService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

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
}
