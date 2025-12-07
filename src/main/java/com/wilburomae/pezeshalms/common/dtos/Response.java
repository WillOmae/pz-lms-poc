package com.wilburomae.pezeshalms.common.dtos;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public record Response<T>(HttpStatus responseCode, String responseDesc, T data) {

    public ResponseEntity<Response<T>> toEntity() {
        return ResponseEntity.status(responseCode).body(this);
    }
}
