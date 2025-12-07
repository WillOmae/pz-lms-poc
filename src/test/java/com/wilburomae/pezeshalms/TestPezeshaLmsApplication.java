package com.wilburomae.pezeshalms;

import org.springframework.boot.SpringApplication;

import java.util.Locale;
import java.util.TimeZone;

public class TestPezeshaLmsApplication {

    public static void main(String[] args) {
        Locale.setDefault(Locale.of("en", "KE"));
        TimeZone.setDefault(TimeZone.getTimeZone("Africa/Nairobi"));
        SpringApplication.from(PezeshaLmsApplication::main).with(TestcontainersConfiguration.class).run(args);
    }
}
