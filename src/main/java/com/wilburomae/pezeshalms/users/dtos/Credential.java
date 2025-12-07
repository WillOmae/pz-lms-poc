package com.wilburomae.pezeshalms.users.dtos;

public record Credential(String hashedPassword,
                         String status) {
}
