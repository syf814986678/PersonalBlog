package com.shiyifan.constant;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author ZouCha
 * @name MyConstant
 * @date 2020-11-20 15:04:49
 **/
@Component
@Data
public class MyConstant {
    /**
     * 阿里云oss
     */
    @Value("${ali.oss.endpoint}")
    private String endpoint;
    @Value("${ali.oss.accessKeyId}")
    private String accessKeyId;
    @Value("${ali.oss.accessKeySecret}")
    private String accessKeySecret;
    @Value("${ali.oss.bucket}")
    private String bucket;

    /**
     * JwtToken
     */
    //私钥
    @Value("${JwtToken.key}")
    private String key;
    //过期时间
    @Value("${JwtToken.ttl}")
    private Long ttl;

    /**
     * redis博客标识
     */
    @Value("${blog.userTempBlog}")
    private String userTempBlog;
    @Value("${blog.userBlogs}")
    private String userBlogs;
    @Value("${blog.userTotalBlogs}")
    private String userTotalBlogs;
    @Value("${blog.blogsForCommon}")
    private String blogsForCommon;
    @Value("${blog.totalBlogsForCommon}")
    private String totalBlogsForCommon;
    @Value("${blog.categoryBlogsForCommon}")
    private String categoryBlogsForCommon;
    @Value("${blog.categoryTotalBlogsForCommon}")
    private String categoryTotalBlogsForCommon;
}
