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
     * @method searchContentPage
     * @params [keyword, pageNo, pageSize]
     **/
    ArrayList<Map<String, Object>> searchContentByPage(String keyword, int pageNow, int pageSize) throws IOException;
}
