package com.wilburomae.pezeshalms.users.services;

import com.wilburomae.pezeshalms.common.dtos.Response;
import com.wilburomae.pezeshalms.users.dtos.UserRequest;
import org.springframework.stereotype.Service;

@Service
public class UsersUpsertService {

    public Response<Long> upsert(Long id, UserRequest userRequest) {
        return null;
    }
}
