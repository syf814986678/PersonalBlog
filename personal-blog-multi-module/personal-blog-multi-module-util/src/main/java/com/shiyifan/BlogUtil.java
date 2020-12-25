package com.shiyifan;

import com.shiyifan.mapper.BlogMapper;
import com.shiyifan.mapper.CategoryMapper;
import com.shiyifan.pojo.Blog;
import com.shiyifan.pojo.Category;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ZouCha
 * @name BlogUtil
 * @date 2020-12-05 14:03
 **/
@Component
@Log4j2
public class BlogUtil implements ApplicationRunner {
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ElasticsearchUtil elasticsearchUtil;

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Value("${blog.blogsForAdmin}")
    private String blogsForAdmin;

    @Value("${blog.totalBlogsForAdmin}")
    private String totalBlogsForAdmin;

    @Value("${blog.categoryBlogsForAdmin}")
    private String categoryBlogsForAdmin;

    @Value("${blog.categoryTotalBlogsForAdmin}")
    private String categoryTotalBlogsForAdmin;

    @Value("${blog.blogsForCommon}")
    private String blogsForCommon;

    @Value("${blog.totalBlogsForCommon}")
    private String totalBlogsForCommon;

    @Value("${blog.categoryBlogsForCommon}")
    private String categoryBlogsForCommon;

    @Value("${blog.categoryTotalBlogsForCommon}")
    private String categoryTotalBlogsForCommon;

    @Value("${searchPath}")
    private String searchPath;

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
     * @method getBlogListByPageForCommon
     * @params [start, end]
     **/
    public ArrayList<Blog> getBlogListByPageForCommon(int start, int end) {
        log.info("方法:getBlogListForCommon开始");
        return (ArrayList<Blog>)(Object) redisUtil.lGet(blogsForCommon, start, end);
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
     * @method getCategoryBlogListByPageForCommon
     * @params [categoryId, start, end]
     **/
    public ArrayList<Blog> getCategoryBlogListByPageForCommon(int categoryId, int start, int end) {
        log.info("方法:getCategoryBlogListForCommon开始");
        return (ArrayList<Blog>)(Object) redisUtil.lGet(categoryBlogsForCommon + categoryId, start, end);
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
     * @method getBlogForCommon
     * @params [blogId]
     **/
    public Blog getBlogForCommon(String blogId) {
        log.info("方法:getBlog开始");
        return (Blog) redisUtil.get(blogId);
    }

    /**
     * @author ZouCha
     * @date 2020-12-07 18:49:53
     * @method setBlogForCommon
     * @params [blogId]
     **/
    public void setBlogForCommon(String blogId) throws Exception {
        log.info("方法:setBlog开始");
        try {
            redisUtil.set(blogId, blogMapper.selectBlogByIdForCommon(blogId));
        } catch (Exception e) {
            log.error("setBlog错误" + e.toString());
            throw new Exception("setBlog错误" + e.toString());
        }
    }

    /**
     * @return java.util.ArrayList<java.util.Map < java.lang.String, java.lang.Object>>
     * @author ZouCha
     * @date 2020-12-19 16:03:19
     * @method searchContentByPageForCommon
     * @params [keyword, pageNow, pageSize]
     **/
    public ArrayList<Map<String, Object>> searchContentByPageForCommon(String keyword, int pageNow, int pageSize) throws IOException {
        ArrayList<Map<String, Object>> list = null;
        try {
            list = elasticsearchUtil.searchContentByPageForCommon(keyword, pageNow, pageSize);
            if (redisUtil.get(keyword) == null) {
                redisUtil.set(keyword, 1);
            } else {
                redisUtil.incr(keyword, 1);
                if (Integer.parseInt(String.valueOf(redisUtil.get(keyword))) == 5) {
                    FileWriter fw = new FileWriter(searchPath + "/remote.txt", true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    // 往已有的文件上添加字符串
                    bw.write(keyword + "\n");
                    bw.close();
                    fw.close();
                }
            }
        } catch (IOException e) {
            log.error("searchContentByPageForCommon错误" + e.toString());
            throw new IOException("searchContentByPageForCommon错误");
        }
        return list;
    }

    /**
     * @return void
     * @author ZouCha
     * @date 2020-12-19 16:30:50
     * @method initBlogListForAdmin
     * @params []
     **/
    public void initBlogListForAdmin(int userId) throws Exception {
        log.info("方法:initBlogListForAdmin开始");
        try {
            for (Blog blog : blogMapper.selectBlogListByPageForAdmin(userId, 0)) {
                redisUtil.RSet(blogsForAdmin + userId, blog);
            }
            redisUtil.set(totalBlogsForAdmin + userId, blogMapper.selectTotalBlogsForAdmin(userId, 0));
        } catch (Exception e) {
            log.error("initBlogListForAdmin错误" + e.toString());
            throw new Exception("initBlogListForAdmin错误" + e.toString());
        }
    }

    /**
     * @return void
     * @author ZouCha
     * @date 2020-12-20 12:58:22
     * @method initCategoryBlogListForAdmin
     * @params [userId]
     **/
    public void initCategoryBlogListForAdmin(int userId) throws Exception {
        log.info("方法:initCategoryBlogListForAdmin开始");
        try {
            for (Category category : categoryMapper.selectCategoryForAdmin(userId)) {
                ArrayList<Blog> categoryBlogListForAdmin = blogMapper.selectBlogListByPageForAdmin(userId, category.getCategoryId());
                for (Blog blog : categoryBlogListForAdmin) {
                    redisUtil.RSet(categoryBlogsForAdmin + userId + category.getCategoryId(), blog);
                }
                redisUtil.set(categoryTotalBlogsForAdmin + userId + category.getCategoryId(), blogMapper.selectTotalBlogsForAdmin(userId, category.getCategoryId()));
            }
        } catch (Exception e) {
            log.error("initCategoryBlogListForAdmin错误" + e.toString());
            throw new Exception("initCategoryBlogListForAdmin错误" + e.toString());
        }
    }

    /**
     * @return void
     * @author ZouCha
     * @date 2020-12-17 14:40:22
     * @method getTotalBlogForAdmin
     * @params [userId]
     **/
    public Integer getTotalBlogsForAdmin(int userId) {
        log.info("方法:getTotalBlogsForAdmin开始");
        return (Integer) redisUtil.get(totalBlogsForAdmin + userId);
    }

    /**
     * @return java.util.List<java.lang.Object>
     * @author ZouCha
     * @date 2020-12-19 16:30:08
     * @method getBlogListByPageForAdmin
     * @params [userId, start, end]
     **/
    public ArrayList<Blog> getBlogListByPageForAdmin(int userId, int start, int end) {
        log.info("方法:getBlogListByPageForAdmin开始");
        return (ArrayList<Blog>)(Object) redisUtil.lGet(blogsForAdmin + userId, start, end);
    }

    /**
     * @return void
     * @author ZouCha
     * @date 2020-12-07 18:48:04
     * @method flushDb
     * @params []
     **/
    public void flushDb() {
        log.info("方法:flushDb开始");
        redisUtil.flushDb();
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("方法:flushDb开始");
        redisUtil.flushDb();
    }
}

