package com.shiyifan.dao;

import com.shiyifan.pojo.ElasticSearchBlog;
import com.shiyifan.pojo.Myblog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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
    /*------------------------------登陆后进行的操作-------------------------------*/

    /**
     * 添加博客
     * @author ZouCha
     * @date 2020-11-20 15:15:32
     * @method addBlog
     * @params [myblog]
     * @return void
     *
     **/
    void addBlog(Myblog myblog);

    /**
     * 根据博客ID和用户ID查找博客
     * @author ZouCha
     * @date 2020-11-20 15:15:44
     * @method selectBlogById
     * @params [userid, blogid]
     * @return com.shiyifan.pojo.Myblog
     *
     **/
    Myblog selectBlogById(@Param("userid")int userid,@Param("blogid") String blogid);

    /**
     * 根据博客ID删除博客
     * @author ZouCha
     * @date 2020-11-20 15:16:06
     * @method deleteBlogById
     * @params [userid, blogid]
     * @return void
     *
     **/
    void deleteBlogById(@Param("userid")int userid,@Param("blogid") String blogid);

    /**
     * 更新博客
     * @author ZouCha
     * @date 2020-11-20 15:16:17
     * @method updateBlog
     * @params [myblog]
     * @return void
     *
     **/
    void updateBlog(Myblog myblog);

    /**
     * 分页查询博客(多条)
     * @author ZouCha
     * @date 2020-11-20 15:16:27
     * @method selectBlogAll
     * @params [userid]
     * @return java.util.ArrayList<com.shiyifan.pojo.Myblog>
     *
     **/
    ArrayList<Myblog> selectBlogAll(@Param("userid")int userid);

    /**
     * 分页查询博客(一条)
     * @author ZouCha
     * @date 2020-11-20 15:19:41
     * @method selectBlogForOne
     * @params [userid, blogid]
     * @return com.shiyifan.pojo.Myblog
     *
     **/
    Myblog selectBlogForOne(@Param("userid")int userid,@Param("blogid")String blogid);

    /**
     * 查询总条数
     * @author ZouCha
     * @date 2020-11-20 15:16:38
     * @method selectTotalBlogNums
     * @params [userid]
     * @return int
     *
     **/
    int selectTotalBlogNums(@Param("userid")int userid);

    /**
     * 根据种类ID查找博客
     * @author ZouCha
     * @date 2020-11-20 15:16:48
     * @method selectBlogByCategoryIdAndPage
     * @params [userid, categoryid, pageNow, pageSize]
     * @return java.util.ArrayList<com.shiyifan.pojo.Myblog>
     *
     **/
    ArrayList<Myblog> selectBlogByCategoryIdAndPage(@Param("userid")int userid,@Param("categoryid")int categoryid,@Param("pageNow")int pageNow,@Param("pageSize")int pageSize);

    /*---------------------------------------------------------------------------*/

    /*------------------------------公共操作-------------------------------*/

    /**
     * 根据博客ID查找博客
     * @author ZouCha
     * @date 2020-11-20 15:17:02
     * @method selectBlogByIdForCommon
     * @params [blogid]
     * @return com.shiyifan.pojo.Myblog
     *
     **/
    Myblog selectBlogByIdForCommon(@Param("blogid") String blogid);

    /**
     * 查找最新博客(多条)
     * @author ZouCha
     * @date 2020-11-20 15:17:08
     * @method selectBlogAllForCommon
     * @params [categoryid]
     * @return java.util.ArrayList<com.shiyifan.pojo.Myblog>
     *
     **/
    ArrayList<Myblog> selectBlogAllForCommon(@Param("categoryid")int categoryid);

    /**
     * 查找最新博客(一条)
     * @author ZouCha
     * @date 2020-11-20 15:20:13
     * @method selectBlogForOneForCommon
     * @params [blogid]
     * @return com.shiyifan.pojo.Myblog
     *
     **/
    Myblog selectBlogForOneForCommon(@Param("blogid") String blogid);

    /**
     * 查询全部总条数
     * @author ZouCha
     * @date 2020-11-20 15:17:13
     * @method selectTotalBlogNumsForCommon
     * @params [categoryid]
     * @return int
     *
     **/
    int selectTotalBlogNumsForCommon(@Param("categoryid")int categoryid);

    /**
     * 根据作者查找博客
     * @author ZouCha
     * @date 2020-11-20 15:17:20
     * @method selectBlogByAuthorForCommon
     * @params [userid, pageNow, pageSize]
     * @return java.util.ArrayList<com.shiyifan.pojo.Myblog>
     *
     **/
    ArrayList<Myblog> selectBlogByAuthorForCommon(@Param("userid")int userid,@Param("pageNow")int pageNow,@Param("pageSize")int pageSize);

    /**
     * ElasticSearch查找博客(多条)
     * @author ZouCha
     * @date 2020-11-20 15:17:26
     * @method selectElasticSearchBlogByIdForCommon
     * @params [blogid]
     * @return com.shiyifan.pojo.ElasticSearchBlog
     *
     **/
    ArrayList<ElasticSearchBlog> selectElasticSearchAllBlogForCommon();

    /**
     * ElasticSearch查找博客(一条)
     * @author ZouCha
     * @date 2020-11-20 15:21:00
     * @method selectElasticSearchBlogByIdForCommon
     * @params [blogid]
     * @return com.shiyifan.pojo.ElasticSearchBlog
     *
     **/
    ElasticSearchBlog selectElasticSearchBlogByIdForCommon(@Param("blogid") String blogid);


}
