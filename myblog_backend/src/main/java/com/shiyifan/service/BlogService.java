package com.shiyifan.service;

import com.shiyifan.pojo.Myblog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author 81498
 */
public interface BlogService {
    /*------------------------------登陆后进行的操作-------------------------------*/
    //添加博客
    void addBlog(Myblog myblog);

    //根据博客ID和用户ID查找博客
    Myblog selectBlogById(int userId, String blogId);

    //根据博客ID删除博客
    void deleteBlogById(int userId, String blogId, int categoryId);

    //更新博客
    void updateBlog(Myblog myblog);

    //分页查询博客
    ArrayList<Myblog> selectBlogByPage(int userId, int pageNow, int pageSize);

    //查询总条数
    int selectTotalBlogNums(int userId);

    //根据种类ID查找博客
    ArrayList<Myblog> selectBlogByCategoryIdAndPage(int userId, int categoryId, int pageNow, int pageSize);

    //暂存redis
    Boolean setTempBlog(Myblog myblog);

    //读取暂存redis
    Myblog getTempBlog(int userId);

    void addElasticsearchBlog(String blogId) throws IOException;

    void deleteElasticsearchBlog(String blogId) throws IOException;

    void updateElasticsearchBlog(String blogId) throws IOException;

    /*---------------------------------------------------------------------------*/

    /*------------------------------公共操作-------------------------------*/
    //根据博客ID查找博客
    Myblog selectBlogByIdForCommon(String blogId);

    //分页查询博客selectBlogByPage
    ArrayList<Myblog> selectBlogAllByPageForCommon(int categoryId, int pageNow, int pageSize);

    //查询全部总条数
    int selectTotalBlogNumsForCommon(int categoryId);

    //根据作者查找博客
    ArrayList<Myblog> selectBlogByAuthorForCommon(int userId, int pageNow, int pageSize);

    /*---------------------------------------------------------------------------*/

    /*------------------------------搜索操作-------------------------------*/
    ArrayList<Map<String, Object>> searchContentPage(String keyword, int pageNo, int pageSize) throws IOException;
}
