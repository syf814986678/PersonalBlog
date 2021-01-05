package com.shiyifan.mapper;


import com.shiyifan.pojo.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;


/**
 * @author ZouCha
 * @name BlogMapper
 * @date 2020-11-20 15:15:20
 **/
@Mapper
@Repository
public interface CategoryMapper {
    /**
     * @return java.util.ArrayList<com.shiyifan.pojo.Category>
     * @author ZouCha
     * @date 2020-12-05 16:12:00
     * @method selectCategoryForCommon
     * @params []
     **/
    ArrayList<Category> selectCategoryForCommon();

    /**
     * @return java.util.ArrayList<com.shiyifan.pojo.Category>
     * @author ZouCha
     * @date 2021-01-02 18:39:41
     * @method selectCategoryForAdmin
     * @params [userId, pageNow, pageSize]
     **/
    ArrayList<Category> selectCategoryForAdmin(@Param("userId") int userId, @Param("pageNow") int pageNow, @Param("pageSize") int pageSize);

    /**
     * @return java.lang.Integer
     * @author ZouCha
     * @date 2020-12-30 10:22:00
     * @method getCategoryRankForAdmin
     * @params [userId, categoryId]
     **/
    Integer getCategoryRankForAdmin(@Param("userId") int userId, @Param("categoryId") int categoryId);

    /**
     * @return void
     * @author ZouCha
     * @date 2020-12-30 10:48:55
     * @method addCategoryRankForAdmin
     * @params [userId, categoryId]
     **/
    void addCategoryRankForAdmin(@Param("userId") int userId, @Param("categoryId") int categoryId);

    /**
     * @return void
     * @author ZouCha
     * @date 2020-12-30 15:13:46
     * @method deleteCategoryRankForAdmin
     * @params [userId, categoryId]
     **/
    void deleteCategoryRankForAdmin(@Param("userId") int userId, @Param("categoryId") int categoryId);

    /**
     * @return java.lang.Integer
     * @author ZouCha
     * @date 2021-01-02 14:07:25
     * @method getCategoryIdForAdmin
     * @params [userId, blogId]
     **/
    Integer getCategoryIdForAdmin(@Param("userId") int userId, @Param("blogId") String blogId);

    /**
     * @return java.lang.Integer
     * @author ZouCha
     * @date 2021-01-02 19:03:21
     * @method getTotalCategoriesForAdmin
     * @params [userId]
     **/
    Integer getTotalCategoriesForAdmin(@Param("userId") int userId);

    /**
     * @return void
     * @author user
     * @date 2021-01-05 13:33:48
     * @method addCategoryForAdmin
     * @params [userid, categoryName]
     **/
    void addCategoryForAdmin(@Param("userId") int userId, @Param("categoryName") String categoryName);

    /**
     * @return void
     * @author user
     * @date 2021-01-05 14:13:53
     * @method deleteCategoryForAdmin
     * @params [userId, categoryId]
     **/
    void deleteCategoryForAdmin(@Param("userId") int userId, @Param("categoryId") int categoryId);

    /**
     * @return void
     * @author user
     * @date 2021-01-05 14:31:17
     * @method updateCategoryForAdmin
     * @params [userId, categoryName, categoryId]
     **/
    void updateCategoryForAdmin(@Param("userId") int userId, @Param("categoryName") String categoryName, @Param("categoryId") int categoryId);


}
