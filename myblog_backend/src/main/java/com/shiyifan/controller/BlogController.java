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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author ZouCha
 * @name BlogController
 * @date 2020-11-20 15:05:06
 *
 **/
@RestController
@RequestMapping("/blog")
@Log4j2
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private VisitCountUtil VisitCountUtil;
    /*------------------------------登陆后进行的操作-------------------------------*/

    /**
     * 根据博客ID查找博客
     * 已修改(controller名称,restful风格)
     * @author ZouCha
     * @date 2020-11-20 15:05:54
     * @method selectBlogById
     * @params [request, blogId]
     * @return com.shiyifan.vo.Result
     *
     **/
    @PostMapping("/selectBlogById/{blogId}")
    public Result selectBlogById(HttpServletRequest request, @PathVariable("blogId") String blogId) {
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        Claims claims = (Claims)request.getAttribute(CodeState.USER_CLAIMS_STR);
        if(claims!=null){
            int userId = (int)claims.get("userId");
            Myblog myblog = blogService.selectBlogById(userId, blogId);
            ArrayList<Mycategory> mycategories = categoryService.selectAllCategoryForBlog(userId);
            if(myblog != null && mycategories!=null){
                result.setCodeState(CodeState.SUCCESS_CODE);
                map.put("myblog", myblog);
                map.put("mycategories", mycategories);
            }
            else {
                result.setCodeState(CodeState.EXCEPTION_CODE);
                map.put(CodeState.EXCEPTION_STR, "根据博客ID查找博客失败！");
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
     * 分页查找博客
     * 已修改(controller名称,restful风格)
     * @author ZouCha
     * @date 2020-11-20 15:06:06
     * @method selectBlogsByPage
     * @params [request, pageNow, pageSize]
     * @return com.shiyifan.vo.Result
     *
     **/
    @PostMapping("/selectBlogsByPage/{pageSize}/{pageNow}")
    public Result selectBlogsByPage(HttpServletRequest request, @PathVariable("pageNow") int pageNow, @PathVariable("pageSize") int pageSize) {
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        Claims claims = (Claims)request.getAttribute(CodeState.USER_CLAIMS_STR);
        if(claims!=null){
            int userId = (int)claims.get("userId");
            ArrayList<Myblog> myblogs = blogService.selectBlogByPage(userId, pageNow, pageSize);
            int totalBlogNums = blogService.selectTotalBlogNums(userId);
            if(myblogs!=null && totalBlogNums!=-1){
                result.setCodeState(CodeState.SUCCESS_CODE);
                map.put("myblogs", myblogs);
                map.put("totalBlogNums", totalBlogNums);
            }
            else {
                result.setCodeState(CodeState.EXCEPTION_CODE);
                map.put(CodeState.EXCEPTION_STR, "分页查找博客失败！");
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
     * 添加博客
     * 已修改(controller名称,restful风格)
     * @author ZouCha
     * @date 2020-11-20 15:06:17
     * @method addBlog
     * @params [request, myblog]
     * @return com.shiyifan.vo.Result
     *
     **/
    @PostMapping("/addBlog")
    public Result addBlog(HttpServletRequest request,@RequestBody Myblog myblog){
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        Claims claims = (Claims)request.getAttribute(CodeState.USER_CLAIMS_STR);
        if(claims!=null){
            myblog.setBlogId(UUID.randomUUID().toString().replaceAll("-", ""));
            try {
                if(blogService.addBlog(myblog) && blogService.addElasticsearchBlog(myblog.getBlogId())){
                    result.setCodeState(CodeState.SUCCESS_CODE);
                    map.put("add", "发布成功");
                }
                else {
                    result.setCodeState(CodeState.EXCEPTION_CODE);
                    map.put(CodeState.EXCEPTION_STR, "添加博客失败！");
                }
            }
            catch (Exception e){
                log.error(e);
                result.setCodeState(CodeState.EXCEPTION_CODE);
                map.put(CodeState.EXCEPTION_STR, "添加博客服务端处理错误！");
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
     * 更新博客
     * 已修改(controller名称,restful风格)
     * @author ZouCha
     * @date 2020-11-20 15:06:28
     * @method updateBlog
     * @params [request, myblog]
     * @return com.shiyifan.vo.Result
     *
     **/
    @PostMapping("/updateBlog")
    public Result updateBlog(HttpServletRequest request,@RequestBody Myblog myblog){
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        Claims claims = (Claims)request.getAttribute(CodeState.USER_CLAIMS_STR);
        if(claims!=null){
            try {
                if(blogService.updateBlog(myblog) && blogService.updateElasticsearchBlog(myblog.getBlogId())){
                    result.setCodeState(CodeState.SUCCESS_CODE);
                    map.put("update", "更新成功");
                }
                else {
                    result.setCodeState(CodeState.EXCEPTION_CODE);
                    map.put(CodeState.EXCEPTION_STR, "更新博客失败！");
                }
            }
            catch (Exception e){
                result.setCodeState(CodeState.EXCEPTION_CODE);
                map.put(CodeState.EXCEPTION_STR, "更新博客服务端处理错误！");
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
     * 根据ID删除博客
     * 已修改(controller名称,restful风格)
     * @author ZouCha
     * @date 2020-11-20 15:06:43
     * @method deleteBlog
     * @params [request, blogId, categoryId]
     * @return com.shiyifan.vo.Result
     *
     **/
    @PostMapping("/deleteBlog/{blogId}/{categoryId}")
    public Result deleteBlog(HttpServletRequest request, @PathVariable("blogId") String blogId, @PathVariable("categoryId") int categoryId) {
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        Claims claims = (Claims)request.getAttribute(CodeState.USER_CLAIMS_STR);
        if(claims!=null){
            int userId = (int)claims.get("userId");
            try {
                if(blogService.deleteBlogById(userId, blogId,categoryId) && blogService.deleteElasticsearchBlog(blogId)){
                    result.setCodeState(CodeState.SUCCESS_CODE);
                    map.put("delete", "删除成功");
                }
                else {
                    result.setCodeState(CodeState.EXCEPTION_CODE);
                    map.put(CodeState.EXCEPTION_STR, "根据ID删除博客失败！");
                }
            }
            catch (Exception e){
                result.setCodeState(CodeState.EXCEPTION_CODE);
                map.put(CodeState.EXCEPTION_STR, "根据ID删除博客服务端处理错误！");
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
     * 读取redis暂存博客
     * 已修改(controller名称,restful风格)
     * @author ZouCha
     * @date 2020-11-20 15:06:54
     * @method getTempBlog
     * @params [request]
     * @return com.shiyifan.vo.Result
     *
     **/
    @PostMapping("/getTempBlog")
    public Result getTempBlog(HttpServletRequest request) {
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        Claims claims = (Claims)request.getAttribute(CodeState.USER_CLAIMS_STR);
        if(claims!=null){
            int userId = (int)claims.get("userId");
            Myblog myblog = blogService.getTempBlog(userId);
            if(myblog!=null){
                if(!StringUtils.isEmpty(myblog.getBlogId())){
                    result.setCodeState(CodeState.SUCCESS_CODE);
                    map.put("myblog", myblog);
                }
                else {
                    result.setCodeState(CodeState.SUCCESS_CODE);
                    map.put("myblog", CodeState.SUCCESS_STR);
                }
            }
            else {
                result.setCodeState(CodeState.EXCEPTION_CODE);
                map.put(CodeState.EXCEPTION_STR, "读取redis暂存博客失败！");
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
     * 获取访问人数
     * 已修改(controller名称,restful风格)
     * @author ZouCha
     * @date 2020-11-20 15:07:22
     * @method getVisitNums
     * @params [request, dayNum]
     * @return com.shiyifan.vo.Result
     *
     **/
    @PostMapping("/getVisitNums/{dayNum}")
    public Result getVisitNums(HttpServletRequest request,@PathVariable("dayNum") int dayNum) {
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        Claims claims = (Claims)request.getAttribute(CodeState.USER_CLAIMS_STR);
        if(claims!=null){
            long visitor = VisitCountUtil.getVisitCount((int)claims.get("userId"),dayNum);
            if(visitor!=-1){
                result.setCodeState(CodeState.SUCCESS_CODE);
                map.put("visitNums", visitor);
            }
            else {
                result.setCodeState(CodeState.EXCEPTION_CODE);
                map.put(CodeState.EXCEPTION_STR, "获取访问人数失败！");
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
     * 查询博客总条数
     * 已修改(controller名称,restful风格)
     * @author ZouCha
     * @date 2020-11-20 15:07:44
     * @method selectTotalBlogNums
     * @params [request]
     * @return com.shiyifan.vo.Result
     *
     **/
    @PostMapping("/selectTotalBlogNums")
    public Result selectTotalBlogNums(HttpServletRequest request) {
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        Claims claims = (Claims)request.getAttribute(CodeState.USER_CLAIMS_STR);
        if(claims!=null){
            int totalBlogNums = blogService.selectTotalBlogNums((int)claims.get("userId"));
            if(totalBlogNums!=-1){
                result.setCodeState(CodeState.SUCCESS_CODE);
                map.put("totalBlogNums", totalBlogNums);
            }
            else {
                result.setCodeState(CodeState.EXCEPTION_CODE);
                map.put(CodeState.EXCEPTION_STR, "查询博客总条数失败！");
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

    /*---------------------------------------------------------------------------*/

    /*------------------------------公共操作-------------------------------*/

    /**
     * 获取网站总访问人数 todo
     * 已修改(controller名称,restful风格)
     * @author ZouCha
     * @date 2020-11-20 15:08:05
     * @method getAllVisitNums
     * @params []
     * @return com.shiyifan.vo.Result
     *
     **/
    @PostMapping("/getAllVisitNums")
    public Result getAllVisitNums() {
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        long visitCount = VisitCountUtil.getVisitCount(0, 2);
        if(visitCount!=-1){
            result.setCodeState(CodeState.SUCCESS_CODE);
            map.put("visitAllNums", visitCount);
        }
        else {
            result.setCodeState(CodeState.EXCEPTION_CODE);
            map.put(CodeState.EXCEPTION_STR, "获取网站总访问人数失败！");
        }
        result.setMsg(map);
        return result;
    }

    /**
     * 暂存博客到redis
     * 已修改(controller名称,restful风格)
     * @author ZouCha
     * @date 2020-11-20 15:08:42
     * @method setTempBlog
     * @params [myblog]
     * @return com.shiyifan.vo.Result
     *
     **/
    @PostMapping("/setTempBlog")
    public Result setTempBlog(@RequestBody Myblog myblog) {
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        if (blogService.setTempBlog(myblog)){
            result.setCodeState(CodeState.SUCCESS_CODE);
            map.put("setTempBlog", "未完成博客已暂存");
        }
        else {
            result.setCodeState(CodeState.EXCEPTION_CODE);
            map.put(CodeState.EXCEPTION_STR, "暂存博客到redis失败！");
        }
        result.setMsg(map);
        return result;
    }

    /**
     * 根据博客ID查找公共博客
     * 已修改(controller名称,restful风格)
     * @author ZouCha
     * @date 2020-11-20 15:09:17
     * @method selectBlogByIdForCommon
     * @params [request, blogId]
     * @return com.shiyifan.vo.Result
     *
     **/
    @PostMapping("/selectBlogByIdForCommon/{blogId}")
    public Result selectBlogByIdForCommon(HttpServletRequest request,@PathVariable("blogId") String blogId) {
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        Myblog myblog = blogService.selectBlogByIdForCommon(blogId);
        if(myblog!=null){
            VisitCountUtil.setVisitCount(request,myblog.getMyuser().getUserId());
            result.setCodeState(CodeState.SUCCESS_CODE);
            map.put("myblog", myblog);
        }
        else {
            result.setCodeState(CodeState.EXCEPTION_CODE);
            map.put(CodeState.EXCEPTION_STR, "根据博客ID查找公共博客失败！");
        }
        result.setMsg(map);
        return result;
    }

    /**
     * 分页查找公共博客
     * 已修改(controller名称,restful风格)
     * @author ZouCha
     * @date 2020-11-21 11:00:49
     * @method selectBlogsByPageForCommon
     * @params [request, pageNow, pageSize]
     * @return com.shiyifan.vo.Result
     *
     **/
    @PostMapping("/selectBlogsByPageForCommon/{pageSize}/{pageNow}")
    public Result selectBlogsByPageForCommon(HttpServletRequest request,@PathVariable("pageNow") int pageNow, @PathVariable("pageSize") int pageSize) {
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        ArrayList<Myblog> myblogs = blogService.selectBlogAllByPageForCommon(0,pageNow, pageSize);
        int nums = blogService.selectTotalBlogNumsForCommon(0);
        if(myblogs.size()!=0 && nums!=-1){
            VisitCountUtil.setVisitCount(request);
            result.setCodeState(CodeState.SUCCESS_CODE);
            map.put("myblogs", myblogs);
            map.put("nums",nums);
        }
        else {
            result.setCodeState(CodeState.EXCEPTION_CODE);
            map.put(CodeState.EXCEPTION_STR, "分页查找公共博客失败！");
        }
        result.setMsg(map);
        return result;
    }

    /**
     * 根据种类ID分页查找公共博客
     * 已修改(controller名称,restful风格)
     * @author ZouCha
     * @date 2020-11-21 11:06:45
     * @method selectBlogsByCategoryIdAndPageForCommon
     * @params [categoryId, pageNow, pageSize]
     * @return com.shiyifan.vo.Result
     *
     **/
    @PostMapping("/selectBlogsByCategoryIdAndPageForCommon/{categoryId}/{pageSize}/{pageNow}")
    public Result selectBlogsByCategoryIdAndPageForCommon(@PathVariable("categoryId")int categoryId, @PathVariable("pageNow")int pageNow, @PathVariable("pageSize")int pageSize){
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        ArrayList<Myblog> myblogs = blogService.selectBlogAllByPageForCommon(categoryId, pageNow, pageSize);
        int nums = blogService.selectTotalBlogNumsForCommon(categoryId);
        if(myblogs.size()!=0 && nums!=-1){
            result.setCodeState(CodeState.SUCCESS_CODE);
            map.put("myblogs", myblogs);
            map.put("nums",nums);
        }
        else {
            result.setCodeState(CodeState.EXCEPTION_CODE);
            map.put(CodeState.EXCEPTION_STR, "根据种类ID分页查找公共博客失败！");
        }
        result.setMsg(map);
        return result;
    }

    /**
     * 根据作者分页查找公共博客 todo
     * 已修改(controller名称,restful风格)
     * @author ZouCha
     * @date 2020-11-20 15:09:54
     * @method selectBlogByAuthorAndPageForCommon
     * @params [userId, pageNow, pageSize]
     * @return com.shiyifan.vo.Result
     *
     **/
    @PostMapping("/selectBlogByAuthorAndPageForCommon/{userId}/{pageSize}/{pageNow}")
    public Result selectBlogByAuthorAndPageForCommon(@PathVariable("userId")int userId, @PathVariable("pageNow")int pageNow, @PathVariable("pageSize")int pageSize){
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        ArrayList<Myblog> myblogs = blogService.selectBlogByAuthorForCommon(userId, pageNow, pageSize);
        int nums = blogService.selectTotalBlogNums(userId);
        if(myblogs.size()!=0 && nums!=-1){
            result.setCodeState(CodeState.SUCCESS_CODE);
            map.put("myblogs", myblogs);
            map.put("nums",nums);
        }
        else {
            result.setCodeState(CodeState.EXCEPTION_CODE);
            map.put(CodeState.EXCEPTION_STR, "根据作者分页查找公共博客失败！");
        }
        result.setMsg(map);
        return result;
    }

    /*---------------------------------------------------------------------------*/

    /*------------------------------搜索操作-------------------------------*/

    /**
     * 搜索博客
     * 已修改(controller名称,restful风格)
     * @author ZouCha
     * @date 2020-11-20 15:10:09
     * @method search
     * @params [keyword]
     * @return com.shiyifan.vo.Result
     *
     **/
    @PostMapping("/search/{keyword}")
    public Result search(@PathVariable("keyword") String keyword){
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        try {
            ArrayList<Map<String, Object>> results = blogService.searchContentPage(keyword, 1,30);
            result.setCodeState(CodeState.SUCCESS_CODE);
            map.put("results", results);
            map.put("total", results.size());
        }
        catch (Exception e) {
            log.error(e);
            result.setCodeState(CodeState.EXCEPTION_CODE);
            map.put(CodeState.EXCEPTION_STR, "搜索博客失败！");
        }
        result.setMsg(map);
        return result;
    }

    /**
     * 查找热词
     * 已修改(controller名称,restful风格)
     * @author ZouCha
     * @date 2020-11-20 15:10:54
     * @method hotkeys
     * @params []
     * @return com.shiyifan.vo.Result
     *
     **/
    @PostMapping("/hotkeys")
    public Result hotkeys(){
        Result result = new Result();
        HashMap<String, Object> map = new HashMap<>();
        InputStreamReader in=null;
        BufferedReader br=null;
        try {
            String s = "";
            in = new InputStreamReader(new FileInputStream("/home/syf/myblog/remote.txt"), StandardCharsets.UTF_8);
            br = new BufferedReader(in);
            ArrayList<HashMap<String, String>> hotkeys = new ArrayList<>();
            while ((s = br.readLine()) != null) {
                HashMap<String, String> stringmap = new HashMap<>();
                stringmap.put("value", s);
                hotkeys.add(stringmap);
            }
            result.setCodeState(CodeState.SUCCESS_CODE);
            map.put("hotkeys", hotkeys);
        }
        catch (Exception e) {
            log.error(e);
            result.setCodeState(CodeState.EXCEPTION_CODE);
            map.put(CodeState.EXCEPTION_STR, "查找热刺失败！");
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
        }
        result.setMsg(map);
        return result;
    }
}
