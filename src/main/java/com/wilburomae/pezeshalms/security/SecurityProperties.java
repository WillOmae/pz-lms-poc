package com.wilburomae.pezeshalms.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "application.security")
public class SecurityProperties {

    // CORS
    private List<String> allowedHeaders;
    private List<String> allowedMethods;
    private List<String> allowedOrigins;
    private boolean allowCredentials;
    private long maxAge;
    // token
    private int tokenExpiryTimeSeconds;
}
