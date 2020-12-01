package com.shiyifan.mapper;


import com.shiyifan.pojo.Blog;
import org.apache.ibatis.annotations.Mapper;

import org.springframework.stereotype.Repository;


/**
 * @author ZouCha
 * @name BlogMapper
 * @date 2020-11-20 15:15:20
 **/
@Mapper
@Repository
public interface BlogMapper {
    /**
     * @return com.shiyifan.pojo.Blog
     * @author ZouCha
     * @date 2020-11-30 15:53:33
     * @method selectBlogByBlogId
     * @params [blogId]
     **/
    Blog selectBlogByBlogId(String blogId);
}
