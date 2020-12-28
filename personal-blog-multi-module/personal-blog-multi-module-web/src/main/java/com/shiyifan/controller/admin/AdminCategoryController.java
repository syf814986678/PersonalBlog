package com.shiyifan.controller.admin;

import com.shiyifan.CategoryService;
import com.shiyifan.ResultUtil;
import com.shiyifan.pojo.Category;
import com.shiyifan.pojo.CodeState;
import com.shiyifan.pojo.Result;
import io.jsonwebtoken.Claims;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * @author ZouCha
 * @name AdminCategoryController
 * @date 2020-12-02 18:35
 **/
@RestController
@Log4j2
@RequestMapping("/category/admin")
public class AdminCategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/selectCategoryForAdmin")
    public Result selectCategoryForAdmin(HttpServletRequest request) throws Exception {
        Claims claims = null;
        ArrayList<Category> categoryList = null;
        try {
            claims = (Claims) request.getAttribute(CodeState.USER_CLAIMS_STR);
            int userId = (int) claims.get("userId");
            categoryList = categoryService.selectCategoryForAdmin(userId);
        } catch (Exception e) {
            log.error("selectAllCategoryForAdmin错误" + e.toString());
            throw new Exception("selectAllCategoryForAdmin错误" + e.toString());
        }
        return ResultUtil.success(categoryList);

    }
}
