package com.shiyifan.service;

import com.shiyifan.pojo.Myblog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author ZouCha
 * @name BlogService
 * @date 2020-11-20 15:23:33
 *
 **/
public interface BlogService {
    /*------------------------------登陆后进行的操作-------------------------------*/

    /**
     * 添加博客
     * @author ZouCha
     * @date 2020-11-20 15:23:50
     * @method addBlog
     * @params [myblog]
     * @return void
     *
     **/
    void addBlog(Myblog myblog);

    /**
     * 根据博客ID和用户ID查找博客
     * @author ZouCha
     * @date 2020-11-20 15:23:56
     * @method selectBlogById
     * @params [userId, blogId]
     * @return com.shiyifan.pojo.Myblog
     *
     **/
    Myblog selectBlogById(int userId, String blogId);

    /**
     * 根据博客ID删除博客
     * @author ZouCha
     * @date 2020-11-20 15:24:01
     * @method deleteBlogById
     * @params [userId, blogId, categoryId]
     * @return void
     *
     **/
    void deleteBlogById(int userId, String blogId, int categoryId);

    /**
     * 更新博客
     * @author ZouCha
     * @date 2020-11-20 15:24:06
     * @method updateBlog
     * @params [myblog]
     * @return void
     *
     **/
    void updateBlog(Myblog myblog);

    /**
     * 分页查询博客
     * @author ZouCha
     * @date 2020-11-20 15:24:10
     * @method selectBlogByPage
     * @params [userId, pageNow, pageSize]
     * @return java.util.ArrayList<com.shiyifan.pojo.Myblog>
     *
     **/
    ArrayList<Myblog> selectBlogByPage(int userId, int pageNow, int pageSize);

    /**
     * 查询总条数
     * @author ZouCha
     * @date 2020-11-20 15:24:15
     * @method selectTotalBlogNums
     * @params [userId]
     * @return int
     *
     **/
    int selectTotalBlogNums(int userId);

    /**
     * 根据种类ID查找博客
     * @author ZouCha
     * @date 2020-11-20 15:24:20
     * @method selectBlogByCategoryIdAndPage
     * @params [userId, categoryId, pageNow, pageSize]
     * @return java.util.ArrayList<com.shiyifan.pojo.Myblog>
     *
     **/
    ArrayList<Myblog> selectBlogByCategoryIdAndPage(int userId, int categoryId, int pageNow, int pageSize);

    /**
     * 暂存redis
     * @author ZouCha
     * @date 2020-11-20 15:24:25
     * @method setTempBlog
     * @params [myblog]
     * @return java.lang.Boolean
     *
     **/
    Boolean setTempBlog(Myblog myblog);

    /**
     * 读取暂存redis
     * @author ZouCha
     * @date 2020-11-20 15:24:30
     * @method getTempBlog
     * @params [userId]
     * @return com.shiyifan.pojo.Myblog
     *
     **/
    Myblog getTempBlog(int userId);

    /**
     *
     * @author ZouCha
     * @date 2020-11-20 15:24:36
     * @method addElasticsearchBlog
     * @params [blogId]
     * @return void
     *
     **/
    void addElasticsearchBlog(String blogId) throws IOException;

    /**
     *
     * @author ZouCha
     * @date 2020-11-20 15:24:41
     * @method deleteElasticsearchBlog
     * @params [blogId]
     * @return void
     *
     **/
    void deleteElasticsearchBlog(String blogId) throws IOException;

    /**
     *
     * @author ZouCha
     * @date 2020-11-20 15:24:47
     * @method updateElasticsearchBlog
     * @params [blogId]
     * @return void
     *
     **/
    void updateElasticsearchBlog(String blogId) throws IOException;

    /*---------------------------------------------------------------------------*/

    /*------------------------------公共操作-------------------------------*/

    /**
     * 根据博客ID查找博客
     * @author ZouCha
     * @date 2020-11-20 15:24:53
     * @method selectBlogByIdForCommon
     * @params [blogId]
     * @return com.shiyifan.pojo.Myblog
     *
     **/
    Myblog selectBlogByIdForCommon(String blogId);

    /**
     * 分页查询博客selectBlogByPage
     * @author ZouCha
     * @date 2020-11-20 15:24:57
     * @method selectBlogAllByPageForCommon
     * @params [categoryId, pageNow, pageSize]
     * @return java.util.ArrayList<com.shiyifan.pojo.Myblog>
     *
     **/
    ArrayList<Myblog> selectBlogAllByPageForCommon(int categoryId, int pageNow, int pageSize);

    /**
     * 查询全部总条数
     * @author ZouCha
     * @date 2020-11-20 15:25:02
     * @method selectTotalBlogNumsForCommon
     * @params [categoryId]
     * @return int
     *
     **/
    int selectTotalBlogNumsForCommon(int categoryId);

    /**
     * 根据作者查找博客
     * @author ZouCha
     * @date 2020-11-20 15:25:08
     * @method selectBlogByAuthorForCommon
     * @params [userId, pageNow, pageSize]
     * @return java.util.ArrayList<com.shiyifan.pojo.Myblog>
     *
     **/
    ArrayList<Myblog> selectBlogByAuthorForCommon(int userId, int pageNow, int pageSize);

    /*---------------------------------------------------------------------------*/

    /*------------------------------搜索操作-------------------------------*/

    /**
     *
     * @author ZouCha
     * @date 2020-11-20 15:25:23
     * @method searchContentPage
     * @params [keyword, pageNo, pageSize]
     * @return java.util.ArrayList<java.util.Map<java.lang.String,java.lang.Object>>
     *
     **/
    ArrayList<Map<String, Object>> searchContentPage(String keyword, int pageNo, int pageSize) throws IOException;
}
