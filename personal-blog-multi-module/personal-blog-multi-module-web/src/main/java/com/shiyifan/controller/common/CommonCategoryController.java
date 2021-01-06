package com.shiyifan.controller.common;

import com.shiyifan.CategoryService;
import com.shiyifan.ResultUtil;
import com.shiyifan.pojo.Category;
import com.shiyifan.pojo.Result;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * @author ZouCha
 * @name AdminCategoryController
 * @date 2020-12-02 18:35
 **/
@RestController
@Log4j2
@RequestMapping("/category/common")
public class CommonCategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * @return com.shiyifan.pojo.Result
     * @author user
     * @date 2021-01-05 16:04:06
     * @method selectTopTenCategoryForCommon
     * @params []
     **/
    @PostMapping("/selectTopTenCategoryForCommon")
    public Result selectTopTenCategoryForCommon() throws Exception {
        ArrayList<Category> categoryListForCommon = null;
        try {
            categoryListForCommon = categoryService.selectTopTenCategoryForCommon();
        } catch (Exception e) {
            log.error(e);
            throw new Exception();
        }
        return ResultUtil.success(categoryListForCommon);
    }

    /**
     * @return com.shiyifan.pojo.Result
     * @author user
     * @date 2021-01-06 14:27:43
     * @method selectAllCategoryForCommon
     * @params []
     **/
    @PostMapping("/selectAllCategoryForCommon")
    public Result selectAllCategoryForCommon() throws Exception {
        ArrayList<Category> categoryListForCommon = null;
        try {
            categoryListForCommon = categoryService.selectAllCategoryForCommon();
        } catch (Exception e) {
            log.error(e);
            throw new Exception();
        }
        return ResultUtil.success(categoryListForCommon);
    }
}
