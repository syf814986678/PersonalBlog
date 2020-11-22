package com.shiyifan.controller;

import com.shiyifan.constant.CodeState;
import com.shiyifan.pojo.Mycategory;
import com.shiyifan.service.CategoryService;
import com.shiyifan.vo.Result;
import io.jsonwebtoken.Claims;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author ZouCha
 * @name CategoryController
 * @date 2020-11-20 15:11:31
 *
 **/
@RestController
@RequestMapping("/category")
@Log4j2
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 分页查找博客类别
     * 已修改(controller名称,restful风格)
     * @author ZouCha
     * @date 2020-11-20 15:11:38
     * @method selectBlogCategoryByPage
     * @params [request, pageNow, pageSize]
     * @return com.shiyifan.vo.Result
     *
     **/
    @PostMapping("/selectBlogCategoryByPage/{pageSize}/{pageNow}")
    public Result selectBlogCategoryByPage(HttpServletRequest request, @PathVariable("pageNow") int pageNow, @PathVariable("pageSize") int pageSize){
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        Claims claims = (Claims)request.getAttribute(CodeState.USER_CLAIMS_STR);
        if(claims!=null){
            int userId = (int)claims.get("userId");
            ArrayList<Mycategory> mycategories = categoryService.selectAllCategoryByPage(userId,pageNow,pageSize);
            int totalCategoryNums = categoryService.selectTotalCategoryNums(userId);
            if(mycategories!=null && totalCategoryNums!=-1){
                result.setCodeState(CodeState.SUCCESS_CODE);
                map.put("mycategories",mycategories);
                map.put("totalCategoryNums",totalCategoryNums);
            }
            else {
                result.setCodeState(CodeState.EXCEPTION_CODE);
                map.put(CodeState.EXCEPTION_STR, "分页查找博客类别失败！");
            }
        }
        else {
            if(request.getAttribute(CodeState.TOKEN_ERROR_STR)!=null){
                result.setCodeState(CodeState.TOKEN_ERROR_CODE);
                map.put(CodeState.TOKEN_ERROR_STR, request.getAttribute(CodeState.TOKEN_ERROR_STR));
            }
            if(request.getAttribute(CodeState.TOKEN_TIME_LIMIT_STR)!=null) {
                result.setCodeState(CodeState.TOKEN_TIME_LIMIT_CODE);
                map.put(CodeState.TOKEN_TIME_LIMIT_STR, request.getAttribute(CodeState.TOKEN_TIME_LIMIT_STR));
            }
        }
        result.setMsg(map);
        return result;
    }

    /**
     * 查找全部博客类别
     * 已修改(controller名称,restful风格)
     * @author ZouCha
     * @date 2020-11-20 15:11:42
     * @method selectAllBlogCategory
     * @params [request]
     * @return com.shiyifan.vo.Result
     *
     **/
    @PostMapping("/selectAllBlogCategory")
    public Result selectAllBlogCategory(HttpServletRequest request){
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        Claims claims = (Claims)request.getAttribute(CodeState.USER_CLAIMS_STR);
        if(claims!=null){
            int userId = (int)claims.get("userId");
            ArrayList<Mycategory> mycategories = categoryService.selectAllCategoryForBlog(userId);
            if(mycategories!=null){
                result.setCodeState(CodeState.SUCCESS_CODE);
                map.put("mycategories",mycategories);
            }
            else {
                result.setCodeState(CodeState.EXCEPTION_CODE);
                map.put(CodeState.EXCEPTION_STR, "查找全部博客类别失败！");
            }
        }
        else {
            if(request.getAttribute(CodeState.TOKEN_ERROR_STR)!=null){
                result.setCodeState(CodeState.TOKEN_ERROR_CODE);
                map.put(CodeState.TOKEN_ERROR_STR, request.getAttribute(CodeState.TOKEN_ERROR_STR));
            }
            if(request.getAttribute(CodeState.TOKEN_TIME_LIMIT_STR)!=null) {
                result.setCodeState(CodeState.TOKEN_TIME_LIMIT_CODE);
                map.put(CodeState.TOKEN_TIME_LIMIT_STR, request.getAttribute(CodeState.TOKEN_TIME_LIMIT_STR));
            }
        }
        result.setMsg(map);
        return result;
    }

    /**
     * 添加博客类别
     * 已修改(controller名称,restful风格)
     * @author ZouCha
     * @date 2020-11-20 15:11:49
     * @method addCategory
     * @params [request, categoryName]
     * @return com.shiyifan.vo.Result
     *
     **/
    @PostMapping("/addCategory/{categoryName}")
    public Result addCategory(HttpServletRequest request,@PathVariable("categoryName") String categoryName){
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        Claims claims = (Claims)request.getAttribute(CodeState.USER_CLAIMS_STR);
        if(claims!=null){
            int userId = (int)claims.get("userId");
            if(categoryService.addCategory(userId, categoryName)){
                result.setCodeState(CodeState.SUCCESS_CODE);
                map.put("add","添加成功");
            }
            else {
                result.setCodeState(CodeState.EXCEPTION_CODE);
                map.put(CodeState.EXCEPTION_STR, "添加博客类别失败！");
            }
        }
        else {
            if(request.getAttribute(CodeState.TOKEN_ERROR_STR)!=null){
                result.setCodeState(CodeState.TOKEN_ERROR_CODE);
                map.put(CodeState.TOKEN_ERROR_STR, request.getAttribute(CodeState.TOKEN_ERROR_STR));
            }
            if(request.getAttribute(CodeState.TOKEN_TIME_LIMIT_STR)!=null) {
                result.setCodeState(CodeState.TOKEN_TIME_LIMIT_CODE);
                map.put(CodeState.TOKEN_TIME_LIMIT_STR, request.getAttribute(CodeState.TOKEN_TIME_LIMIT_STR));
            }
        }
        result.setMsg(map);
        return result;
    }

    /**
     * 根据类别ID查找博客类别
     * 已修改(controller名称,restful风格)
     * @author ZouCha
     * @date 2020-11-20 15:11:53
     * @method selectCategoryById
     * @params [request, categoryId]
     * @return com.shiyifan.vo.Result
     *
     **/
    @PostMapping("/selectCategoryById/{categoryId}")
    public Result selectCategoryById(HttpServletRequest request,@PathVariable("categoryId")int categoryId){
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        Claims claims = (Claims)request.getAttribute(CodeState.USER_CLAIMS_STR);
        if(claims!=null){
            int userId = (int)claims.get("userId");
            Mycategory mycategory = categoryService.selectCategoryById(userId, categoryId);
            if(mycategory!=null){
                result.setCodeState(CodeState.SUCCESS_CODE);
                map.put("mycategory", mycategory);
            }
            else {
                result.setCodeState(CodeState.EXCEPTION_CODE);
                map.put(CodeState.EXCEPTION_STR, "根据类别ID查找博客类别失败！");
            }
        }
        else {
            if(request.getAttribute(CodeState.TOKEN_ERROR_STR)!=null){
                result.setCodeState(CodeState.TOKEN_ERROR_CODE);
                map.put(CodeState.TOKEN_ERROR_STR, request.getAttribute(CodeState.TOKEN_ERROR_STR));
            }
            if(request.getAttribute(CodeState.TOKEN_TIME_LIMIT_STR)!=null) {
                result.setCodeState(CodeState.TOKEN_TIME_LIMIT_CODE);
                map.put(CodeState.TOKEN_TIME_LIMIT_STR, request.getAttribute(CodeState.TOKEN_TIME_LIMIT_STR));
            }
        }
        result.setMsg(map);
        return result;
    }

    /**
     * 更新博客类别
     * 已修改(controller名称,restful风格)
     * @author ZouCha
     * @date 2020-11-20 15:11:58
     * @method updateCategory
     * @params [request, categoryId, categoryName]
     * @return com.shiyifan.vo.Result
     *
     **/
    @PostMapping("/updateCategory/{categoryId}/{categoryName}")
    public Result updateCategory(HttpServletRequest request,@PathVariable("categoryId")int categoryId,@PathVariable("categoryName") String categoryName){
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        Claims claims = (Claims)request.getAttribute(CodeState.USER_CLAIMS_STR);
        if(claims!=null){
            int userId = (int)claims.get("userId");
            if(categoryService.updateCategory(userId, categoryName, categoryId)){
                result.setCodeState(CodeState.SUCCESS_CODE);
                map.put("update","修改成功");
            }
            else {
                result.setCodeState(CodeState.EXCEPTION_CODE);
                map.put(CodeState.EXCEPTION_STR, "更新博客类别失败！");
            }
        }
        else {
            if(request.getAttribute(CodeState.TOKEN_ERROR_STR)!=null){
                result.setCodeState(CodeState.TOKEN_ERROR_CODE);
                map.put(CodeState.TOKEN_ERROR_STR, request.getAttribute(CodeState.TOKEN_ERROR_STR));
            }
            if(request.getAttribute(CodeState.TOKEN_TIME_LIMIT_STR)!=null) {
                result.setCodeState(CodeState.TOKEN_TIME_LIMIT_CODE);
                map.put(CodeState.TOKEN_TIME_LIMIT_STR, request.getAttribute(CodeState.TOKEN_TIME_LIMIT_STR));
            }
        }
        result.setMsg(map);
        return result;
    }

    /**
     * 删除博客类别
     * 已修改(controller名称,restful风格)
     * @author ZouCha
     * @date 2020-11-20 15:12:03
     * @method deleteCategory
     * @params [request, categoryId]
     * @return com.shiyifan.vo.Result
     *
     **/
    @PostMapping("/deleteCategory/{categoryId}")
    public Result deleteCategory(HttpServletRequest request, @PathVariable("categoryId")int categoryId){
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        Claims claims = (Claims)request.getAttribute(CodeState.USER_CLAIMS_STR);
        if(claims!=null){
            int userId = (int)claims.get("userId");
            int categoryRank = categoryService.getCategoryRank(userId, categoryId);
            if(categoryRank>0){
                result.setCodeState(CodeState.OPERATION_ERROR_CODE);
                map.put(CodeState.OPERATION_ERROR_STR, "请先修改/删除与此类别相关联的博客!");
            }
            else if(categoryRank==-1){
                result.setCodeState(CodeState.EXCEPTION_CODE);
                map.put(CodeState.EXCEPTION_STR, "获取博客类别Rank失败！");
            }
            else {
                if(categoryService.deleteCategory(userId, categoryId)){
                    result.setCodeState(CodeState.SUCCESS_CODE);
                    map.put("delete","删除成功");
                }
                else {
                    result.setCodeState(CodeState.EXCEPTION_CODE);
                    map.put(CodeState.EXCEPTION_STR, "删除博客类别失败！");
                }
            }
        }
        else {
            if(request.getAttribute(CodeState.TOKEN_ERROR_STR)!=null){
                result.setCodeState(CodeState.TOKEN_ERROR_CODE);
                map.put(CodeState.TOKEN_ERROR_STR, request.getAttribute(CodeState.TOKEN_ERROR_STR));
            }
            if(request.getAttribute(CodeState.TOKEN_TIME_LIMIT_STR)!=null) {
                result.setCodeState(CodeState.TOKEN_TIME_LIMIT_CODE);
                map.put(CodeState.TOKEN_TIME_LIMIT_STR, request.getAttribute(CodeState.TOKEN_TIME_LIMIT_STR));
            }
        }
        result.setMsg(map);
        return result;
    }

    /**
     * 查找全部公共博客类别
     * 已修改(controller名称,restful风格)
     * @author ZouCha
     * @date 2020-11-20 15:12:07
     * @method selectAllCategoryForCommon
     * @params []
     * @return com.shiyifan.vo.Result
     *
     **/
    @PostMapping("/selectAllCategoryForCommon")
    public Result selectAllCategoryForCommon(){
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        ArrayList<Mycategory> mycategories = categoryService.selectAllCategoryForCommon();
        if(mycategories!=null){
            result.setCodeState(CodeState.SUCCESS_CODE);
            map.put("mycategories",mycategories);
        }
        else {
            result.setCodeState(CodeState.EXCEPTION_CODE);
            map.put(CodeState.EXCEPTION_STR, "查找全部公共博客类别失败！");
        }
        result.setMsg(map);
        return result;
    }
}
