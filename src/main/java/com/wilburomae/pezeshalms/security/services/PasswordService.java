package com.wilburomae.pezeshalms.security.services;

import com.wilburomae.pezeshalms.common.dtos.Response;
import com.wilburomae.pezeshalms.security.dtos.PasswordChangeRequest;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {

    public Response<Void> changePassword(PasswordChangeRequest request) {
        return null;
    }
}
