package com.shiyifan.dao;

import com.shiyifan.pojo.Mycategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
/**
 *
 * @author ZouCha
 * @name CategoryMapper
 * @date 2020-11-20 15:17:38
 *
 **/
@Mapper
@Repository
public interface CategoryMapper {
    /*------------------------------登陆后进行的操作-------------------------------*/

    /**
     * 分页查询所有种类
     * @author ZouCha
     * @date 2020-11-20 15:17:56
     * @method selectAllCategoryByPage
     * @params [userid, pageNow, pageSize]
     * @return java.util.ArrayList<com.shiyifan.pojo.Mycategory>
     *
     **/
    ArrayList<Mycategory> selectAllCategoryByPage(@Param("userid")int userid, @Param("pageNow")int pageNow, @Param("pageSize")int pageSize);

    /**
     * 查询添加博客种类
     * @author ZouCha
     * @date 2020-11-20 15:18:01
     * @method selectAllCategoryForBlog
     * @params [userid]
     * @return java.util.ArrayList<com.shiyifan.pojo.Mycategory>
     *
     **/
    ArrayList<Mycategory> selectAllCategoryForBlog(@Param("userid")int userid);

    /**
     * 查询总条数
     * @author ZouCha
     * @date 2020-11-20 15:18:06
     * @method selectTotalCategoryNums
     * @params [userid]
     * @return int
     *
     **/
    int selectTotalCategoryNums(@Param("userid")int userid);

    /**
     * 添加种类
     * @author ZouCha
     * @date 2020-11-20 15:18:11
     * @method addCategory
     * @params [userid, categoryname]
     * @return void
     *
     **/
    void addCategory(@Param("userid")int userid,@Param("categoryname")String categoryname);

    /**
     * 根据种类ID和用户ID查找一个种类
     * @author ZouCha
     * @date 2020-11-20 15:18:15
     * @method selectCategoryById
     * @params [userid, categoryid]
     * @return com.shiyifan.pojo.Mycategory
     *
     **/
    Mycategory selectCategoryById(@Param("userid")int userid,@Param("categoryid")int categoryid);

    /**
     * 根据种类ID和用户ID更新种类
     * @author ZouCha
     * @date 2020-11-20 15:18:20
     * @method updateCategory
     * @params [userid, categoryname, categoryid]
     * @return void
     *
     **/
    void updateCategory(@Param("userid")int userid,@Param("categoryname")String categoryname,@Param("categoryid")int categoryid);

    /**
     * 根据种类ID和用户ID删除种类
     * @author ZouCha
     * @date 2020-11-20 15:18:25
     * @method deleteCategory
     * @params [userid, categoryid]
     * @return void
     *
     **/
    void deleteCategory(@Param("userid")int userid,@Param("categoryid")int categoryid);

    /**
     * 根据种类ID和用户ID查找种类Rank
     * @author ZouCha
     * @date 2020-11-20 15:18:32
     * @method getCategoryRank
     * @params [userid, categoryid]
     * @return int
     *
     **/
    int getCategoryRank(@Param("userid")int userid,@Param("categoryid")int categoryid);

    /**
     * 根据博客ID和用户ID获取博客的种类ID
     * @author ZouCha
     * @date 2020-11-20 15:18:36
     * @method getCategoryid
     * @params [userid, blogid]
     * @return int
     *
     **/
    int getCategoryid(@Param("userid")int userid,@Param("blogid")String blogid);

    /**
     * 根据种类ID和用户ID添加权重
     * @author ZouCha
     * @date 2020-11-20 15:18:42
     * @method addCategoryRank
     * @params [userid, categoryid]
     * @return void
     *
     **/
    void addCategoryRank(@Param("userid")int userid,@Param("categoryid")int categoryid);

    /**
     * 根据种类ID和用户ID减少权重
     * @author ZouCha
     * @date 2020-11-20 15:18:49
     * @method deleteCategoryRank
     * @params [userid, categoryid]
     * @return void
     *
     **/
    void deleteCategoryRank(@Param("userid")int userid,@Param("categoryid")int categoryid);

    /*---------------------------------------------------------------------------*/

    /*------------------------------公共操作-------------------------------*/

    /**
     * 查找所有的种类
     * @author ZouCha
     * @date 2020-11-20 15:19:20
     * @method selectAllCategoryForCommon
     * @params []
     * @return java.util.ArrayList<com.shiyifan.pojo.Mycategory>
     *
     **/
    ArrayList<Mycategory> selectAllCategoryForCommon();
}
