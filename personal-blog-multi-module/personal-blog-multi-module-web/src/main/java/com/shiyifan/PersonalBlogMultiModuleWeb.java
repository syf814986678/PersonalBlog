package com.shiyifan;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

/**
 * @author ZouCha
 * @name PersonalBlogMultiModuleWeb
 * @date 2020-11-@30 12:30
 **/
@SpringBootApplication
@EnableRetry
public class PersonalBlogMultiModuleWeb {
    public static void main(String[] args) {
        SpringApplication.run(PersonalBlogMultiModuleWeb.class, args);

    }

}

