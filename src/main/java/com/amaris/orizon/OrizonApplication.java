package com.amaris.orizon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class OrizonApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrizonApplication.class, args);
    }

}
