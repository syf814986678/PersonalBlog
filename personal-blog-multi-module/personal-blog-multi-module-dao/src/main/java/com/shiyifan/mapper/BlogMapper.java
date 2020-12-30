package com.shiyifan.mapper;


import com.shiyifan.pojo.Blog;
import com.shiyifan.pojo.ElasticSearchBlog;
import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;


/**
 * @author ZouCha
 * @name BlogMapper
 * @date 2020-11-20 15:15:20
 **/
@Mapper
@Repository
public interface BlogMapper {
    /**
     * @return java.util.ArrayList<com.shiyifan.pojo.Blog>
     * @author ZouCha
     * @date 2020-12-05 13:39:22
     * @method selectBlogListByPageForCommon
     * @params [categoryId, pageNow, pageSize]
     **/
    ArrayList<Blog> selectBlogListByPageForCommon(@Param("categoryId") int categoryId);

    /**
     * @return com.shiyifan.pojo.Blog
     * @author ZouCha
     * @date 2020-12-07 19:02:47
     * @method selectBlogByIdForCommon
     * @params [blogId]
     **/
    Blog selectBlogByIdForCommon(@Param("blogId") String blogId);

    /**
     * @return java.lang.Integer
     * @author ZouCha
     * @date 2020-12-20 13:26:01
     * @method selectTotalBlogsForAdmin
     * @params [userId, categoryId]
     **/
    Integer selectTotalBlogsForAdmin(@Param("userId") int userId, @Param("categoryId") int categoryId);

    /**
     * @return java.util.ArrayList<com.shiyifan.pojo.Blog>
     * @author ZouCha
     * @date 2020-12-20 12:38:23
     * @method selectBlogListByPageForAdmin
     * @params [categoryId]
     **/
    ArrayList<Blog> selectBlogListByPageForAdmin(@Param("userId") int userId, @Param("categoryId") int categoryId);

    /**
     * @return void
     * @author ZouCha
     * @date 2020-12-30 10:33:30
     * @method addBlogForAdmin
     * @params [userId, blog]
     **/
    void addBlogForAdmin(@Param("userId") int userId, @Param("blog") Blog blog);

    /**
     * @return com.shiyifan.pojo.Blog
     * @author ZouCha
     * @date 2020-12-30 11:00:33
     * @method selectBlogForRedisForAdmin
     * @params [userId, blogId]
     **/
    Blog selectBlogForRedisForAdmin(@Param("userId") int userId, @Param("blogId") String blogId);

    /**
     * @return com.shiyifan.pojo.ElasticSearchBlog
     * @author ZouCha
     * @date 2020-12-30 13:40:43
     * @method selectElasticSearchBlogByIdForAdmin
     * @params [userId, blogId]
     **/
    ElasticSearchBlog selectElasticSearchBlogByIdForAdmin(@Param("userId") int userId, @Param("blogId") String blogId);

    /**
     * @return void
     * @author ZouCha
     * @date 2020-12-30 15:17:40
     * @method deleteBlogByIdForAdmin
     * @params [userId, blogId]
     **/
    void deleteBlogByIdForAdmin(@Param("userId") int userId, @Param("blogId") String blogId);
}
