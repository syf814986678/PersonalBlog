package com.shiyifan.dao;

import com.shiyifan.pojo.Mycategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Mapper
@Repository
public interface CategoryMapper {
    /*------------------------------登陆后进行的操作-------------------------------*/
    //分页查询所有种类
    ArrayList<Mycategory> selectAllCategoryByPage(@Param("userid")int userid, @Param("pageNow")int pageNow, @Param("pageSize")int pageSize);

    //查询添加博客种类
    ArrayList<Mycategory> selectAllCategoryForBlog(@Param("userid")int userid);

    //查询总条数
    int selectTotalCategoryNums(@Param("userid")int userid);

    //添加种类
    void addCategory(@Param("userid")int userid,@Param("categoryname")String categoryname);

    //根据种类ID和用户ID查找一个种类
    Mycategory selectCategoryById(@Param("userid")int userid,@Param("categoryid")int categoryid);

    //根据种类ID和用户ID更新种类
    void updateCategory(@Param("userid")int userid,@Param("categoryname")String categoryname,@Param("categoryid")int categoryid);

    //根据种类ID和用户ID删除种类
    void deleteCategory(@Param("userid")int userid,@Param("categoryid")int categoryid);

    //根据种类ID和用户ID查找种类Rank
    int getCategoryRank(@Param("userid")int userid,@Param("categoryid")int categoryid);

    //根据博客ID和用户ID获取博客的种类ID
    int getCategoryid(@Param("userid")int userid,@Param("blogid")String blogid);

    //根据种类ID和用户ID添加权重
    void addCategoryRank(@Param("userid")int userid,@Param("categoryid")int categoryid);

    //根据种类ID和用户ID减少权重
    void deleteCategoryRank(@Param("userid")int userid,@Param("categoryid")int categoryid);
    /*---------------------------------------------------------------------------*/
    /*------------------------------公共操作-------------------------------*/
    //查找所有的种类
    ArrayList<Mycategory> selectAllCategoryForCommon();
}
