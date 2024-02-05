package com.thewildoasis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
public class TheWildOasisApplication {

    public static void main(String[] args) {
        SpringApplication.run(TheWildOasisApplication.class, args);
    }

}
