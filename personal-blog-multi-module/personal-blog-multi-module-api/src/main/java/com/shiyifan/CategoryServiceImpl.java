package com.shiyifan;

import com.shiyifan.mapper.CategoryMapper;
import com.shiyifan.pojo.Category;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

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
     * @method selectCategoryForCommon
     * @params []
     **/
    @Override
    public ArrayList<Category> selectCategoryForCommon() throws Exception {
        log.info("方法:selectCategoryForCommon开始");
        ArrayList<Category> categories = null;
        try {
            categories = categoryMapper.selectCategoryForCommon();
        } catch (Exception e) {
            log.error("查找公共类别错误" + e.toString());
            throw new Exception("查找公共类别错误" + e.toString());
        }
        return categories;
    }

    /**
     * @return java.util.ArrayList<com.shiyifan.pojo.Category>
     * @author ZouCha
     * @date 2020-12-27 15:46:33
     * @method selectCategoryForAdmin
     * @params [userId]
     **/
    @Override
    public ArrayList<Category> selectCategoryForAdmin(int userId) throws Exception {
        log.info("方法:selectCategoryForAdmin开始,userId:"+userId);
        ArrayList<Category> categories = null;
        try {
            categories = categoryMapper.selectCategoryForAdmin(userId);
        } catch (Exception e) {
            log.error("selectCategoryForAdmin错误" + e.toString());
            throw new Exception("selectCategoryForAdmin错误" + e.toString());
        }
        return categories;
    }
}
