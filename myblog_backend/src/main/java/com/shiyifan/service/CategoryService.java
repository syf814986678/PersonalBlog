package com.shiyifan.service;

import com.shiyifan.pojo.Mycategory;

import java.util.ArrayList;

public interface CategoryService {
    /*------------------------------登陆后进行的操作-------------------------------*/
    //分页查询所有种类
    ArrayList<Mycategory> selectAllCategoryByPage(int userId, int pageNow, int pageSize);

    //查询添加博客种类
    ArrayList<Mycategory> selectAllCategoryForBlog(int userId);

    //查询总条数
    int selectTotalCategoryNums(int userId);

    //添加种类
    void addCategory(int userId, String categoryName);

    //根据种类ID和用户ID查找一个种类
    Mycategory selectCategoryById(int userId, int categoryId);

    //根据种类ID和用户ID更新种类
    void updateCategory(int userId, String categoryName, int categoryId);

    //根据种类ID和用户ID删除种类
    void deleteCategory(int userId, int categoryId);

    //根据种类ID和用户ID查找种类Rank
    int getCategoryRank(int userId, int categoryId);
    /*---------------------------------------------------------------------------*/
    /*------------------------------公共操作-------------------------------*/
    //查找所有的种类
    ArrayList<Mycategory> selectAllCategoryForCommon();
}
