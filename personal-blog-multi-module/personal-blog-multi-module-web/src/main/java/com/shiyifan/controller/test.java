package com.shiyifan.controller;

import com.shiyifan.mapper.BlogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZouCha
 * @name test
 * @date 2020-11-30 15:59
 **/
@RestController
public class test {
    @Autowired
    private BlogMapper blogMapper;
    @RequestMapping("/get")
    public String test(){
        return blogMapper.selectBlogByBlogId("02039b6a22164e898e2f235b9e5f9cb3").toString();
    }
}
