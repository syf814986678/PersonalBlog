package com.shiyifan.mapper;


import com.shiyifan.pojo.Blog;
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
     * @date 2020-12-30 10:21:02
     * @method selectCategoryForAdmin
     * @params [userId]
     **/
    ArrayList<Category> selectCategoryForAdmin(@Param("userId") int userId);

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

}
