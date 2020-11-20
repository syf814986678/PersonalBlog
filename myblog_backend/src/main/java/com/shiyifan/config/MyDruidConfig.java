package com.shiyifan.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;

/**
 *
 * @author ZouCha
 * @name MyDruidConfig
 * @date 2020-11-20 14:50:47
 *
 **/
@Configuration
public class MyDruidConfig {
    @ConfigurationProperties(prefix = "spring.datasource")
    /**
     *
     * @author ZouCha
     * @date 2020-11-20 15:01:57
     * @method druidDatasource
     * @params []
     * @return javax.sql.DataSource
     *
     **/
    @Bean
    public DataSource druidDatasource(){
        return new DruidDataSource();
    }
    /**
     *
     * @author ZouCha
     * @date 2020-11-20 15:02:11
     * @method statViewServlet
     * @params []
     * @return org.springframework.boot.web.servlet.ServletRegistrationBean
     *
     **/
    @Bean
    public ServletRegistrationBean statViewServlet(){
        ServletRegistrationBean<StatViewServlet> bean = new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");
        //登录后台账号密码
        HashMap<String, String> initParameters = new HashMap<>();
        //固定格式
        initParameters.put("loginUsername", "admin");
        initParameters.put("loginPassword", "Syf1998.");
        //允许谁可以访问
        initParameters.put("allow", "");
        bean.setInitParameters(initParameters);
        return bean;
    }
}
