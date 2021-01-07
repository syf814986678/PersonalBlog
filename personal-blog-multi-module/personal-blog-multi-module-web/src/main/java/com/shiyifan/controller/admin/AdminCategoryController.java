package com.shiyifan.controller.admin;

import com.shiyifan.CategoryService;
import com.shiyifan.ResultUtil;
import com.shiyifan.pojo.Category;
import com.shiyifan.pojo.CodeState;
import com.shiyifan.pojo.Result;
import io.jsonwebtoken.Claims;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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

    /**
     * @return com.shiyifan.pojo.Result
     * @author ZouCha
     * @date 2021-01-02 19:05:31
     * @method selectCategoryForAdmin
     * @params [request, pageSize, pageNow]
     **/
    @PostMapping("/selectCategoryForAdmin/{pageSize}/{pageNow}")
    public Result selectCategoryForAdmin(HttpServletRequest request, @PathVariable("pageSize") int pageSize, @PathVariable("pageNow") int pageNow) throws Exception {
        Claims claims = null;
        ArrayList<Category> categoryList = null;
        try {
            claims = (Claims) request.getAttribute(CodeState.USER_CLAIMS_STR);
            int userId = (int) claims.get("userId");
            categoryList = categoryService.selectCategoryForAdmin(userId, pageNow, pageSize);
        } catch (Exception e) {
            log.error("selectAllCategoryForAdmin错误" + e.toString());
            throw new Exception("selectAllCategoryForAdmin错误" + e.toString());
        }
        return ResultUtil.success(categoryList);

    }

    @PostMapping("/getTotalCategoriesForAdmin")
    public Result getTotalCategoriesForAdmin(HttpServletRequest request) throws Exception {
        Claims claims = null;
        Integer totalCategoriesForAdmin = null;
        try {
            claims = (Claims) request.getAttribute(CodeState.USER_CLAIMS_STR);
            int userId = (int) claims.get("userId");
            totalCategoriesForAdmin = categoryService.getTotalCategoriesForAdmin(userId);
        } catch (Exception e) {
            log.error("getTotalCategoriesForAdmin错误" + e.toString());
            throw new Exception("getTotalCategoriesForAdmin错误" + e.toString());
        }
        return ResultUtil.success(totalCategoriesForAdmin);
    }

    @PostMapping("/addCategoryForAdmin/{categoryName}")
    public Result addCategoryForAdmin(HttpServletRequest request, @PathVariable("categoryName") String categoryName) throws Exception {
        Claims claims = null;
        try {
            claims = (Claims) request.getAttribute(CodeState.USER_CLAIMS_STR);
            int userId = (int) claims.get("userId");
            categoryService.addCategoryForAdmin(userId, categoryName);
        } catch (Exception e) {
            log.error("addCategoryForAdmin错误" + e.toString());
            throw new Exception("addCategoryForAdmin错误" + e.toString());
        }
        return ResultUtil.success(null);
    }

    @PostMapping("/deleteCategoryForAdmin/{categoryId}")
    public Result deleteCategoryForAdmin(HttpServletRequest request, @PathVariable("categoryId") int categoryId) throws Exception {
        Claims claims = null;
        try {
            claims = (Claims) request.getAttribute(CodeState.USER_CLAIMS_STR);
            int userId = (int) claims.get("userId");
            if (categoryService.deleteCategoryForAdmin(userId, categoryId)) {
                return ResultUtil.success(null);
            } else {
                return ResultUtil.operationError("请先修改/删除与此类别相关联的博客!", null);
            }
        } catch (Exception e) {
            log.error("deleteCategoryForAdmin错误" + e.toString());
            throw new Exception("deleteCategoryForAdmin错误" + e.toString());
        }

    }

    @PostMapping("/updateCategoryForAdmin/{categoryId}/{categoryName}")
    public Result updateCategoryForAdmin(HttpServletRequest request, @PathVariable("categoryId") int categoryId, @PathVariable("categoryName") String categoryName) throws Exception {
        Claims claims = null;
        try {
            claims = (Claims) request.getAttribute(CodeState.USER_CLAIMS_STR);
            int userId = (int) claims.get("userId");
            categoryService.updateCategoryForAdmin(userId, categoryName, categoryId);
        } catch (Exception e) {
            log.error("updateCategoryForAdmin错误" + e.toString());
            throw new Exception("updateCategoryForAdmin错误" + e.toString());
        }
        return ResultUtil.success(null);
    }
}
