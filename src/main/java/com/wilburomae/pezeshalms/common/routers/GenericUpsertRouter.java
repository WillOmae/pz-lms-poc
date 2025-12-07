package com.wilburomae.pezeshalms.common.routers;

import com.wilburomae.pezeshalms.common.services.UpsertService;
import jakarta.servlet.ServletException;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.io.IOException;

public class GenericUpsertRouter<T> implements HandlerFunction<ServerResponse> {

    private final String permission;
    private final UpsertService<T> upsertService;
    private final Class<T> requestClass;
    private final RequestValidatorService validator;

    public GenericUpsertRouter(String permission, UpsertService<T> upsertService, Class<T> requestClass, RequestValidatorService validator) {
        this.permission = permission;
        this.upsertService = upsertService;
        this.requestClass = requestClass;
        this.validator = validator;
    }

    @Override
    public ServerResponse handle(ServerRequest req) throws ServletException, BadRequestException {
        PermissionChecker.validatePermission(permission);

        Long id = null;
        if (req.method() == HttpMethod.PUT) {
            if (!req.pathVariables().containsKey("id")) {
                throw new IllegalArgumentException("PUT requests must include path variable 'id'");
            }
            id = Long.valueOf(req.pathVariable("id"));
        }

        T request;
        try {
            request = req.body(requestClass);
        } catch (IOException e) {
            throw new BadRequestException(e);
        }

        validator.validate(request);

        return upsertService.upsert(id, request).toServerResponse();
    }
}