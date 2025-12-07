package com.wilburomae.pezeshalms.common.routers;

import com.wilburomae.pezeshalms.common.services.DeletionService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

public class GenericDeleteRouter<T> implements HandlerFunction<ServerResponse> {

    private final String permission;
    private final String nameSingular;
    private final JpaRepository<T, Long> repository;
    private final DeletionService deletionService;

    public GenericDeleteRouter(String permission, String nameSingular, JpaRepository<T, Long> repository, DeletionService deletionService) {
        this.permission = permission;
        this.nameSingular = nameSingular;
        this.repository = repository;
        this.deletionService = deletionService;
    }

    @Override
    public ServerResponse handle(ServerRequest req) {
        PermissionChecker.validatePermission(permission);
        long id = Long.parseLong(req.pathVariable("id"));
        return deletionService.delete(repository, id, nameSingular).toServerResponse();
    }
}