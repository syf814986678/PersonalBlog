package com.shiyifan.mapper;


import com.shiyifan.pojo.Blog;
import org.apache.ibatis.annotations.Mapper;

import org.springframework.stereotype.Repository;


/**
 *
 * @author ZouCha
 * @name BlogMapper
 * @date 2020-11-20 15:15:20
 *
 **/
@Mapper
@Repository
public interface BlogMapper {
    /**
     *
     * @author ZouCha
     * @date 2020-11-30 15:53:33
     * @method selectBlogByBlogId
     * @params [blogId]
     * @return com.shiyifan.pojo.Blog
     *
     **/
    Blog selectBlogByBlogId(String blogId);
}
