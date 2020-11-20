package com.shiyifan.config;

import com.shiyifan.interceptor.MyJwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 * @author ZouCha
 * @name MyWebMvcConfig
 * @date 2020-11-20 14:56:38
 *
 **/
@Configuration
public class MyWebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private MyJwtInterceptor myJwtInterceptor;
    /**
     *
     * @author ZouCha
     * @date 2020-11-20 15:03:56
     * @method addViewControllers
     * @params [registry]
     * @return void
     *
     **/
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/").setViewName("/index.html");
//        registry.addViewController("/index").setViewName("/index.html");
//        registry.addViewController("/login").setViewName("/index.html");
//        registry.addViewController("/admin/**").setViewName("/index.html");
    }
    /**
     *
     * @author ZouCha
     * @date 2020-11-20 15:04:13
     * @method addInterceptors
     * @params [registry]
     * @return void
     *
     **/
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(myJwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/blog/selectLastestBlogByPagForCommon")
                .excludePathPatterns("/blog/selectBlogByCategoryIdAndPageForCommon")
                .excludePathPatterns("/blog/selectBlogByIdForCommon")
                .excludePathPatterns("/category/selectAllCategoryForCommon");
    }
}
