package com.wilburomae.pezeshalms.common.services;

import com.wilburomae.pezeshalms.common.data.entities.IdAuditableEntity;
import com.wilburomae.pezeshalms.common.dtos.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;

import java.util.function.Supplier;

import static org.springframework.http.HttpStatus.*;

public interface UpsertService<T> {

    Response<Long> upsert(Long id, T request);

    default <A extends IdAuditableEntity> Response<A> initEntity(Long id, JpaRepository<A, Long> repository, Supplier<A> constructor) {
        if (id != null) {
            return repository.findById(id)
                    .map(a -> new Response<>(OK, "Initialised", a))
                    .orElseGet(() -> new Response<>(NOT_FOUND, "Not found by id", null));
        } else {
            return new Response<>(OK, "Initialised", constructor.get());
        }
    }

    default <A extends IdAuditableEntity> Response<Long> successResponse(Long id, String nameSingular, A entity) {
        HttpStatus status = id == null ? CREATED : OK;
        String action = id == null ? "created" : "updated";
        return new Response<>(status, "%s %s successfully".formatted(nameSingular, action), entity.getId());
    }
}
