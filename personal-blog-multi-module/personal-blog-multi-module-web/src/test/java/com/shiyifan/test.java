package com.shiyifan;

import com.shiyifan.mapper.BlogMapper;
import com.shiyifan.mapper.CategoryMapper;
import com.shiyifan.mapper.LoginMapper;
import com.shiyifan.pojo.Blog;
import com.shiyifan.pojo.Category;
import com.shiyifan.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.UUID;

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

    @Test
    public void test() throws Exception {
//        Blog blog = new Blog(UUID.randomUUID().toString().replaceAll("-", ""), "多模块测试", "123", "123", new Category(12, "测试", 20, 1, 0, null, null), new User(3, "测试", "132", "cesd", 0, null, null), 0, null, null);
//        System.out.println(blogService.addBlogForAdmin(3, blog));
        blogUtil.deleteBlogInRedisAndElasticSearchForAdmin(3, "72bf6b0a2ad84613889b5ad0b31cb6d4");
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
}
