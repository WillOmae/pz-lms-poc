package com.wilburomae.pezeshalms.common.services;

import com.wilburomae.pezeshalms.common.dtos.Response;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeletionService {

    <T> Response<Void> delete(JpaRepository<T, Long> repository, long id, String nameSingular);
}
