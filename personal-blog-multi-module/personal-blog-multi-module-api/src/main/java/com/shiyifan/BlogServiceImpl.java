package com.shiyifan;


import com.shiyifan.mapper.BlogMapper;
import com.shiyifan.pojo.Blog;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ZouCha
 * @name BlogService
 * @date 2020-11-20 15:23:33
 **/
@Service
@Order
@Log4j2
public class BlogServiceImpl implements BlogService{

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private BlogUtil blogUtil;

    /**
     * 根据种类ID和分页数据查找博客
     * pageNow：必须≥1
     *
     * @return java.util.ArrayList<com.shiyifan.pojo.Blog>
     * @author ZouCha
     * @date 2020-12-05 13:49:46
     * @method selectBlogListByPageForCommon
     * @params [categoryId, pageNow, pageSize]
     **/
    @Override
    public ArrayList<Blog> selectBlogListByPageForCommon(int categoryId, int pageNow, int pageSize) throws Exception {
        log.info("方法:selectBlogListByPageForCommon开始");
        ArrayList<Blog> blogListForCommon = new ArrayList<>();
        try {
            for (int i = 1; i <= pageNow; i++) {
                int start = (i - 1) * pageSize;
                int end = (i * pageSize) - 1;
                List<Object> blogListInRedis;
                if (categoryId == 0) {
                    blogListInRedis = blogUtil.getBlogListForCommon(start, end);
                    if (blogListInRedis.size() == 0) {
                        blogUtil.initBlogListForCommon();
                        blogListInRedis = blogUtil.getBlogListForCommon(start, end);
                    }
                } else {
                    blogListInRedis = blogUtil.getCategoryBlogListForCommon(categoryId, start, end);
                    if (blogListInRedis.size() == 0) {
                        blogUtil.initCategoryBlogListForCommon();
                        blogListInRedis = blogUtil.getCategoryBlogListForCommon(categoryId, start, end);
                    }
                }
                blogListForCommon.addAll((ArrayList<Blog>) (Object) blogListInRedis);
            }
        } catch (Exception e) {
            log.error("分页查找公共博客错误" + e.toString());
            throw new Exception("分页查找公共博客错误" + e.toString());
        }
        return blogListForCommon;
    }

    /**
     * @return java.lang.Integer
     * @author ZouCha
     * @date 2020-12-05 15:45:03
     * @method selectTotalBlogsForCommon
     * @params [categoryId]
     **/
    @Override
    public Integer selectTotalBlogsForCommon(int categoryId) throws Exception {
        log.info("方法:selectTotalBlogsForCommon开始");
        Integer totalBlogsForCommon;
        try {
            if (categoryId == 0) {
                totalBlogsForCommon = blogUtil.getTotalBlogsForCommon();
                if (totalBlogsForCommon == null) {
                    blogUtil.initBlogListForCommon();
                    totalBlogsForCommon = blogUtil.getTotalBlogsForCommon();
                }
            } else {
                totalBlogsForCommon = blogUtil.getCategoryTotalBlogsForCommon(categoryId);
                if (totalBlogsForCommon == null) {
                    blogUtil.initCategoryBlogListForCommon();
                    totalBlogsForCommon = blogUtil.getCategoryTotalBlogsForCommon(categoryId);
                }
            }
        } catch (Exception e) {
            log.error("分页查找公共博客数量错误" + e.toString());
            throw new Exception("分页查找公共博客数量错误" + e.toString());
        }
        return totalBlogsForCommon;
    }

    /**
     * @return com.shiyifan.pojo.Blog
     * @author ZouCha
     * @date 2020-12-07 18:33:34
     * @method selectBlogByIdForCommon
     * @params [blogId]
     **/
    @Override
    public Blog selectBlogByIdForCommon(String blogId) throws Exception {
        log.info("方法:selectBlogByIdForCommon开始");
        Blog blog = null;
        try {
            blog = blogUtil.getBlog(blogId);
            if (blog == null) {
                blogUtil.setBlog(blogId);
                blog = blogUtil.getBlog(blogId);
            }
        } catch (Exception e) {
            log.error("根据ID查找公共博客错误" + e.toString());
            throw new Exception("根据ID查找公共博客错误" + e.toString());
        }
        return blog;
    }

    @Override
    @Retryable(value = Exception.class)
    public ArrayList<Map<String, Object>> searchContentByPage(String keyword, int pageNow, int pageSize) throws IOException {
        log.info("方法:searchContentByPage开始");
        ArrayList<Map<String, Object>> list = null;
        try {
            list = blogUtil.searchContentByPage(keyword, pageNow, pageSize);
        } catch (IOException e) {
            log.error("searchContentByPage错误" + e.toString());
            throw new IOException("searchContentByPage错误" + e.toString());
        }
        return list;
    }
}
