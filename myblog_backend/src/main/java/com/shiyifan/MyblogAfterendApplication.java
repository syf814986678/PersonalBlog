package com.shiyifan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class MyblogAfterendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyblogAfterendApplication.class, args);
    }
}
