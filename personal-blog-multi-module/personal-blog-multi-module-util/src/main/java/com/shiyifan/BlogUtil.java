package com.shiyifan;

import com.shiyifan.mapper.BlogMapper;
import com.shiyifan.mapper.CategoryMapper;
import com.shiyifan.pojo.Blog;
import com.shiyifan.pojo.Category;
import com.shiyifan.pojo.ElasticSearchBlog;
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

    @Value("${blog.userTempBlog}")
    private String userTempBlog;

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
            for (Category category : categoryMapper.selectAllCategoryForCommon()) {
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
        log.info("方法:getBlogListForCommon开始,start:" + start + ",end:" + end);
        return (ArrayList<Blog>) (Object) redisUtil.lGet(blogsForCommon, start, end);
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
        log.info("方法:getCategoryBlogListForCommon开始,categoryId:" + categoryId + ",start:" + start + ",end:" + end);
        return (ArrayList<Blog>) (Object) redisUtil.lGet(categoryBlogsForCommon + categoryId, start, end);
    }

    /**
     * @return Integer
     * @author ZouCha
     * @date 2020-12-05 15:40:06
     * @method getCategoryTotalBlogsForCommon
     * @params [categoryId]
     **/
    public Integer getCategoryTotalBlogsForCommon(int categoryId) {
        log.info("方法:getCategoryTotalBlogsForCommon开始,categoryId:" + categoryId);
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
        log.info("方法:getBlogForCommon开始,blogId:" + blogId);
        return (Blog) redisUtil.get(blogId);
    }

    /**
     * @author ZouCha
     * @date 2020-12-07 18:49:53
     * @method setBlogForCommon
     * @params [blogId]
     **/
    public void setBlogForCommon(String blogId) throws Exception {
        log.info("方法:setBlogForCommon开始,blogId:" + blogId);
        try {
            redisUtil.set(blogId, blogMapper.selectBlogByIdForCommon(blogId));
        } catch (Exception e) {
            log.error("setBlogForCommon错误" + e.toString());
            throw new Exception("setBlogForCommon错误" + e.toString());
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
        log.info("方法:searchContentByPageForCommon开始,keyword:" + keyword + ",pageNow:" + pageNow + ",pageSize:" + pageSize);
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
        log.info("方法:initBlogListForAdmin开始,userId:" + userId);
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
        log.info("方法:initCategoryBlogListForAdmin开始,:userId:" + userId);
        try {
            for (Category category : categoryMapper.selectCategoryForAdmin(userId,0,Integer.MAX_VALUE)) {
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
        log.info("方法:getBlogListByPageForAdmin开始,userId:" + userId + ",start:" + start + ",end:" + end);
        return (ArrayList<Blog>) (Object) redisUtil.lGet(blogsForAdmin + userId, start, end);
    }

    /**
     * @return java.lang.Integer
     * @author ZouCha
     * @date 2020-12-27 15:12:21
     * @method getCategoryTotalBlogsForAdmin
     * @params [userId, categoryId]
     **/
    public Integer getCategoryTotalBlogsForAdmin(int userId, int categoryId) {
        log.info("方法:getCategoryTotalBlogsForAdmin开始,userId:" + userId + ",categoryId:" + categoryId);
        return (Integer) redisUtil.get(categoryTotalBlogsForAdmin + userId + categoryId);
    }

    /**
     * @return java.util.ArrayList<com.shiyifan.pojo.Blog>
     * @author ZouCha
     * @date 2020-12-27 15:16:15
     * @method getCategoryBlogListByPageForAdmin
     * @params [userId, categoryId, start, end]
     **/
    public ArrayList<Blog> getCategoryBlogListByPageForAdmin(int userId, int categoryId, int start, int end) {
        log.info("方法:getCategoryBlogListByPageForAdmin开始,userId:" + userId + ",categoryId:" + categoryId + ",start:" + start + ",end"+ end);
        return (ArrayList<Blog>) (Object) redisUtil.lGet(categoryBlogsForAdmin + userId + categoryId, start, end);
    }

    /**
     * @return void
     * @author ZouCha
     * @date 2020-12-30 09:44:28
     * @method setTempBlogForAdmin
     * @params [blog]
     **/
    public void setTempBlogForAdmin(int userId, Blog blog) throws Exception {
        log.info("方法:setTempBlogForAdmin开始,userId:" + userId+",blogId:" + blog.getBlogId());
        try {
            redisUtil.set(userTempBlog + userId, blog);
        } catch (Exception e) {
            log.error("setTempBlogForAdmin错误" + e.toString());
            throw new Exception("setTempBlogForAdmin错误" + e.toString());
        }
    }

    /**
     * @return com.shiyifan.pojo.Blog
     * @author ZouCha
     * @date 2020-12-30 09:38:18
     * @method getTempBlogForAdmin
     * @params [userId]
     **/
    public Blog getTempBlogForAdmin(int userId) {
        log.info("方法:getTempBlogForAdmin开始,userId:" + userId);
        return (Blog) redisUtil.get(userTempBlog + userId);
    }

    /**
     * @return void
     * @author ZouCha
     * @date 2020-12-30 11:04:17
     * @method setBlogToRedisAndElasticSearchForAdmin
     * @params [userId, blog]
     **/
    public void setBlogToRedisAndElasticSearchForAdmin(int userId, String blogId) throws Exception {
        log.info("方法:setBlogToRedisAndElasticSearchForAdmin开始,userId:" + userId);
        try {
            ElasticSearchBlog elasticSearchBlog = blogMapper.selectElasticSearchBlogByIdForAdmin(userId, blogId);
            elasticsearchUtil.addElasticsearchBlogForAdmin(elasticSearchBlog);
            Blog blog = blogMapper.selectBlogForRedisForAdmin(userId, blogId);
            redisUtil.lSet(blogsForAdmin + userId, blog);
            redisUtil.incr(totalBlogsForAdmin + userId, 1);
            redisUtil.lSet(categoryBlogsForAdmin + userId + blog.getCategory().getCategoryId(), blog);
            redisUtil.incr(categoryTotalBlogsForAdmin + userId + blog.getCategory().getCategoryId(), 1);
            redisUtil.lSet(blogsForCommon, blog);
            redisUtil.incr(totalBlogsForCommon, 1);
            redisUtil.lSet(categoryBlogsForCommon + blog.getCategory().getCategoryId(), blog);
            redisUtil.incr(categoryTotalBlogsForCommon + blog.getCategory().getCategoryId(), 1);
        } catch (Exception e) {
            log.error("setBlogToRedisAndElasticSearchForAdmin错误" + e.toString());
            throw new Exception("setBlogToRedisAndElasticSearchForAdmin错误" + e.toString());
        }
    }

    /**
     * @return void
     * @author ZouCha
     * @date 2021-01-02 15:13:55
     * @method deleteBlogInRedisAndElasticSearchForAdmin
     * @params [userId, blogId]
     **/
    public void deleteBlogInRedisAndElasticSearchForAdmin(int userId, String blogId) throws Exception {
        log.info("方法:deleteBlogInRedisAndElasticSearchForAdmin开始,userId:" + userId);
        try {
            elasticsearchUtil.deleteElasticsearchBlogForAdmin(blogId);
            Blog blog = blogMapper.selectBlogForRedisForAdmin(userId, blogId);
            redisUtil.del(blogId);
            redisUtil.lRemove(blogsForCommon, 1, blog);
            redisUtil.decr(totalBlogsForCommon, 1);
            redisUtil.lRemove(categoryBlogsForCommon + blog.getCategory().getCategoryId(), 1, blog);
            redisUtil.decr(categoryTotalBlogsForCommon + blog.getCategory().getCategoryId(), 1);
            redisUtil.lRemove(blogsForAdmin + userId, 1, blog);
            redisUtil.decr(totalBlogsForAdmin + userId, 1);
            redisUtil.lRemove(categoryBlogsForAdmin + userId + blog.getCategory().getCategoryId(), 1, blog);
            redisUtil.decr(categoryTotalBlogsForAdmin + userId + blog.getCategory().getCategoryId(), 1);
            if (getTotalBlogsForCommon() < 0) {
                redisUtil.set(totalBlogsForCommon, 0);
            }
            if (getCategoryTotalBlogsForCommon(blog.getCategory().getCategoryId()) < 0) {
                redisUtil.set(totalBlogsForCommon, 0);
            }
            if (getTotalBlogsForAdmin(userId) < 0) {
                redisUtil.set(totalBlogsForAdmin + userId, 0);
            }
            if (getCategoryTotalBlogsForAdmin(userId, blog.getCategory().getCategoryId()) < 0) {
                redisUtil.set(categoryTotalBlogsForAdmin + userId + blog.getCategory().getCategoryId(), 0);
            }
        } catch (Exception e) {
            log.error("deleteBlogInRedisAndElasticSearchForAdmin错误" + e.toString());
            throw new Exception("deleteBlogInRedisAndElasticSearchForAdmin错误" + e.toString());
        }
    }

    /**
     * @return void
     * @author ZouCha
     * @date 2021-01-02 16:50:46
     * @method updateBlogInRedisAndElasticSearchForAdmin
     * @params [userId, blog]
     **/
    public void updateBlogInRedisAndElasticSearchForAdmin(int userId, Blog blog) throws Exception {
        log.info("方法:updateBlogInRedisAndElasticSearchForAdmin开始,userId:" + userId+",blogId:" + blog.getBlogId());
        ArrayList<Blog> blogList = null;
        try {
            ElasticSearchBlog elasticSearchBlog = blogMapper.selectElasticSearchBlogByIdForAdmin(userId, blog.getBlogId());
            elasticsearchUtil.updateElasticsearchBlogForAdmin(elasticSearchBlog);

            redisUtil.del(blog.getBlogId());

            int index = 0;
            blogList = getBlogListByPageForCommon(0, -1);
            for (Blog blogInList : blogList) {
                if (blogInList.getBlogId().equals(blog.getBlogId())) {
                    redisUtil.lUpdateIndex(blogsForCommon, index, blog);
                    break;
                }
                index++;
            }

            index = 0;
            blogList = getCategoryBlogListByPageForCommon(blog.getCategory().getCategoryId(), 0, -1);
            for (Blog blogInList : blogList) {
                if (blogInList.getBlogId().equals(blog.getBlogId())) {
                    redisUtil.lUpdateIndex(categoryBlogsForCommon + blogInList.getCategory().getCategoryId(), index, blog);
                    break;
                }
                index++;
            }

            index = 0;
            blogList = getBlogListByPageForAdmin(userId, 0, -1);
            for (Blog blogInList : blogList) {
                if (blogInList.getBlogId().equals(blog.getBlogId())) {
                    redisUtil.lUpdateIndex(blogsForAdmin + userId, index, blog);
                    break;
                }
                index++;
            }

            index = 0;
            blogList = getCategoryBlogListByPageForAdmin(userId, blog.getCategory().getCategoryId(), 0, -1);
            for (Blog blogInList : blogList) {
                if (blogInList.getBlogId().equals(blog.getBlogId())) {
                    redisUtil.lUpdateIndex(categoryBlogsForAdmin + userId + blog.getCategory().getCategoryId(), index, blog);
                    break;
                }
                index++;
            }

        } catch (Exception e) {
            log.error("updateBlogInRedisAndElasticSearchForAdmin错误" + e);
            throw new Exception("updateBlogInRedisAndElasticSearchForAdmin错误" + e);
        }

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

