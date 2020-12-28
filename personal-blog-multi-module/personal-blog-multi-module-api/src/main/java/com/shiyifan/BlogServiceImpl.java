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
import java.util.Map;

/**
 * @author ZouCha
 * @name BlogService
 * @date 2020-11-20 15:23:33
 **/
@Service
@Order
@Log4j2
public class BlogServiceImpl implements BlogService {

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
        log.info("方法:selectBlogListByPageForCommon开始,categoryId:"+categoryId+",pageNow:"+pageNow+",pageSize:"+pageSize);
        ArrayList<Blog> blogListForCommon = new ArrayList<>();
        try {
            for (int i = 1; i <= pageNow; i++) {
                int start = (i - 1) * pageSize;
                int end = (i * pageSize) - 1;
                ArrayList<Blog> blogListInRedis;
                if (categoryId == 0) {
                    blogListInRedis = blogUtil.getBlogListByPageForCommon(start, end);
                    if (blogListInRedis.size() == 0) {
                        blogUtil.initBlogListForCommon();
                        blogListInRedis = blogUtil.getBlogListByPageForCommon(start, end);
                    }
                } else {
                    blogListInRedis = blogUtil.getCategoryBlogListByPageForCommon(categoryId, start, end);
                    if (blogListInRedis.size() == 0) {
                        blogUtil.initCategoryBlogListForCommon();
                        blogListInRedis = blogUtil.getCategoryBlogListByPageForCommon(categoryId, start, end);
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
        log.info("方法:selectTotalBlogsForCommon开始,categoryId:"+categoryId);
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
        log.info("方法:selectBlogByIdForCommon开始,blogId:"+blogId);
        Blog blog = null;
        try {
            blog = blogUtil.getBlogForCommon(blogId);
            if (blog == null) {
                blogUtil.setBlogForCommon(blogId);
                blog = blogUtil.getBlogForCommon(blogId);
            }
        } catch (Exception e) {
            log.error("根据ID查找公共博客错误" + e.toString());
            throw new Exception("根据ID查找公共博客错误" + e.toString());
        }
        return blog;
    }

    /**
     * @return java.util.ArrayList<java.util.Map < java.lang.String, java.lang.Object>>
     * @author ZouCha
     * @date 2020-12-19 16:03:07
     * @method searchContentByPageForCommon
     * @params [keyword, pageNow, pageSize]
     **/
    @Override
    @Retryable(value = Exception.class)
    public ArrayList<Map<String, Object>> searchContentByPageForCommon(String keyword, int pageNow, int pageSize) throws IOException {
        log.info("方法:searchContentByPageForCommon开始,keyword:"+keyword+",pageNow:"+pageNow+",pageSize:"+pageSize);
        ArrayList<Map<String, Object>> list = null;
        try {
            list = blogUtil.searchContentByPageForCommon(keyword, pageNow, pageSize);
        } catch (IOException e) {
            log.error("searchContentByPageForCommon错误" + e.toString());
            throw new IOException("searchContentByPageForCommon错误" + e.toString());
        }
        return list;
    }

    /**
     * @return java.util.ArrayList<com.shiyifan.pojo.Blog>
     * @author ZouCha
     * @date 2020-12-19 16:19:34
     * @method selectBlogByPageForAdmin
     * @params [userId, pageNow, pageSize]
     **/
    @Override
    public ArrayList<Blog> selectBlogListByPageForAdmin(int userId, int categoryId, int pageNow, int pageSize) throws Exception {
        log.info("方法:selectBlogListByPageForAdmin开始,userId:"+userId+",categoryId:"+categoryId+",pageNow:"+pageNow+",pageSize:"+pageSize);
        int start = (pageNow - 1) * pageSize;
        int end = (pageNow * pageSize) - 1;
        ArrayList<Blog> blogs = null;
        try {
            if (categoryId == 0) {
                blogs = blogUtil.getBlogListByPageForAdmin(userId, start, end);
                if (blogs.size() == 0) {
                    blogUtil.initBlogListForAdmin(userId);
                    blogs = blogUtil.getBlogListByPageForAdmin(userId, start, end);
                }
            } else {
                blogs = blogUtil.getCategoryBlogListByPageForAdmin(userId, categoryId, start, end);
                if (blogs.size() == 0) {
                    blogUtil.initCategoryBlogListForAdmin(userId);
                    blogs = blogUtil.getCategoryBlogListByPageForAdmin(userId, categoryId, start, end);
                }
            }
        } catch (Exception e) {
            log.error("selectBlogListByPageForAdmin错误" + e.toString());
            throw new Exception("selectBlogListByPageForAdmin错误" + e.toString());
        }
        return blogs;
    }

    /**
     * @return java.lang.Integer
     * @author ZouCha
     * @date 2020-12-19 16:12:32
     * @method selectTotalBlogsForAdmin
     * @params [userId]
     **/
    @Override
    public Integer selectTotalBlogsForAdmin(int userId, int categoryId) throws Exception {
        log.info("方法:selectTotalBlogsForAdmin开始,userId:"+userId+",categoryId:"+categoryId);
        Integer totalBlogsForAdmin = null;
        try {
            if (categoryId == 0) {
                totalBlogsForAdmin = blogUtil.getTotalBlogsForAdmin(userId);
                if (totalBlogsForAdmin == null) {
                    blogUtil.initBlogListForAdmin(userId);
                    totalBlogsForAdmin = blogUtil.getTotalBlogsForAdmin(userId);
                }
            } else {
                totalBlogsForAdmin = blogUtil.getCategoryTotalBlogsForAdmin(userId, categoryId);
                if (totalBlogsForAdmin == null) {
                    blogUtil.initCategoryBlogListForAdmin(userId);
                    totalBlogsForAdmin = blogUtil.getCategoryTotalBlogsForAdmin(userId, categoryId);
                }
            }
        } catch (Exception e) {
            log.error("selectTotalBlogsForAdmin错误" + e.toString());
            throw new Exception("selectTotalBlogsForAdmin错误" + e.toString());
        }
        return totalBlogsForAdmin;
    }
}
