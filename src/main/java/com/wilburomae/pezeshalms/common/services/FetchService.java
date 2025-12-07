package com.wilburomae.pezeshalms.common.services;

import com.wilburomae.pezeshalms.common.dtos.Response;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.function.Function;

public interface FetchService {

    <T, U> Response<List<U>> fetch(JpaRepository<T, Long> repository, Function<T, U> mapper, String nameSingular);

    <T, U> Response<U> fetchById(JpaRepository<T, Long> repository, long id, Function<T, U> mapper, String nameSingular);
}
