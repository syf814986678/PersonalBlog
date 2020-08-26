package com.shiyifan.dao;

import com.shiyifan.pojo.Myblog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BlogMapper {
    /*------------------------------登陆后进行的操作-------------------------------*/
    //添加博客
    void addBlog(Myblog myblog);

    //根据博客ID和用户ID查找博客
    Myblog selectBlogById(@Param("userid")int userid,@Param("blogid") String blogid);

    //根据博客ID删除博客
    void deleteBlogById(@Param("userid")int userid,@Param("blogid") String blogid);

    //更新博客
    void updateBlog(Myblog myblog);

    //分页查询博客
    List<Myblog> selectBlogAll(@Param("userid")int userid);
    Myblog selectBlogForOne(@Param("userid")int userid,@Param("blogid")String blogid);

    //查询总条数
    int selectTotalBlogNums(@Param("userid")int userid);

    //根据种类ID查找博客
    List<Myblog> selectBlogByCategoryIdAndPage(@Param("userid")int userid,@Param("categoryid")int categoryid,@Param("pageNow")int pageNow,@Param("pageSize")int pageSize);

    /*---------------------------------------------------------------------------*/

    /*------------------------------公共操作-------------------------------*/
    //根据博客ID查找博客
    Myblog selectBlogByIdForCommon(@Param("blogid") String blogid);

    //查找最新博客
    List<Myblog> selectBlogAllForCommon(@Param("categoryid")int categoryid);
    Myblog selectBlogForOneForCommon(@Param("blogid") String blogid);

    //查询全部总条数
    int selectTotalBlogNumsForCommon(@Param("categoryid")int categoryid);

    //根据作者查找博客
    List<Myblog> selectBlogByAuthorForCommon(@Param("userid")int userid,@Param("pageNow")int pageNow,@Param("pageSize")int pageSize);
}
