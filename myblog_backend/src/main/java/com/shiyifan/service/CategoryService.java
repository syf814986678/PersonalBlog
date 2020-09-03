package com.shiyifan.service;

import com.shiyifan.pojo.Mycategory;

import java.util.ArrayList;

public interface CategoryService {
    /*------------------------------登陆后进行的操作-------------------------------*/
    //分页查询所有种类
    ArrayList<Mycategory> selectAllCategoryByPage(int userid, int pageNow, int pageSize);

    //查询添加博客种类
    ArrayList<Mycategory> selectAllCategoryForBlog(int userid);

    //查询总条数
    int selectTotalCategoryNums(int userid);

    //添加种类
    void addCategory(int userid,String categoryname);

    //根据种类ID和用户ID查找一个种类
    Mycategory selectCategoryById(int userid,int categoryid);

    //根据种类ID和用户ID更新种类
    void updateCategory(int userid,String categoryname,int categoryid);

    //根据种类ID和用户ID删除种类
    void deleteCategory(int userid,int categoryid);

    //根据种类ID和用户ID查找种类Rank
    int getCategoryRank(int userid,int categoryid);
    /*---------------------------------------------------------------------------*/
    /*------------------------------公共操作-------------------------------*/
    //查找所有的种类
    ArrayList<Mycategory> selectAllCategoryForCommon();
}
