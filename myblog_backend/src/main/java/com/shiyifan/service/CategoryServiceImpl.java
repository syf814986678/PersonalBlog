package com.shiyifan.service;

import com.shiyifan.dao.CategoryMapper;
import com.shiyifan.pojo.Mycategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
/**
 *
 * @author ZouCha
 * @name CategoryServiceImpl
 * @date 2020-11-20 15:29:45
 *
 **/
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    /**
     *
     * @author ZouCha
     * @date 2020-11-20 15:29:51
     * @method selectAllCategoryByPage
     * @params [userId, pageNow, pageSize]
     * @return java.util.ArrayList<com.shiyifan.pojo.Mycategory>
     *
     **/
    @Override
    public ArrayList<Mycategory> selectAllCategoryByPage(int userId, int pageNow, int pageSize) {
        int start = (pageNow-1)*pageSize;
        return categoryMapper.selectAllCategoryByPage(userId, start, pageSize);
    }
    /**
     *
     * @author ZouCha
     * @date 2020-11-20 15:29:57
     * @method selectAllCategoryForBlog
     * @params [userId]
     * @return java.util.ArrayList<com.shiyifan.pojo.Mycategory>
     *
     **/
    @Override
    public ArrayList<Mycategory> selectAllCategoryForBlog(int userId) {
        return categoryMapper.selectAllCategoryForBlog(userId);
    }
    /**
     *
     * @author ZouCha
     * @date 2020-11-20 15:30:02
     * @method selectTotalCategoryNums
     * @params [userId]
     * @return int
     *
     **/
    @Override
    public int selectTotalCategoryNums(int userId) {
        return categoryMapper.selectTotalCategoryNums(userId);
    }
    /**
     *
     * @author ZouCha
     * @date 2020-11-20 15:30:05
     * @method addCategory
     * @params [userId, categoryName]
     * @return void
     *
     **/
    @Override
    public void addCategory(int userId, String categoryName) {
        categoryMapper.addCategory(userId, categoryName);
    }
    /**
     *
     * @author ZouCha
     * @date 2020-11-20 15:30:09
     * @method selectCategoryById
     * @params [userId, categoryId]
     * @return com.shiyifan.pojo.Mycategory
     *
     **/
    @Override
    public Mycategory selectCategoryById(int userId, int categoryId) {
        return categoryMapper.selectCategoryById(userId, categoryId);
    }
    /**
     *
     * @author ZouCha
     * @date 2020-11-20 15:30:14
     * @method updateCategory
     * @params [userId, categoryName, categoryId]
     * @return void
     *
     **/
    @Override
    public void updateCategory(int userId, String categoryName, int categoryId) {
        categoryMapper.updateCategory(userId, categoryName, categoryId);
    }
    /**
     *
     * @author ZouCha
     * @date 2020-11-20 15:30:19
     * @method deleteCategory
     * @params [userId, categoryId]
     * @return void
     *
     **/
    @Override
    public void deleteCategory(int userId, int categoryId) {
        categoryMapper.deleteCategory(userId, categoryId);
    }
    /**
     *
     * @author ZouCha
     * @date 2020-11-20 15:30:22
     * @method getCategoryRank
     * @params [userId, categoryId]
     * @return int
     *
     **/
    @Override
    public int getCategoryRank(int userId, int categoryId) {
        return categoryMapper.getCategoryRank(userId, categoryId);
    }
    /**
     *
     * @author ZouCha
     * @date 2020-11-20 15:30:31
     * @method selectAllCategoryForCommon
     * @params []
     * @return java.util.ArrayList<com.shiyifan.pojo.Mycategory>
     *
     **/
    @Override
    public ArrayList<Mycategory> selectAllCategoryForCommon() {
        return categoryMapper.selectAllCategoryForCommon();
    }
}
