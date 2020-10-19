package com.shiyifan.controller;

import com.shiyifan.constant.CodeState;
import com.shiyifan.pojo.Myblog;
import com.shiyifan.pojo.Mycategory;
import com.shiyifan.service.BlogService;
import com.shiyifan.service.CategoryService;
import com.shiyifan.utils.VisitCountUtil;
import com.shiyifan.vo.Result;
import io.jsonwebtoken.Claims;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author 81498
 */
@RestController
@RequestMapping("/blog")
@Log4j2
@CrossOrigin
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private VisitCountUtil VisitCountUtil;
    /*------------------------------登陆后进行的操作-------------------------------*/

    //查找最新六条博客
    @PostMapping("/index")
    public Result selectLastestSixBlog(HttpServletRequest request) {
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        try {
            Claims claims = (Claims)request.getAttribute("user_claims");
            if(claims!=null){
                int userId = (int)claims.get("userId");
                ArrayList<Myblog> myblogs = blogService.selectBlogByPage(userId,1,5);
                blogService.selectTotalBlogNums(userId);
                result.setCodeState(CodeState.success);
                map.put("myblogs", myblogs);
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

    //根据博客ID和用户ID查找博客
    @PostMapping("/selectBlogById")
    public Result selectBlogById(HttpServletRequest request, @RequestParam("blogId") String blogId) {
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        try {
            Claims claims = (Claims)request.getAttribute("user_claims");
            if(claims!=null){
                int userId = (int)claims.get("userId");
                Myblog myblog = blogService.selectBlogById(userId, blogId);
                ArrayList<Mycategory> mycategories = categoryService.selectAllCategoryForBlog(userId);
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
            log.error(e);
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
                int userId = (int)claims.get("userId");
                ArrayList<Myblog> myblogs = blogService.selectBlogByPage(userId, pageNow, pageSize);
                int totalBlogNums = blogService.selectTotalBlogNums(userId);
                result.setCodeState(CodeState.success);
                map.put("myblogs", myblogs);
                map.put("totalBlogNums", totalBlogNums);
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
                blogService.addElasticsearchBlog(myblog.getBlogId());
                result.setCodeState(CodeState.success);
                map.put("add", "发布成功");
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

    //更新博客
    @PostMapping("/updateBlog")
    public Result updateBlog(HttpServletRequest request,@RequestBody Myblog myblog){
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        try {
            Claims claims = (Claims)request.getAttribute("user_claims");
            if(claims!=null){
                blogService.updateBlog(myblog);
                blogService.updateElasticsearchBlog(myblog.getBlogId());
                result.setCodeState(CodeState.success);
                map.put("update", "更新成功");
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

    //根据ID删除博客
    @PostMapping("/deleteBlog")
    public Result deleteBlog(HttpServletRequest request, @RequestParam("blogId") String blogId, @RequestParam("categoryId") int categoryId) {
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        try {
            Claims claims = (Claims)request.getAttribute("user_claims");
            if(claims!=null){
                int userId = (int)claims.get("userId");
                blogService.deleteBlogById(userId, blogId,categoryId);
                blogService.deleteElasticsearchBlog(blogId);
                result.setCodeState(CodeState.success);
                map.put("delete", "删除成功");
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

    //读取暂存redis
    @PostMapping("/getTempBlog")
    public Result getTempBlog(HttpServletRequest request) {
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        try {
            Claims claims = (Claims)request.getAttribute("user_claims");
            if(claims!=null){
                int userId = (int)claims.get("userId");
                Myblog myblog = blogService.getTempBlog(userId);
                result.setCodeState(CodeState.success);
                map.put("myblog", myblog);
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

    //获取访问人数
    @PostMapping("/getVisitNums")
    public Result getVisitNums(HttpServletRequest request,@RequestParam("dayNum") int dayNum) {
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        try {
            Claims claims = (Claims)request.getAttribute("user_claims");
            if(claims!=null){
                long visitor = VisitCountUtil.getVisitCount((int)claims.get("userId"),dayNum);
                result.setCodeState(CodeState.success);
                map.put("visitNums", visitor);
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

    //查询博客总条数
    @PostMapping("/selectTotalBlogNums")
    public Result selectTotalBlogNums(HttpServletRequest request) {
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        try {
            Claims claims = (Claims)request.getAttribute("user_claims");
            if(claims!=null){
                int totalBlogNums = blogService.selectTotalBlogNums((int)claims.get("userId"));
                result.setCodeState(CodeState.success);
                map.put("totalBlogNums", totalBlogNums);
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
    /*---------------------------------------------------------------------------*/

    /*------------------------------公共操作-------------------------------*/
    //获取所有访问人数
    @PostMapping("/getAllVisitNums")
    public Result getAllVisitNums() {
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        try {
            long visitCount = VisitCountUtil.getVisitCount(0, 2);
            result.setCodeState(CodeState.success);
            map.put("visitAllNums", visitCount);
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
            log.error(e);
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
    public Result selectBlogByIdForCommon(HttpServletRequest request,@RequestParam("blogId") String blogId) {
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        try {
            Myblog myblog = blogService.selectBlogByIdForCommon(blogId);
            VisitCountUtil.setVisitCount(request,myblog.getMyuser().getUserId());
            result.setCodeState(CodeState.success);
            map.put("myblog", myblog);
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

    //分页查找最新博客
    @PostMapping("/selectLastestBlogByPagForCommon")
    public Result selectLastestBlogByPagForCommon(HttpServletRequest request,@RequestParam("pageNow") int pageNow, @RequestParam("pageSize") int pageSize) {
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        try {
            ArrayList<Myblog> myblogs = blogService.selectBlogAllByPageForCommon(0,pageNow, pageSize);
            int nums = blogService.selectTotalBlogNumsForCommon(0);
            VisitCountUtil.setVisitCount(request);
            result.setCodeState(CodeState.success);
            map.put("myblogs", myblogs);
            map.put("nums",nums);
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

    //根据种类ID查找博客
    @PostMapping("/selectBlogByCategoryIdAndPageForCommon")
    public Result selectBlogByCategoryIdAndPageForCommon(@RequestParam("categoryId")int categoryId, @RequestParam("pageNow")int pageNow, @RequestParam("pageSize")int pageSize){
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        try {
            ArrayList<Myblog> myblogs = blogService.selectBlogAllByPageForCommon(categoryId, pageNow, pageSize);
            int nums = blogService.selectTotalBlogNumsForCommon(categoryId);
            result.setCodeState(CodeState.success);
            map.put("myblogs", myblogs);
            map.put("nums",nums);
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

    //根据作者查找博客
    @PostMapping("/selectBlogByAuthorForCommon")
    public Result selectBlogByAuthorForCommon(@RequestParam("userId")int userId, @RequestParam("pageNow")int pageNow, @RequestParam("pageSize")int pageSize){
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        try {
            ArrayList<Myblog> myblogs = blogService.selectBlogByAuthorForCommon(userId, pageNow, pageSize);
            int nums = blogService.selectTotalBlogNums(userId);
            result.setCodeState(CodeState.success);
            map.put("myblogs", myblogs);
            map.put("nums",nums);
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
    /*---------------------------------------------------------------------------*/

    /*------------------------------搜索操作-------------------------------*/

    //搜索博客
    @PostMapping("/search/{keyword}")
    public Result search(@PathVariable("keyword") String keyword){
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        try {
            ArrayList<Map<String, Object>> results = blogService.searchContentPage(keyword, 1,30);
            map.put("results", results);
            map.put("total", results.size());
            result.setCodeState(CodeState.success);
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

    //查找热词
    @PostMapping("/hotkeys")
    public Result hotkeys(){
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        InputStreamReader in=null;
        BufferedReader br=null;
        try {
            String s = "";
            in = new InputStreamReader(new FileInputStream("C:\\Users\\81498\\Desktop\\remote.txt"), "UTF-8");
            br = new BufferedReader(in);
            ArrayList<HashMap<String, String>> hotkeys = new ArrayList<>();
            while ((s = br.readLine()) != null) {
                HashMap<String, String> stringmap = new HashMap<>();
                stringmap.put("value", s);
                hotkeys.add(stringmap);
            }
            map.put("hotkeys", hotkeys);
            result.setCodeState(CodeState.success);
        }
        catch (Exception e) {
            log.error(e);
            result.setCodeState(CodeState.exception);
            map.put("exception", "服务端处理错误！请稍后再试");
        }
        finally {
            try {
                assert in != null;
                in.close();
                assert br != null;
                br.close();
            } catch (IOException e) {
                log.error(e);
            }
            result.setMsg(map);
        }
        return result;
    }
}
