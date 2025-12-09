package com.wilburomae.pezeshalms.transactions.services;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Random;

@Service
public class TransactionReferenceGenerator {

    private static final Random RANDOM = new SecureRandom();

    public String generateReference() {
        long timestamp = Instant.now().toEpochMilli();
        int random4 = RANDOM.nextInt(1000, 10000);
        long combined = Long.parseLong(timestamp + "" + random4);
        return Long.toString(combined, 36).toUpperCase();
    }
}
