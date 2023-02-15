package com.shiyifan;

import com.shiyifan.mapper.CategoryMapper;
import com.shiyifan.pojo.Category;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;

/**
 * @author ZouCha
 * @name CategoryServiceImpl
 * @date 2020-12-05 18:28
 **/
@Service
@Order
@Log4j2
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * @return java.util.ArrayList<com.shiyifan.pojo.Category>
     * @author ZouCha
     * @date 2020-12-05 18:29:31
     * @method selectTopTenCategoryForCommon
     * @params []
     **/
    @Override
    public ArrayList<Category> selectTopTenCategoryForCommon() throws Exception {
        log.info("方法:selectCategoryForCommon开始");
        ArrayList<Category> categories = null;
        try {
            categories = categoryMapper.selectTopTenCategoryForCommon();
        } catch (Exception e) {
            log.error("查找公共类别错误" + e.toString());
            throw new Exception("查找公共类别错误" + e.toString());
        }
        return categories;
    }

    /**
     *
     * @author user
     * @date 2021-01-06 14:26:49
     * @method selectAllCategoryForCommon
     * @params []
     * @return java.util.ArrayList<com.shiyifan.pojo.Category>
     *
     **/
    @Override
    public ArrayList<Category> selectAllCategoryForCommon() throws Exception {
        log.info("方法:selectAllCategoryForCommon开始");
        ArrayList<Category> categories = null;
        try {
            categories = categoryMapper.selectAllCategoryForCommon();
        } catch (Exception e) {
            log.error("selectAllCategoryForCommon错误" + e.toString());
            throw new Exception("selectAllCategoryForCommon错误" + e.toString());
        }
        return categories;
    }

    /**
     * @return java.util.ArrayList<com.shiyifan.pojo.Category>
     * @author ZouCha
     * @date 2021-01-02 18:34:45
     * @method selectCategoryForAdmin
     * @params [userId, pageNow, pageSize]
     **/
    @Override
    public ArrayList<Category> selectCategoryForAdmin(int userId, int pageNow, int pageSize) throws Exception {
        log.info("方法:selectCategoryForAdmin开始,userId:" + userId + ",pageNow:" + pageNow + ",pageSize:" + pageSize);
        ArrayList<Category> categories = null;
        int start = 0;
        int end = 0;
        if (pageSize == 0) {
            end = Integer.MAX_VALUE;
        } else {
            start = (pageNow - 1) * pageSize;
            end = pageSize;
        }
        try {
            categories = categoryMapper.selectCategoryForAdmin(userId, start, end);
        } catch (Exception e) {
            log.error("selectCategoryForAdmin错误" + e.toString());
            throw new Exception("selectCategoryForAdmin错误" + e.toString());
        }
        return categories;
    }

    /**
     * @return java.lang.Integer
     * @author ZouCha
     * @date 2020-12-30 10:23:03
     * @method getCategoryRankForAdmin
     * @params [userId, categoryId]
     **/
    @Override
    public Integer getCategoryRankForAdmin(int userId, int categoryId) throws Exception {
        log.info("方法:getCategoryRankForAdmin开始,userId:" + userId + ",categoryId:" + categoryId);
        Integer categoryRankForAdmin = null;
        try {
            categoryRankForAdmin = categoryMapper.getCategoryRankForAdmin(userId, categoryId);
        } catch (Exception e) {
            log.error("getCategoryRankForAdmin错误" + e.toString());
            throw new Exception("getCategoryRankForAdmin错误" + e.toString());
        }
        return categoryRankForAdmin;
    }

    /**
     * @return void
     * @author ZouCha
     * @date 2020-12-30 10:52:43
     * @method addCategoryRankForAdmin
     * @params [userId, categoryId]
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addCategoryRankForAdmin(int userId, int categoryId) throws Exception {
        log.info("方法:addCategoryRankForAdmin开始,userId:" + userId + ",categoryId:" + categoryId);
        try {
            categoryMapper.addCategoryRankForAdmin(userId, categoryId);
        } catch (Exception e) {
            log.error("addCategoryRankForAdmin错误" + e.toString());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new Exception("addCategoryRankForAdmin错误" + e.toString());
        }
    }

    /**
     * @return void
     * @author ZouCha
     * @date 2020-12-30 15:15:26
     * @method deleteCategoryRankForAdmin
     * @params [userId, categoryId]
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCategoryRankForAdmin(int userId, int categoryId) throws Exception {
        log.info("方法:deleteCategoryRankForAdmin开始,userId:" + userId + ",categoryId:" + categoryId);
        try {
            categoryMapper.deleteCategoryRankForAdmin(userId, categoryId);
        } catch (Exception e) {
            log.error("deleteCategoryRankForAdmin错误" + e.toString());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new Exception("deleteCategoryRankForAdmin错误" + e.toString());
        }
    }

    /**
     * @return java.lang.Integer
     * @author ZouCha
     * @date 2021-01-02 18:35:01
     * @method getCategoryIdForAdmin
     * @params [userId, blogId]
     **/
    @Override
    public Integer getCategoryIdForAdmin(int userId, String blogId) throws Exception {
        log.info("方法:getCategoryIdForAdmin开始,userId:" + userId + ",blogId:" + blogId);
        Integer categoryIdForAdmin = null;
        try {
            categoryIdForAdmin = categoryMapper.getCategoryIdForAdmin(userId, blogId);
        } catch (Exception e) {
            log.error("getCategoryIdForAdmin错误" + e.toString());
            throw new Exception("getCategoryIdForAdmin错误" + e.toString());
        }
        return categoryIdForAdmin;
    }

    /**
     * @return java.lang.Integer
     * @author ZouCha
     * @date 2021-01-02 19:04:57
     * @method getTotalCategoriesForAdmin
     * @params [userId]
     **/
    @Override
    public Integer getTotalCategoriesForAdmin(int userId) throws Exception {
        log.info("方法:getTotalCategoriesForAdmin开始,userId:" + userId);
        Integer totalCategoriesForAdmin = null;
        try {
            totalCategoriesForAdmin = categoryMapper.getTotalCategoriesForAdmin(userId);
        } catch (Exception e) {
            log.error("getTotalCategoriesForAdmin错误" + e.toString());
            throw new Exception("getTotalCategoriesForAdmin错误" + e.toString());
        }
        return totalCategoriesForAdmin;
    }

    /**
     * @return java.lang.Boolean
     * @author user
     * @date 2021-01-05 14:14:48
     * @method addCategoryForAdmin
     * @params [userId, categoryName]
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addCategoryForAdmin(int userId, String categoryName) throws Exception {
        log.info("方法:addCategoryForAdmin开始,userId:" + userId + ",categoryName:" + categoryName);
        try {
            categoryMapper.addCategoryForAdmin(userId, categoryName);
        } catch (Exception e) {
            log.error("addCategoryForAdmin错误" + e.toString());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new Exception("addCategoryForAdmin错误" + e.toString());
        }
        return true;
    }

    /**
     * @return java.lang.Boolean
     * @author user
     * @date 2021-01-05 14:34:59
     * @method deleteCategoryForAdmin
     * @params [userId, categoryId]
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteCategoryForAdmin(int userId, int categoryId) throws Exception {
        log.info("方法:deleteCategoryForAdmin开始,userId:" + userId + ",categoryId:" + categoryId);
        try {
            Integer categoryRankForAdmin = getCategoryRankForAdmin(userId, categoryId);
            if (categoryRankForAdmin != null && categoryRankForAdmin > 0) {
                return false;
            }
            categoryMapper.deleteCategoryForAdmin(userId, categoryId);
        } catch (Exception e) {
            log.error("deleteCategoryForAdmin错误" + e.toString());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new Exception("deleteCategoryForAdmin错误" + e.toString());
        }
        return true;
    }

    /**
     * @return java.lang.Boolean
     * @author user
     * @date 2021-01-05 14:35:06
     * @method updateCategoryForAdmin
     * @params [userId, categoryName, categoryId]
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateCategoryForAdmin(int userId, String categoryName, int categoryId) throws Exception {
        log.info("方法:updateCategoryForAdmin开始,userId:" + userId + ",categoryId:" + categoryId + ",categoryName:" + categoryName);
        try {
            categoryMapper.updateCategoryForAdmin(userId, categoryName, categoryId);
        } catch (Exception e) {
            log.error("updateCategoryForAdmin错误" + e.toString());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new Exception("updateCategoryForAdmin错误" + e.toString());
        }
        return true;
    }


}
