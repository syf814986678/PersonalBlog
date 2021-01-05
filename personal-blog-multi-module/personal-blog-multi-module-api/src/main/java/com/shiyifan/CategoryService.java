package com.shiyifan;


import com.shiyifan.pojo.Category;

import java.util.ArrayList;

/**
 * @author ZouCha
 * @name CategoryService
 * @date 2020-11-20 15:23:33
 **/
public interface CategoryService {
    /**
     * @return java.util.ArrayList<com.shiyifan.pojo.Category>
     * @author ZouCha
     * @date 2020-12-05 18:28:11
     * @method selectCategoryForCommon
     * @params []
     **/
    ArrayList<Category> selectCategoryForCommon() throws Exception;

    /**
     * @return java.util.ArrayList<com.shiyifan.pojo.Category>
     * @author ZouCha
     * @date 2021-01-02 18:34:30
     * @method selectCategoryForAdmin
     * @params [userId, pageNow, pageSize]
     **/
    ArrayList<Category> selectCategoryForAdmin(int userId, int pageNow, int pageSize) throws Exception;

    /**
     * @return java.lang.Integer
     * @author ZouCha
     * @date 2020-12-30 10:22:27
     * @method getCategoryRankForAdmin
     * @params [userId, categoryId]
     **/
    Integer getCategoryRankForAdmin(int userId, int categoryId) throws Exception;

    /**
     * @return void
     * @author ZouCha
     * @date 2020-12-30 10:52:49
     * @method addCategoryRankForAdmin
     * @params [userId, categoryId]
     **/
    void addCategoryRankForAdmin(int userId, int categoryId) throws Exception;

    /**
     * @return void
     * @author ZouCha
     * @date 2020-12-30 15:15:07
     * @method deleteCategoryRankForAdmin
     * @params [userId, categoryId]
     **/
    void deleteCategoryRankForAdmin(int userId, int categoryId) throws Exception;

    /**
     * @return java.lang.Integer
     * @author ZouCha
     * @date 2021-01-02 14:08:16
     * @method getCategoryIdForAdmin
     * @params [userId, blogId]
     **/
    Integer getCategoryIdForAdmin(int userId, String blogId) throws Exception;

    /**
     * @return java.lang.Integer
     * @author ZouCha
     * @date 2021-01-02 19:04:40
     * @method getTotalCategoriesForAdmin
     * @params [userId]
     **/
    Integer getTotalCategoriesForAdmin(int userId) throws Exception;

    /**
     * @return java.lang.Boolean
     * @author user
     * @date 2021-01-05 13:30:36
     * @method addCategoryForAdmin
     * @params [userId, categoryName]
     **/
    Boolean addCategoryForAdmin(int userId, String categoryName) throws Exception;

    /**
     * @return java.lang.Boolean
     * @author user
     * @date 2021-01-05 14:15:18
     * @method deleteCategoryForAdmin
     * @params [userId, categoryId]
     **/
    Boolean deleteCategoryForAdmin(int userId, int categoryId) throws Exception;

    /**
     * @return java.lang.Boolean
     * @author user
     * @date 2021-01-05 14:33:07
     * @method updateCategoryForAdmin
     * @params [userId, categoryName, categoryId]
     **/
    Boolean updateCategoryForAdmin(int userId, String categoryName, int categoryId) throws Exception;
}
