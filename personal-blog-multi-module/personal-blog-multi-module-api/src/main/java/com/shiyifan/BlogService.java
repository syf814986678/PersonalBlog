package com.shiyifan;


import com.shiyifan.pojo.Blog;

/**
 * @author ZouCha
 * @name BlogService
 * @date 2020-11-20 15:23:33
 **/
public interface BlogService {
    /**
     * @return com.shiyifan.pojo.Blog
     * @author ZouCha
     * @date 2020-11-30 15:53:33
     * @method selectBlogByBlogId
     * @params [blogId]
     **/
    Blog selectBlogByBlogId(String blogId);
}
