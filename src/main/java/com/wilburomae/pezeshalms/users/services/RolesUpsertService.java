package com.wilburomae.pezeshalms.users.services;

import com.wilburomae.pezeshalms.common.dtos.Response;
import com.wilburomae.pezeshalms.common.services.UpsertService;
import com.wilburomae.pezeshalms.users.dtos.RoleRequest;
import org.springframework.stereotype.Service;

@Service
public class RolesUpsertService implements UpsertService<RoleRequest> {

    @Override
    public Response<Long> upsert(Long id, RoleRequest request) {
        return null;
    }
}
