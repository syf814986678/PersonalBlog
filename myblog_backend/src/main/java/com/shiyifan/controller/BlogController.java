package com.shiyifan.controller;

import com.shiyifan.constant.CodeState;
import com.shiyifan.pojo.Myblog;
import com.shiyifan.pojo.Mycategory;
import com.shiyifan.service.BlogService;
import com.shiyifan.service.CategoryService;
import com.shiyifan.vo.Result;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    private BlogService blogService;

    @Autowired
    private CategoryService categoryService;
    /*------------------------------登陆后进行的操作-------------------------------*/

    //查找最新六条博客
    @PostMapping("/index")
    public Result selectLastestSixBlog(HttpServletRequest request) {
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        try {
            Claims claims = (Claims)request.getAttribute("user_claims");
            if(claims!=null){
                int userid = (int)claims.get("userid");
                ArrayList<Myblog> myblogs = blogService.selectBlogByPage(userid,1,5);
                blogService.selectTotalBlogNums(userid);
                result.setCodeState(CodeState.success);
                map.put("myblogs", myblogs);
            }
            else {
                result.setCodeState(CodeState.tokenError);
                map.put("tokenError", request.getAttribute("tokenError"));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setCodeState(CodeState.exception);
            map.put("exception", "服务端处理错误！请稍后再试");
        }
        finally {
            result.setMsg(map);
        }
        return result;
    }

    //根据博客ID和用户ID查找博客
    @PostMapping("/selectBlogById")
    public Result selectBlogById(HttpServletRequest request, @RequestParam("blogid") String blogid) {
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        try {
            Claims claims = (Claims)request.getAttribute("user_claims");
            if(claims!=null){
                int userid = (int)claims.get("userid");
                Myblog myblog = blogService.selectBlogById(userid, blogid);
                ArrayList<Mycategory> mycategories = categoryService.selectAllCategoryForBlog(userid);
                result.setCodeState(CodeState.success);
                map.put("myblog", myblog);
                map.put("mycategories", mycategories);
            }
            else {
                result.setCodeState(CodeState.tokenError);
                map.put("tokenError", request.getAttribute("tokenError"));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setCodeState(CodeState.exception);
            map.put("exception", "服务端处理错误！请稍后再试");
        }
        finally {
            result.setMsg(map);
        }
        return result;
    }

    //分页查询博客和查询总条数
    @PostMapping("/selectBlogByPage")
    public Result selectBlogByPage(HttpServletRequest request, @RequestParam("pageNow") int pageNow, @RequestParam("pageSize") int pageSize) {
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        try {
            Claims claims = (Claims)request.getAttribute("user_claims");
            if(claims!=null){
                int userid = (int)claims.get("userid");
                ArrayList<Myblog> myblogs = blogService.selectBlogByPage(userid, pageNow, pageSize);
                int totalBlogNums = blogService.selectTotalBlogNums(userid);
                result.setCodeState(CodeState.success);
                map.put("myblogs", myblogs);
                map.put("totalBlogNums", totalBlogNums);
            }
            else {
                System.out.println("controller"+request.getAttribute("tokenError"));
                result.setCodeState(CodeState.tokenError);
                map.put("tokenError", request.getAttribute("tokenError"));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setCodeState(CodeState.exception);
            map.put("exception", "服务端处理错误！请稍后再试");
        }
        finally {
            result.setMsg(map);
        }
        return result;
    }

    //添加博客
    @PostMapping("/addBlog")
    public Result addBlog(HttpServletRequest request,@RequestBody Myblog myblog) {
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        try {
            Claims claims = (Claims)request.getAttribute("user_claims");
            if(claims!=null){
                myblog.setBlogId(UUID.randomUUID().toString().replaceAll("-", ""));
                blogService.addBlog(myblog);
                result.setCodeState(CodeState.success);
                map.put("add", "发布成功");
            }
            else {
                result.setCodeState(CodeState.tokenError);
                map.put("tokenError", request.getAttribute("tokenError"));
            }
        }
        catch (Exception e) {
            System.out.println(e);
            result.setCodeState(CodeState.exception);
            map.put("exception", "服务端处理错误！请稍后再试");
        }
        finally {
            result.setMsg(map);
        }
        return result;
    }

    //更新博客
    @PostMapping("/updateBlog")
    public Result updateBlog(HttpServletRequest request,@RequestBody Myblog myblog){
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        try {
            Claims claims = (Claims)request.getAttribute("user_claims");
            if(claims!=null){
                blogService.updateBlog(myblog);
                result.setCodeState(CodeState.success);
                map.put("update", "更新成功");
            }
            else {
                result.setCodeState(CodeState.tokenError);
                map.put("tokenError", request.getAttribute("tokenError"));
            }
        }
        catch (Exception e) {
            System.out.println(e);
            result.setCodeState(CodeState.exception);
            map.put("exception", "服务端处理错误！请稍后再试");
        }
        finally {
            result.setMsg(map);
        }
        return result;
    }

    //根据ID删除博客
    @PostMapping("/deleteBlog")
    public Result deleteBlog(HttpServletRequest request, @RequestParam("blogid") String blogid, @RequestParam("categoryid") int categoryid) {
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        try {
            Claims claims = (Claims)request.getAttribute("user_claims");
            if(claims!=null){
                int userid = (int)claims.get("userid");
                blogService.deleteBlogById(userid, blogid,categoryid);
                result.setCodeState(CodeState.success);
                map.put("delete", "删除成功");
            }
            else {
                result.setCodeState(CodeState.tokenError);
                map.put("tokenError", request.getAttribute("tokenError"));
            }
        }
        catch (Exception e) {
            System.out.println(e);
            result.setCodeState(CodeState.exception);
            map.put("exception", "服务端处理错误！请稍后再试");
        }
        finally {
            result.setMsg(map);
        }
        return result;
    }

    @PostMapping("/getTempBlog")
    //读取暂存redis
    public Result getTempBlog(HttpServletRequest request) {
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        try {
            Claims claims = (Claims)request.getAttribute("user_claims");
            if(claims!=null){
                int userid = (int)claims.get("userid");
                Myblog myblog = blogService.getTempBlog(userid);
                result.setCodeState(CodeState.success);
                map.put("myblog", myblog);
            }
            else {
                result.setCodeState(CodeState.tokenError);
                map.put("tokenError", request.getAttribute("tokenError"));
            }
        }
        catch (Exception e) {
            System.out.println(e);
            result.setCodeState(CodeState.exception);
            map.put("exception", "服务端处理错误！请稍后再试");
        }
        finally {
            result.setMsg(map);
        }
        return result;
    }
    /*---------------------------------------------------------------------------*/

    /*------------------------------公共操作-------------------------------*/
    //暂存redis
    @PostMapping("/setTempBlog")
    public Result setTempBlog(@RequestBody Myblog myblog) {
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        try {
            blogService.setTempBlog(myblog);
            result.setCodeState(CodeState.success);
            map.put("setTempBlog", "未完成博客已暂存");
        }
        catch (Exception e) {
            System.out.println(e);
            result.setCodeState(CodeState.exception);
            map.put("exception", "服务端处理错误！请稍后再试");
        }
        finally {
            result.setMsg(map);
        }
        return result;
    }

    //根据博客ID查找博客
    @PostMapping("/selectBlogByIdForCommon")
    public Result selectBlogByIdForCommon(@RequestParam("blogid") String blogid) {
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        try {
            Myblog myblog = blogService.selectBlogByIdForCommon(blogid);
            result.setCodeState(CodeState.success);
            map.put("myblog", myblog);
        }
        catch (Exception e) {
            System.out.println(e);
            result.setCodeState(CodeState.exception);
            map.put("exception", "服务端处理错误！请稍后再试");
        }
        finally {
            result.setMsg(map);
        }
        return result;
    }

    //分页查找最新博客
    @PostMapping("/selectLastestBlogByPagForCommon")
    public Result selectLastestBlogByPagForCommon(@RequestParam("pageNow") int pageNow, @RequestParam("pageSize") int pageSize) {
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        try {
            ArrayList<Myblog> myblogs = blogService.selectBlogAllByPageForCommon(0,pageNow, pageSize);
            int nums = blogService.selectTotalBlogNumsForCommon(0);
            result.setCodeState(CodeState.success);
            map.put("myblogs", myblogs);
            map.put("nums",nums);
        }
        catch (Exception e) {
            System.out.println(e);
            result.setCodeState(CodeState.exception);
            map.put("exception", "服务端处理错误！请稍后再试");
        }
        finally {
            result.setMsg(map);
        }
        return result;
    }

    //根据种类ID查找博客
    @PostMapping("/selectBlogByCategoryIdAndPageForCommon")
    public Result selectBlogByCategoryIdAndPageForCommon(@RequestParam("categoryid")int categoryid, @RequestParam("pageNow")int pageNow, @RequestParam("pageSize")int pageSize){
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        try {
            ArrayList<Myblog> myblogs = blogService.selectBlogAllByPageForCommon(categoryid, pageNow, pageSize);
            int nums = blogService.selectTotalBlogNumsForCommon(categoryid);
            result.setCodeState(CodeState.success);
            map.put("myblogs", myblogs);
            map.put("nums",nums);
        }
        catch (Exception e) {
            System.out.println(e);
            result.setCodeState(CodeState.exception);
            map.put("exception", "服务端处理错误！请稍后再试");
        }
        finally {
            result.setMsg(map);
        }
        return result;
    }

    //根据作者查找博客
    @PostMapping("/selectBlogByAuthorForCommon")
    public Result selectBlogByAuthorForCommon(@RequestParam("userid")int userid, @RequestParam("pageNow")int pageNow, @RequestParam("pageSize")int pageSize){
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        try {
            ArrayList<Myblog> myblogs = blogService.selectBlogByAuthorForCommon(userid, pageNow, pageSize);
            int nums = blogService.selectTotalBlogNums(userid);
            result.setCodeState(CodeState.success);
            map.put("myblogs", myblogs);
            map.put("nums",nums);
        }
        catch (Exception e) {
            System.out.println(e);
            result.setCodeState(CodeState.exception);
            map.put("exception", "服务端处理错误！请稍后再试");
        }
        finally {
            result.setMsg(map);
        }
        return result;
    }
    /*---------------------------------------------------------------------------*/

    /*------------------------------搜索操作-------------------------------*/
    @PostMapping("/search/{keyword}")
    public Result search(@PathVariable("keyword") String keyword){
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        System.out.println(keyword);
        try {
            ArrayList<Map<String, Object>> results = blogService.searchContentPage(keyword, 1,30);
            map.put("results", results);
            map.put("total", results.size());
            result.setCodeState(CodeState.success);
        }
        catch (Exception e) {
            System.out.println(e);
            result.setCodeState(CodeState.exception);
            map.put("exception", "服务端处理错误！请稍后再试");
        }
        finally {
            result.setMsg(map);
        }
        return result;
    }
}
