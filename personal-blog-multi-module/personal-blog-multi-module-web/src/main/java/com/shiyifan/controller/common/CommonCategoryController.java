package com.shiyifan.controller.common;

import com.shiyifan.BlogService;
import com.shiyifan.CategoryService;
import com.shiyifan.ResultUtil;
import com.shiyifan.pojo.Blog;
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
@CrossOrigin
@Log4j2
@RequestMapping("/common/category")
public class CommonCategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/selectCategoryForCommon")
    public Result selectCategoryForCommon() throws Exception {
        ArrayList<Category> categoryListForCommon = null;
        try {
            categoryListForCommon = categoryService.selectCategoryForCommon();
        } catch (Exception e) {
            log.error(e);
            throw new Exception();
        }
        return ResultUtil.success(categoryListForCommon);
    }
}
