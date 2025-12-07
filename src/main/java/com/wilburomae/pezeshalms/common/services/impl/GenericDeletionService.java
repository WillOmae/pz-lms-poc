package com.wilburomae.pezeshalms.common.services.impl;

import com.wilburomae.pezeshalms.common.dtos.Response;
import com.wilburomae.pezeshalms.common.services.DeletionService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import static org.springframework.http.HttpStatus.OK;

@Service
public class GenericDeletionService implements DeletionService {

    @Override
    public <T> Response<Void> delete(JpaRepository<T, Long> repository, long id, String nameSingular) {
        repository.deleteById(id);
        return new Response<>(OK, "%s deleted successfully".formatted(nameSingular), null);
    }
}
