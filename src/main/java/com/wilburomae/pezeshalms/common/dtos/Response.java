package com.wilburomae.pezeshalms.common.dtos;

import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.ServerResponse;

public record Response<T>(HttpStatus responseCode, String responseDesc, T data) {

    public ServerResponse toServerResponse() {
        return ServerResponse.status(responseCode).body(this);
    }
}
