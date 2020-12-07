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
}
