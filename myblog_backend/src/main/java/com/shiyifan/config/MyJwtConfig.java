package com.shiyifan.config;

import com.shiyifan.utils.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyJwtConfig {
    @Bean
    public JwtUtil jwtUtil(){
        return new JwtUtil();
    }
}
