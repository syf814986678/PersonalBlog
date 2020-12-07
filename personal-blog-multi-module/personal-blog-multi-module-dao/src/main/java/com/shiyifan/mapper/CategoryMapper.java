package com.shiyifan.mapper;


import com.shiyifan.pojo.Blog;
import com.shiyifan.pojo.Category;
import org.apache.ibatis.annotations.Mapper;
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
}
