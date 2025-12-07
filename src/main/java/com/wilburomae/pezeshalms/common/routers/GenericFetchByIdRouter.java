package com.wilburomae.pezeshalms.common.routers;

import com.wilburomae.pezeshalms.common.services.FetchService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.function.Function;

public class GenericFetchByIdRouter<T, U> implements HandlerFunction<ServerResponse> {

    private final String permission;
    private final String nameSingular;
    private final Function<T, U> mapper;
    private final JpaRepository<T, Long> repository;
    private final FetchService fetchService;

    public GenericFetchByIdRouter(String permission, String nameSingular, Function<T, U> mapper, JpaRepository<T, Long> repository, FetchService fetchService) {
        this.permission = permission;
        this.nameSingular = nameSingular;
        this.mapper = mapper;
        this.repository = repository;
        this.fetchService = fetchService;
    }

    @Override
    public ServerResponse handle(ServerRequest req) {
        PermissionChecker.validatePermission(permission);
        long id = Long.parseLong(req.pathVariable("id"));
        return fetchService.fetchById(repository, id, mapper, nameSingular).toServerResponse();
    }
}