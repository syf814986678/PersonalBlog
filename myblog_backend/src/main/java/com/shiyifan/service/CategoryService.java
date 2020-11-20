package com.shiyifan.service;

import com.shiyifan.pojo.Mycategory;

import java.util.ArrayList;
/**
 *
 * @author ZouCha
 * @name CategoryService
 * @date 2020-11-20 15:28:43
 *
 **/
public interface CategoryService {
    /*------------------------------登陆后进行的操作-------------------------------*/

    /**
     * 分页查询所有种类
     * @author ZouCha
     * @date 2020-11-20 15:28:51
     * @method selectAllCategoryByPage
     * @params [userId, pageNow, pageSize]
     * @return java.util.ArrayList<com.shiyifan.pojo.Mycategory>
     *
     **/
    ArrayList<Mycategory> selectAllCategoryByPage(int userId, int pageNow, int pageSize);

    /**
     * 查询添加博客种类
     * @author ZouCha
     * @date 2020-11-20 15:28:55
     * @method selectAllCategoryForBlog
     * @params [userId]
     * @return java.util.ArrayList<com.shiyifan.pojo.Mycategory>
     *
     **/
    ArrayList<Mycategory> selectAllCategoryForBlog(int userId);

    /**
     * 查询总条数
     * @author ZouCha
     * @date 2020-11-20 15:29:00
     * @method selectTotalCategoryNums
     * @params [userId]
     * @return int
     *
     **/
    int selectTotalCategoryNums(int userId);

    /**
     * 添加种类
     * @author ZouCha
     * @date 2020-11-20 15:29:05
     * @method addCategory
     * @params [userId, categoryName]
     * @return void
     *
     **/
    void addCategory(int userId, String categoryName);

    /**
     * 根据种类ID和用户ID查找一个种类
     * @author ZouCha
     * @date 2020-11-20 15:29:10
     * @method selectCategoryById
     * @params [userId, categoryId]
     * @return com.shiyifan.pojo.Mycategory
     *
     **/
    Mycategory selectCategoryById(int userId, int categoryId);

    /**
     * 根据种类ID和用户ID更新种类
     * @author ZouCha
     * @date 2020-11-20 15:29:14
     * @method updateCategory
     * @params [userId, categoryName, categoryId]
     * @return void
     *
     **/
    void updateCategory(int userId, String categoryName, int categoryId);

    /**
     * 根据种类ID和用户ID删除种类
     * @author ZouCha
     * @date 2020-11-20 15:29:19
     * @method deleteCategory
     * @params [userId, categoryId]
     * @return void
     *
     **/
    void deleteCategory(int userId, int categoryId);

    /**
     * 根据种类ID和用户ID查找种类Rank
     * @author ZouCha
     * @date 2020-11-20 15:29:24
     * @method getCategoryRank
     * @params [userId, categoryId]
     * @return int
     *
     **/
    int getCategoryRank(int userId, int categoryId);

    /*---------------------------------------------------------------------------*/

    /*------------------------------公共操作-------------------------------*/

    /**
     * 查找所有的种类
     * @author ZouCha
     * @date 2020-11-20 15:29:34
     * @method selectAllCategoryForCommon
     * @params []
     * @return java.util.ArrayList<com.shiyifan.pojo.Mycategory>
     *
     **/
    ArrayList<Mycategory> selectAllCategoryForCommon();
}
