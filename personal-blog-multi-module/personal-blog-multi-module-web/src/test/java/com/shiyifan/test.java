package com.shiyifan;

import com.shiyifan.mapper.BlogMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author ZouCha
 * @name test
 * @date 2020-12-01 12:15
 **/
@SpringBootTest
public class test {
    @Autowired
    private AliYunUtil aliYunUtil;
    @Autowired
    private BlogMapper blogMapper;

    @Test
    public void testaliYunUtil(){
        aliYunUtil.preLoadDcdn();
        aliYunUtil.refreshDcdn();
    }
    @Test
    public void testmapper(){
        System.out.println(blogMapper.selectBlogByBlogId("02039b6a22164e898e2f235b9e5f9cb3").toString());
    }
}
