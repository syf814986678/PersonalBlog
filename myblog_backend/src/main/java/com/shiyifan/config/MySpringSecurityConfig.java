package com.shiyifan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

/**
 *
 * @author ZouCha
 * @name MySpringSecurityConfig
 * @date 2020-11-20 14:53:51
 *
 **/
@Configuration
@EnableWebSecurity
public class MySpringSecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     *
     * @author ZouCha
     * @date 2020-11-20 15:03:07
     * @method configure
     * @params [http]
     * @return void
     *
     **/
    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("/**").permitAll();
//        http.headers().contentTypeOptions().disable();
//        http.csrf().disable().authorizeRequests()
//                .antMatchers("/static/**").permitAll().anyRequest().authenticated();
        http.authorizeRequests()
                .antMatchers("/**").permitAll().anyRequest().authenticated()
        .and().csrf().disable();
    }
    /**
     *
     * @author ZouCha
     * @date 2020-11-20 15:03:16
     * @method allowUrlEncodedSlashHttpFirewall
     * @params []
     * @return org.springframework.security.web.firewall.HttpFirewall
     *
     **/
    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
//此处开启不让检测‘/’符号
        firewall.setAllowUrlEncodedSlash(true);
        return firewall;
    }
    /**
     *
     * @author ZouCha
     * @date 2020-11-20 15:03:22
     * @method getBcryptPasswordEncoder
     * @params []
     * @return org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
     *
     **/
     @Bean
    public BCryptPasswordEncoder getBcryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
    /**
     *
     * @author ZouCha
     * @date 2020-11-20 15:03:29
     * @method configure
     * @params [web]
     * @return void
     *
     **/
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.httpFirewall(allowUrlEncodedSlashHttpFirewall());
//        super.configure(web);
    }
}
