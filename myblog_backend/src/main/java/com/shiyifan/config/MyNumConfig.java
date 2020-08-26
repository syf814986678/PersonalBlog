package com.shiyifan.config;

import com.shiyifan.utils.ArabicNumToChineseNumUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyNumConfig {
    @Bean
    public ArabicNumToChineseNumUtil arabicNumToChineseNumUtil(){
        return new ArabicNumToChineseNumUtil();
    }
}
