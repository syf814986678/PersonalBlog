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

@RestController
@RequestMapping("/category")
@Log4j2
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/selectAllCategoryByPage")
    public Result selectAllCategoryByPage(HttpServletRequest request, @RequestParam("pageNow") int pageNow, @RequestParam("pageSize") int pageSize){
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        try {
            Claims claims = (Claims)request.getAttribute("user_claims");
            if(claims!=null){
                int userid = (int)claims.get("userid");
                ArrayList<Mycategory> mycategories = categoryService.selectAllCategoryByPage(userid,pageNow,pageSize);
                int totalCategoryNums = categoryService.selectTotalCategoryNums(userid);
                result.setCodeState(CodeState.success);
                map.put("mycategories",mycategories);
                map.put("totalCategoryNums",totalCategoryNums);
            }
            else {
                result.setCodeState(CodeState.tokenError);
                map.put("tokenError", request.getAttribute("tokenError"));
            }
        }
        catch (Exception e) {
            log.error(e);
            result.setCodeState(CodeState.exception);
            map.put("exception", "服务端处理错误！请稍后再试");
        }
        finally {
            result.setMsg(map);
        }
        return result;
    }

    @PostMapping("/selectAllCategoryForBlog")
    public Result selectAllCategoryForBlog(HttpServletRequest request){
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        try {
            Claims claims = (Claims)request.getAttribute("user_claims");
            if(claims!=null){
                int userid = (int)claims.get("userid");
                ArrayList<Mycategory> mycategories = categoryService.selectAllCategoryForBlog(userid);
                result.setCodeState(CodeState.success);
                map.put("mycategories",mycategories);
            }
            else {
                result.setCodeState(CodeState.tokenError);
                map.put("tokenError", request.getAttribute("tokenError"));
            }
        }
        catch (Exception e) {
            log.error(e);
            result.setCodeState(CodeState.exception);
            map.put("exception", "服务端处理错误！请稍后再试");
        }
        finally {
            result.setMsg(map);
        }
        return result;
    }

    @PostMapping("/addCategory")
    public Result addCategory(HttpServletRequest request,@RequestParam("categoryname") String categoryname){
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        try {
            Claims claims = (Claims)request.getAttribute("user_claims");
            if(claims!=null){
                int userid = (int)claims.get("userid");
                categoryService.addCategory(userid, categoryname);
                result.setCodeState(CodeState.success);
                map.put("add","添加成功");
            }
            else {
                result.setCodeState(CodeState.tokenError);
                map.put("tokenError", request.getAttribute("tokenError"));
            }
        }
        catch (Exception e) {
            log.error(e);
            result.setCodeState(CodeState.exception);
            map.put("exception", "服务端处理错误！请稍后再试");
        }
        finally {
            result.setMsg(map);
        }
        return result;
    }

    @PostMapping("/selectCategoryById")
    public Result selectCategoryById(HttpServletRequest request,@RequestParam("categoryid")int categoryid){
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        try {
            Claims claims = (Claims)request.getAttribute("user_claims");
            if(claims!=null){
                int userid = (int)claims.get("userid");
                Mycategory mycategory = categoryService.selectCategoryById(userid, categoryid);
                result.setCodeState(CodeState.success);
                map.put("mycategory", mycategory);
            }
            else {
                result.setCodeState(CodeState.tokenError);
                map.put("tokenError", request.getAttribute("tokenError"));
            }
        }
        catch (Exception e) {
            log.error(e);
            result.setCodeState(CodeState.exception);
            map.put("exception", "服务端处理错误！请稍后再试");
        }
        finally {
            result.setMsg(map);
        }
        return result;
    }

    @PostMapping("/updateCategory")
    public Result updateCategory(HttpServletRequest request,@RequestParam("categoryid")int categoryid,@RequestParam("categoryname") String categoryname){
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        try {
            Claims claims = (Claims)request.getAttribute("user_claims");
            if(claims!=null){
                int userid = (int)claims.get("userid");
                categoryService.updateCategory(userid, categoryname, categoryid);
                result.setCodeState(CodeState.success);
                map.put("update","修改成功");
            }
            else {
                result.setCodeState(CodeState.tokenError);
                map.put("tokenError", request.getAttribute("tokenError"));
            }
        }
        catch (Exception e) {
            log.error(e);
            result.setCodeState(CodeState.exception);
            map.put("exception", "服务端处理错误！请稍后再试");
        }
        finally {
            result.setMsg(map);
        }
        return result;
    }

    @PostMapping("/deleteCategory")
    public Result deleteCategory(HttpServletRequest request, @RequestParam("categoryid")int categoryid){
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        try {
            Claims claims = (Claims)request.getAttribute("user_claims");
            if(claims!=null){
                int userid = (int)claims.get("userid");
                int categoryRank = categoryService.getCategoryRank(userid, categoryid);
                if(categoryRank>0){
                    result.setCodeState(CodeState.operationError);
                    map.put("operationError", "请先修改/删除与此类别相关联的博客!");
                }
                else {
                    categoryService.deleteCategory(userid, categoryid);
                    result.setCodeState(CodeState.success);
                    map.put("delete","删除成功");
                }
            }
            else {
                result.setCodeState(CodeState.tokenError);
                map.put("tokenError", request.getAttribute("tokenError"));
            }
        }
        catch (Exception e) {
            log.error(e);
            result.setCodeState(CodeState.exception);
            map.put("exception", "服务端处理错误！请稍后再试");
        }
        finally {
            result.setMsg(map);
        }
        return result;
    }

    @PostMapping("/selectAllCategoryForCommon")
    public Result selectAllCategoryForCommon(){
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        try {
            ArrayList<Mycategory> mycategories = categoryService.selectAllCategoryForCommon();
            result.setCodeState(CodeState.success);
            map.put("mycategories",mycategories);
        }
        catch (Exception e) {
            log.error(e);
            result.setCodeState(CodeState.exception);
            map.put("exception", "服务端处理错误！请稍后再试");
        }
        finally {
            result.setMsg(map);
        }
        return result;
    }
}
