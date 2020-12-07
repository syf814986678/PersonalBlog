package com.shiyifan;

import com.shiyifan.mapper.BlogMapper;
import com.shiyifan.mapper.CategoryMapper;
import com.shiyifan.pojo.Blog;
import com.shiyifan.pojo.Category;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZouCha
 * @name BlogUtil
 * @date 2020-12-05 14:03
 **/
@Component
@Log4j2
public class BlogUtil {
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private CategoryMapper categoryMapper;

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

    /**
     * @return void
     * @author ZouCha
     * @date 2020-12-05 14:12:53
     * @method initBlogListForCommon
     * @params []
     **/
    public void initBlogListForCommon() throws Exception {
        log.info("方法:initBlogListForCommon开始");
        try {
            ArrayList<Blog> blogListForCommon = blogMapper.selectBlogListByPageForCommon(0);
            for (Blog blog : blogListForCommon) {
                redisUtil.RSet(blogsForCommon, blog);
            }
            redisUtil.set(totalBlogsForCommon, blogListForCommon.size());
        } catch (Exception e) {
            log.error("初始化公共博客错误" + e.toString());
            throw new Exception("初始化公共博客错误" + e.toString());
        }

    }

    /**
     * @return void
     * @author ZouCha
     * @date 2020-12-05 14:16:16
     * @method initCategoryBlogListForCommon
     * @params []
     **/
    public void initCategoryBlogListForCommon() throws Exception {
        log.info("方法:initCategoryBlogListForCommon开始");
        try {
            for (Category category : categoryMapper.selectCategoryForCommon()) {
                ArrayList<Blog> categoryBlogListForCommon = blogMapper.selectBlogListByPageForCommon(category.getCategoryId());
                for (Blog blog : categoryBlogListForCommon) {
                    redisUtil.RSet(categoryBlogsForCommon + category.getCategoryId(), blog);
                }
                redisUtil.set(categoryTotalBlogsForCommon + category.getCategoryId(), categoryBlogListForCommon.size());
            }
        } catch (Exception e) {
            log.error("初始化公共类别博客错误" + e.toString());
            throw new Exception("初始化公共类别博客错误" + e.toString());
        }
    }

    /**
     * @return java.util.List<java.lang.Object>
     * @author ZouCha
     * @date 2020-12-05 14:34:55
     * @method getBlogListForCommon
     * @params [start, end]
     **/
    public List<Object> getBlogListForCommon(int start, int end) {
        log.info("方法:getBlogListForCommon开始");
        return redisUtil.lGet(blogsForCommon, start, end);
    }

    /**
     * @return Integer
     * @author ZouCha
     * @date 2020-12-05 15:38:40
     * @method getTotalBlogsForCommon
     * @params []
     **/
    public Integer getTotalBlogsForCommon() {
        log.info("方法:getTotalBlogsForCommon开始");
        return (Integer) redisUtil.get(totalBlogsForCommon);
    }

    /**
     * @return java.util.List<java.lang.Object>
     * @author ZouCha
     * @date 2020-12-05 14:35:25
     * @method getCategoryBlogListForCommon
     * @params [categoryId, start, end]
     **/
    public List<Object> getCategoryBlogListForCommon(int categoryId, int start, int end) {
        log.info("方法:getCategoryBlogListForCommon开始");
        return redisUtil.lGet(categoryBlogsForCommon + categoryId, start, end);
    }

    /**
     * @return Integer
     * @author ZouCha
     * @date 2020-12-05 15:40:06
     * @method getCategoryTotalBlogsForCommon
     * @params [categoryId]
     **/
    public Integer getCategoryTotalBlogsForCommon(int categoryId) {
        log.info("方法:getCategoryTotalBlogsForCommon开始");
        return (Integer) redisUtil.get(categoryTotalBlogsForCommon + categoryId);
    }

    /**
     * @return com.shiyifan.pojo.Blog
     * @author ZouCha
     * @date 2020-12-07 18:49:38
     * @method getBlog
     * @params [blogId]
     **/
    public Blog getBlog(String blogId) {
        log.info("方法:getBlog开始");
        return (Blog) redisUtil.get(blogId);
    }

    /**
     * @author ZouCha
     * @date 2020-12-07 18:49:53
     * @method setBlog
     * @params [blogId]
     **/
    public void setBlog(String blogId) {
        log.info("方法:setBlog开始");
        redisUtil.set(blogId, blogMapper.selectBlogByIdForCommon(blogId));
    }

    /**
     * @return void
     * @author ZouCha
     * @date 2020-12-07 18:48:04
     * @method flushDb
     * @params []
     **/
    public void flushDb() {
        redisUtil.flushDb();
    }
}
