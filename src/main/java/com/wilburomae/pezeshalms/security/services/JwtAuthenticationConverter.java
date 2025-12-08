package com.wilburomae.pezeshalms.security.services;

import org.jspecify.annotations.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;
import java.util.Collections;

public class JwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public @Nullable AbstractAuthenticationToken convert(Jwt source) {
        Object authoritiesClaim = source.getClaims().get("authorities");
        Collection<SimpleGrantedAuthority> authorities = Collections.emptyList();
        if (authoritiesClaim instanceof Collection<?> c) {
            authorities = c.stream()
                    .map(Object::toString)
                    .map(SimpleGrantedAuthority::new)
                    .toList();
        }

        return new JwtAuthenticationToken(source, authorities);
    }
}
