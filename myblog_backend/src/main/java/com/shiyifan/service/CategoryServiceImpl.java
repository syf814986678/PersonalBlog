package com.shiyifan.service;

import com.shiyifan.dao.CategoryMapper;
import com.shiyifan.pojo.Mycategory;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
/**
 *
 * @author ZouCha
 * @name CategoryServiceImpl
 * @date 2020-11-20 15:29:45
 *
 **/
@Service
@Order
@Log4j2
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
        ArrayList<Mycategory> mycategories=null;
        try {
            mycategories=categoryMapper.selectAllCategoryByPage(userId, start, pageSize);
        }
        catch (Exception e){
            log.error(e);
        }
        return mycategories;
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
        ArrayList<Mycategory> mycategories = null;
        try {
            mycategories = categoryMapper.selectAllCategoryForBlog(userId);
        }
        catch (Exception e){
            log.error(e);
        }
        return mycategories;
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
        try {
            return categoryMapper.selectTotalCategoryNums(userId);
        }
        catch (Exception e){
            log.error(e);
            return -1;
        }

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
    @Transactional(rollbackFor = Exception.class)
    public Boolean addCategory(int userId, String categoryName) {
        try {
            categoryMapper.addCategory(userId, categoryName);
            return true;
        }
        catch (Exception e){
            log.error(e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
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
        Mycategory mycategory=null;
        try {
            mycategory=categoryMapper.selectCategoryById(userId, categoryId);
        }
        catch (Exception e){
            log.error(e);
        }
        return mycategory;
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
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateCategory(int userId, String categoryName, int categoryId) {
        try {
            categoryMapper.updateCategory(userId, categoryName, categoryId);
            return true;
        }
        catch (Exception e){
            log.error(e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
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
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteCategory(int userId, int categoryId) {
        try {
            categoryMapper.deleteCategory(userId, categoryId);
            return true;
        }
        catch (Exception e){
            log.error(e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
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
        try {
            return categoryMapper.getCategoryRank(userId, categoryId);
        }
        catch (Exception e){
            return -1;
        }

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
        ArrayList<Mycategory> mycategories=null;
        try {
            mycategories=categoryMapper.selectAllCategoryForCommon();
        }
        catch (Exception e){
            log.error(e);
        }
        return mycategories;
    }
}
