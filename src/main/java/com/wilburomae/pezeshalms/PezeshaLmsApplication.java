package com.wilburomae.pezeshalms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Locale;
import java.util.TimeZone;

@SpringBootApplication
@EnableJpaAuditing
public class PezeshaLmsApplication {

    public static void main(String[] args) {
        Locale.setDefault(Locale.of("en", "KE"));
        TimeZone.setDefault(TimeZone.getTimeZone("Africa/Nairobi"));
        SpringApplication.run(PezeshaLmsApplication.class, args);
    }
}
