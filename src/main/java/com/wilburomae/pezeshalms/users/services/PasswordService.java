package com.wilburomae.pezeshalms.users.services;

import com.wilburomae.pezeshalms.common.dtos.Response;
import com.wilburomae.pezeshalms.users.dtos.PasswordChangeRequest;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {

    public Response<Void> changePassword(PasswordChangeRequest request) {
        return null;
    }
}
