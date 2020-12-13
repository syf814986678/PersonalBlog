package com.shiyifan;

import com.shiyifan.mapper.BlogMapper;
import com.shiyifan.mapper.CategoryMapper;
import com.shiyifan.mapper.LoginMapper;
import com.shiyifan.pojo.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    @Autowired
    private BlogUtil blogUtil;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private LoginMapper loginMapper;
    @Autowired
    private BlogService blogService;

//    @Test
//    public void testaliYunUtil(){
//        aliYunUtil.preLoadDcdn();
//        aliYunUtil.refreshDcdn();
//    }
    @Test
    public void testmapper() throws IOException {
        blogService.searchContentByPage("测试", 1, 30);

    }
}
