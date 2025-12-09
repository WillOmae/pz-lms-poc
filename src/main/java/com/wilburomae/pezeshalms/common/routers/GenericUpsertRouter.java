package com.wilburomae.pezeshalms.common.routers;

import com.wilburomae.pezeshalms.common.dtos.Response;
import com.wilburomae.pezeshalms.common.services.UpsertService;
import jakarta.servlet.ServletException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.io.IOException;

import static org.springframework.http.HttpStatus.*;

public class GenericUpsertRouter<T> implements HandlerFunction<ServerResponse> {

    private final Logger logger = LoggerFactory.getLogger(GenericUpsertRouter.class);
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
    public ServerResponse handle(ServerRequest req) throws ServletException {
        PermissionChecker.validatePermission(permission);

        Long id = null;
        if (req.method() == HttpMethod.PUT) {
            if (!req.pathVariables().containsKey("id")) {
                Exception e = new IllegalArgumentException("PUT requests must include path variable 'id'");
                logger.error(e.getMessage(), e);
                return new Response<>(METHOD_NOT_ALLOWED, "Invalid resource", null).toServerResponse();
            }
            id = Long.valueOf(req.pathVariable("id"));
        }

        T request;
        try {
            request = req.body(requestClass);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return new Response<>(BAD_REQUEST, "Invalid request body", null).toServerResponse();
        }

        validator.validate(request);

        try {
            return upsertService.upsert(id, request).toServerResponse();
        } catch (DataIntegrityViolationException e) {
            logger.error(e.getMessage(), e);
            return new Response<>(CONFLICT, "Duplicate entry", null).toServerResponse();
        }
    }
}