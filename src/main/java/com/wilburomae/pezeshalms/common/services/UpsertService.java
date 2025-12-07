package com.wilburomae.pezeshalms.common.services;

import com.wilburomae.pezeshalms.common.dtos.Response;

public interface UpsertService<T> {

    Response<Long> upsert(Long id, T request);
}
