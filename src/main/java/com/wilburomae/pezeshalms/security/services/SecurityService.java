package com.wilburomae.pezeshalms.security.services;

import com.wilburomae.pezeshalms.common.dtos.Response;
import com.wilburomae.pezeshalms.security.SecurityProperties;
import com.wilburomae.pezeshalms.security.dtos.DBUserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.springframework.http.HttpStatus.OK;

@Service
public class SecurityService {

    private final JwtEncoder encoder;
    private final SecurityProperties cp;

    public SecurityService(SecurityProperties cp, JwtEncoder encoder) {
        this.cp = cp;
        this.encoder = encoder;
    }

    public Response<String> login(DBUserDetails userDetails) {
        int expiryTimeSeconds = cp.getTokenExpiryTimeSeconds();
        String token = generateToken(expiryTimeSeconds, userDetails);

        return new Response<>(OK, "Logged in successfully", token);
    }

    public String generateToken(int expiryTimeSeconds, DBUserDetails userDetails) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("encryption.lms.com")
                .issuedAt(now)
                .expiresAt(now.plus(expiryTimeSeconds, ChronoUnit.SECONDS))
                .subject(userDetails.getUsername())
                .claim("authorities", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .claim("username", userDetails.getUsername())
                .build();
        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
