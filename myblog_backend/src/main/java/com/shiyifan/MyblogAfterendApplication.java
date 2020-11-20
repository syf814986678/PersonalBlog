package com.shiyifan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
/**
 *
 * @author ZouCha
 * @name MyblogAfterendApplication
 * @date 2020-11-20 15:35:40
 *
 **/
@SpringBootApplication
@EnableRetry
public class MyblogAfterendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyblogAfterendApplication.class, args);
    }
}
