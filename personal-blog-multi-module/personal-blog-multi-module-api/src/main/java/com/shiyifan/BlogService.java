package com.shiyifan;


import com.shiyifan.pojo.Blog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author ZouCha
 * @name BlogService
 * @date 2020-11-20 15:23:33
 **/
public interface BlogService {
    /**
     * @return java.util.ArrayList<com.shiyifan.pojo.Blog>
     * @author ZouCha
     * @date 2020-12-05 13:37:37
     * @method selectBlogListByPageForCommon
     * @params [categoryId, nums]
     **/
    ArrayList<Blog> selectBlogListByPageForCommon(int categoryId, int pageNow, int pageSize) throws Exception;

    /**
     * @return java.lang.Integer
     * @author ZouCha
     * @date 2020-12-05 15:45:18
     * @method selectTotalBlogsForCommon
     * @params [categoryId]
     **/
    Integer selectTotalBlogsForCommon(int categoryId) throws Exception;

    /**
     * @return com.shiyifan.pojo.Blog
     * @author ZouCha
     * @date 2020-12-07 18:33:09
     * @method selectBlogByIdForCommon
     * @params [blogId]
     **/
    Blog selectBlogByIdForCommon(String blogId) throws Exception;

    /**
     * @return java.util.ArrayList<java.util.Map < java.lang.String, java.lang.Object>>
     * @author ZouCha
     * @date 2020-12-13 11:46:12
     * @method searchContentPageForCommon
     * @params [keyword, pageNo, pageSize]
     **/
    ArrayList<Map<String, Object>> searchContentByPageForCommon(String keyword, int pageNow, int pageSize) throws IOException;

    /**
     * @return java.util.ArrayList<com.shiyifan.pojo.Blog>
     * @author ZouCha
     * @date 2020-12-19 16:04:53
     * @method selectBlogListByPageForAdmin
     * @params [userId, pageNow, pageSize]
     **/
    ArrayList<Blog> selectBlogListByPageForAdmin(int userId, int categoryId, int pageNow, int pageSize) throws Exception;

    /**
     * @return java.lang.Integer
     * @author ZouCha
     * @date 2020-12-20 13:41:05
     * @method selectTotalBlogsForAdmin
     * @params [userId]
     **/
    Integer selectTotalBlogsForAdmin(int userId, int categoryId) throws Exception;

    /**
     * @return void
     * @author ZouCha
     * @date 2020-12-30 09:34:44
     * @method setTempBlogForAdmin
     * @params [userId, blog]
     **/
    void setTempBlogForAdmin(int userId, Blog blog) throws Exception;

    /**
     * @return com.shiyifan.pojo.Blog
     * @author ZouCha
     * @date 2020-12-30 09:40:10
     * @method getTempBlogForAdmin
     * @params [userId]
     **/
    Blog getTempBlogForAdmin(int userId);

    /**
     * @return java.lang.Boolean
     * @author ZouCha
     * @date 2020-12-30 10:06:12
     * @method addBlogForAdmin
     * @params [userId, blog]
     **/
    Boolean addBlogForAdmin(int userId, Blog blog) throws Exception;

    /**
     * @return java.lang.Boolean
     * @author ZouCha
     * @date 2020-12-30 14:50:15
     * @method deleteBlogForAdmin
     * @params [userId, blogId, categoryId]
     **/
    Boolean deleteBlogForAdmin(int userId, String blogId, int categoryId) throws Exception;

    /**
     * @return java.lang.Boolean
     * @author ZouCha
     * @date 2021-01-02 14:00:02
     * @method updateBlogForAdmin
     * @params [userId, blog]
     **/
    Integer updateBlogForAdmin(int userId, Blog blog) throws Exception;

    /**
     * @return com.shiyifan.pojo.Blog
     * @author ZouCha
     * @date 2021-01-02 17:07:11
     * @method selectBlogByIdForAdmin
     * @params [userId, blogId]
     **/
    Blog selectBlogByIdForAdmin(int userId, String blogId) throws Exception;

    /**
     * @return java.lang.Boolean
     * @author user
     * @date 2021-01-05 15:06:17
     * @method updateBlogForAdmin
     * @params [userId, blogId]
     **/
    Boolean updateBlogForAdmin(int userId, String blogId) throws Exception;

    /**
     * @return java.util.ArrayList<java.lang.String>
     * @author user
     * @date 2021-01-05 15:14:29
     * @method selectBlogIdByCategoryIdForAdmin
     * @params [userId, categoryId]
     **/
    ArrayList<String> selectBlogIdByCategoryIdForAdmin(int userId, int categoryId) throws Exception;

    /**
     * @param userId:
     * @param blogId:
     * @return java.lang.String
     * @author 走叉
     * @date 2023-02-22 13:58:06
     * @method downloadBlog2Markdown
     **/
    String downloadBlog2MarkdownForCommon(int userId, String blogId) throws Exception;

}
