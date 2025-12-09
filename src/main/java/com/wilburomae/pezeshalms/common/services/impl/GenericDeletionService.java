package com.wilburomae.pezeshalms.common.services.impl;

import com.wilburomae.pezeshalms.common.dtos.Response;
import com.wilburomae.pezeshalms.common.services.DeletionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import static org.springframework.http.HttpStatus.*;

@Service
public class GenericDeletionService implements DeletionService {

    private final Logger logger = LoggerFactory.getLogger(GenericDeletionService.class);

    @Override
    public <T> Response<Void> delete(JpaRepository<T, Long> repository, long id, String nameSingular) {
        if (!repository.existsById(id)) {
            return new Response<>(NOT_FOUND, "%s not found".formatted(nameSingular), null);
        }

        try {
            repository.deleteById(id);
            return new Response<>(OK, "%s deleted successfully".formatted(nameSingular), null);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new Response<>(INTERNAL_SERVER_ERROR, "Error deleting %s".formatted(nameSingular), null);
        }
    }
}
