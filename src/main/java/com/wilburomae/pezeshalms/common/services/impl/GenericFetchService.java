package com.wilburomae.pezeshalms.common.services.impl;

import com.wilburomae.pezeshalms.common.dtos.Response;
import com.wilburomae.pezeshalms.common.services.FetchService;
import com.wilburomae.pezeshalms.common.utilities.StringUtilities;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@Service
public class GenericFetchService implements FetchService {

    @Transactional
    @Override
    public <T, U> Response<List<U>> fetch(JpaRepository<T, Long> repository, Function<T, U> mapper, String nameSingular) {
        List<U> fetched = repository.findAll().stream().map(mapper).toList();
        int count = fetched.size();
        return new Response<>(OK, "Fetched %d %s".formatted(count, (count == 1 ? nameSingular : StringUtilities.toPlural(nameSingular))), fetched);
    }

    @Transactional
    @Override
    public <T, U> Response<U> fetchById(JpaRepository<T, Long> repository, long id, Function<T, U> mapper, String nameSingular) {
        return repository.findById(id)
                .map(entity -> new Response<>(OK, "%s found".formatted(nameSingular), mapper.apply(entity)))
                .orElseGet(() -> new Response<>(NOT_FOUND, "%s not found".formatted(nameSingular), null));
    }
}
