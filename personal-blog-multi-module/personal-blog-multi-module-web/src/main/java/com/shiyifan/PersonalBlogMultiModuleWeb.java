package com.shiyifan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

/**
 * @author ZouCha
 * @name PersonalBlogMultiModuleWeb
 * @date 2020-11-@30 12:30
 **/
@SpringBootApplication
public class PersonalBlogMultiModuleWeb {
    public static void main(String[] args) {
        SpringApplication.run(PersonalBlogMultiModuleWeb.class, args);

    }

}

