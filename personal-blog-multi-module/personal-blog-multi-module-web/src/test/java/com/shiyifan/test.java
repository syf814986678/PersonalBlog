package com.shiyifan;

import com.shiyifan.mapper.BlogMapper;
import com.shiyifan.mapper.CategoryMapper;
import com.shiyifan.mapper.LoginMapper;
import com.shiyifan.pojo.Blog;
import org.elasticsearch.client.RestHighLevelClient;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

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
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Test
    public void test() throws Exception {


    }

    @Test
    public void testmapper() throws Exception {
        for (int i = 0; i < 3; i++) {
            System.out.println("==========================第" + i + "次============================");
            ArrayList<Blog> blogs = null;
            blogs = blogUtil.getBlogListByPageForAdmin(3, 0, 5);
            if (blogs.size() == 0) {
                blogUtil.initBlogListForAdmin(3);
                blogs = blogUtil.getBlogListByPageForAdmin(3, 0, 5);
            }
            System.out.println(blogs);
            System.out.println("======================================================");
        }

    }

    @Autowired
    private StringEncryptor stringEncryptor;

    @Test
    public void testencoding() {
        String yuanwen1 = "oiLT2d85Er0NfdlZOZaPlktj6EffErcGmDm1js8gFMg6OQh27e0eE99EBoijtHBuN1M+4DVr6eS94RQehbpreQ==";
//        String miwen1 = stringEncryptor.encrypt(yuanwen1) ;
//        System.out.println(miwen1);
        System.out.println(stringEncryptor.decrypt(yuanwen1));

    }
}
