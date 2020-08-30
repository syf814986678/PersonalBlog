package com.shiyifan.constant;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class MyConstant {
    /*
        阿里云oss
     */
    @Value("${ali.oss.endpoint}")
    private String endpoint;
    @Value("${ali.oss.accessKeyId}")
    private String accessKeyId;
    @Value("${ali.oss.accessKeySecret}")
    private String accessKeySecret;
    @Value("${ali.oss.bucket}")
    private String bucket;

    /*
        JwtToken
     */
    //私钥
    @Value("${JwtToken.key}")
    private String key;
    //过期时间
    @Value("${JwtToken.ttl}")
    private Long ttl;
}
