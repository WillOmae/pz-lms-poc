package com.wilburomae.pezeshalms.products.services;

import com.wilburomae.pezeshalms.common.dtos.Response;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.http.HttpStatus.OK;

@Service
public class PaymentsService {

    public Response<String> send(BigDecimal amount, String msisdn, String reference) {
        return new Response<>(OK, "Sent", UUID.randomUUID().toString());
    }
}
