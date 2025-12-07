package com.wilburomae.pezeshalms.users.services;

import com.wilburomae.pezeshalms.common.dtos.Response;
import com.wilburomae.pezeshalms.common.services.UpsertService;
import com.wilburomae.pezeshalms.users.dtos.UserRequest;
import org.springframework.stereotype.Service;

@Service
public class UsersUpsertService implements UpsertService<UserRequest> {

    @Override
    public Response<Long> upsert(Long id, UserRequest userRequest) {
        return null;
    }
}
