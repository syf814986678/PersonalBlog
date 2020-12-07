package com.shiyifan.mapper;


import com.shiyifan.pojo.Blog;
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
}
