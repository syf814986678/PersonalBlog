package com.shiyifan.controller.admin;

import com.shiyifan.BlogService;
import com.shiyifan.RabbitmqService;
import com.shiyifan.ResultUtil;
import com.shiyifan.VisitorUtil;
import com.shiyifan.pojo.Blog;
import com.shiyifan.pojo.CodeState;
import com.shiyifan.pojo.Result;
import io.jsonwebtoken.Claims;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;


/**
 * @author ZouCha
 * @name AdminBlogController
 * @date 2020-12-02 18:35
 **/

@RestController
@Log4j2
@RequestMapping("/blog/admin")
public class AdminBlogController {

    @Autowired
    private VisitorUtil visitorUtil;

    @Autowired
    private BlogService blogService;

    @Autowired
    private RabbitmqService rabbitmqService;

    /**
     * 获取访问人数
     * 已修改(controller名称,restful风格)
     *
     * @return com.shiyifan.vo.Result
     * @author ZouCha
     * @date 2020-11-20 15:07:22
     * @method getVisitNums
     * @params [request, dayNum]
     **/
    @PostMapping("/getVisitNumsForAdmin/{dayNum}")
    public Result getVisitNumsForAdmin(HttpServletRequest request, @PathVariable("dayNum") int dayNum) throws Exception {
        Claims claims = null;
        Long visitor = null;
        try {
            claims = (Claims) request.getAttribute(CodeState.USER_CLAIMS_STR);
            int userId = (int) claims.get("userId");
            visitor = visitorUtil.getVisitCount(userId, dayNum);
        } catch (Exception e) {
            log.error("getVisitNumsForAdmin错误" + e.toString());
            throw new Exception("getVisitNumsForAdmin错误" + e.toString());
        }
        return ResultUtil.success(visitor);
    }

    /**
     * @return com.shiyifan.pojo.Result
     * @author ZouCha
     * @date 2020-12-20 13:56:30
     * @method selectBlogListByPageForAdmin
     * @params [request, pageNow, pageSize]
     **/
    @PostMapping("/selectBlogListByPageForAdmin/{categoryId}/{pageSize}/{pageNow}")
    public Result selectBlogListByPageForAdmin(HttpServletRequest request, @PathVariable("categoryId") int categoryId, @PathVariable("pageNow") int pageNow, @PathVariable("pageSize") int pageSize) throws Exception {
        Claims claims = null;
        ArrayList<Blog> blogListForAdmin = null;
        try {
            claims = (Claims) request.getAttribute(CodeState.USER_CLAIMS_STR);
            int userId = (int) claims.get("userId");
            blogListForAdmin = blogService.selectBlogListByPageForAdmin(userId, categoryId, pageNow, pageSize);
        } catch (Exception e) {
            log.error("selectBlogListByPageForAdmin错误" + e.toString());
            throw new Exception("selectBlogListByPageForAdmin错误" + e.toString());
        }
        return ResultUtil.success(blogListForAdmin);
    }

    /**
     * 获取总博客数
     *
     * @return java.lang.String
     * @author ZouCha
     * @date 2020-12-17 13:57:13
     * @method hello
     * @params []
     **/
    @PostMapping("/selectTotalBlogsForAdmin/{categoryId}")
    public Result selectTotalBlogsForAdmin(HttpServletRequest request, @PathVariable("categoryId") int categoryId) throws Exception {
        Claims claims = null;
        Integer totalBlogsForAdmin = null;
        try {
            claims = (Claims) request.getAttribute(CodeState.USER_CLAIMS_STR);
            int userId = (int) claims.get("userId");
            totalBlogsForAdmin = blogService.selectTotalBlogsForAdmin(userId, categoryId);
        } catch (Exception e) {
            log.error("selectTotalBlogsForAdmin错误" + e.toString());
            throw new Exception("selectTotalBlogsForAdmin错误" + e.toString());
        }
        return ResultUtil.success(totalBlogsForAdmin);
    }

    /**
     * @return com.shiyifan.pojo.Result
     * @author ZouCha
     * @date 2020-12-30 09:30:31
     * @method getTempBlogForAdmin
     * @params [request]
     **/
    @PostMapping("/getTempBlogForAdmin")
    public Result getTempBlogForAdmin(HttpServletRequest request) throws Exception {
        Claims claims = null;
        Blog tempBlogForAdmin = null;
        try {
            claims = (Claims) request.getAttribute(CodeState.USER_CLAIMS_STR);
            int userId = (int) claims.get("userId");
            tempBlogForAdmin = blogService.getTempBlogForAdmin(userId);
        } catch (Exception e) {
            log.error("getTempBlogForAdmin错误" + e.toString());
            throw new Exception("getTempBlogForAdmin错误" + e.toString());
        }
        return ResultUtil.success(tempBlogForAdmin);
    }

    /**
     * @return com.shiyifan.pojo.Result
     * @author ZouCha
     * @date 2020-12-30 10:03:55
     * @method setTempBlogForAdmin
     * @params [request, blog]
     **/
    @PostMapping("/setTempBlogForAdmin")
    public Result setTempBlogForAdmin(HttpServletRequest request, @RequestBody Blog blog) throws Exception {
        Claims claims = null;
        try {
            claims = (Claims) request.getAttribute(CodeState.USER_CLAIMS_STR);
            int userId = (int) claims.get("userId");
            blogService.setTempBlogForAdmin(userId, blog);
        } catch (Exception e) {
            log.error("setTempBlogForAdmin错误" + e.toString());
            throw new Exception("setTempBlogForAdmin错误" + e.toString());
        }
        return ResultUtil.success(null);
    }

    /**
     * @return com.shiyifan.pojo.Result
     * @author ZouCha
     * @date 2020-12-30 14:26:59
     * @method addBlogForAdmin
     * @params [request, blog]
     **/
    @PostMapping("/addBlogForAdmin")
    public Result addBlogForAdmin(HttpServletRequest request, @RequestBody Blog blog) throws Exception {
        Claims claims = null;
        try {
            claims = (Claims) request.getAttribute(CodeState.USER_CLAIMS_STR);
            int userId = (int) claims.get("userId");
            rabbitmqService.insertBlogProvider(blog, userId);
//            blogService.addBlogForAdmin(userId, blog);
        } catch (Exception e) {
            log.error("addBlogForAdmin错误" + e.toString());
            throw new Exception("addBlogForAdmin错误" + e.toString());
        }
        return ResultUtil.success(null);
    }

    /**
     * @return com.shiyifan.pojo.Result
     * @author ZouCha
     * @date 2021-01-02 16:56:51
     * @method deleteBlogForAdmin
     * @params [request, blogId, categoryId]
     **/
    @PostMapping("/deleteBlogForAdmin/{blogId}/{categoryId}")
    public Result deleteBlogForAdmin(HttpServletRequest request, @PathVariable("blogId") String blogId, @PathVariable("categoryId") int categoryId) throws Exception {
        Claims claims = null;
        try {
            claims = (Claims) request.getAttribute(CodeState.USER_CLAIMS_STR);
            int userId = (int) claims.get("userId");
            if (!blogService.deleteBlogForAdmin(userId, blogId, categoryId)) {
                throw new Exception("触发防刷机制，删除博客失败");
            }
        } catch (Exception e) {
            log.error("deleteBlogForAdmin错误" + e.toString());
            throw new Exception("deleteBlogForAdmin错误" + e.toString());
        }
        return ResultUtil.success(null);
    }

    /**
     * @return com.shiyifan.pojo.Result
     * @author ZouCha
     * @date 2021-01-02 16:56:56
     * @method updateBlogForAdmin
     * @params [request, blog]
     **/
    @PostMapping("/updateBlogForAdmin")
    public Result updateBlogForAdmin(HttpServletRequest request, @RequestBody Blog blog) throws Exception {
        Claims claims = null;
        try {
            claims = (Claims) request.getAttribute(CodeState.USER_CLAIMS_STR);
            int userId = (int) claims.get("userId");
            Integer value = blogService.updateBlogForAdmin(userId, blog);
            if (value == 0) {
                return ResultUtil.success(null);
            } else if (value == 1) {
                return ResultUtil.exception("触发防刷机制，更新博客失败", null);
            } else if (value == 2) {
                return ResultUtil.operationError("类别ID不存在！", null);
            } else {
                return ResultUtil.exception("其他异常", null);
            }
        } catch (Exception e) {
            log.error("updateBlogForAdmin错误" + e.toString());
            throw new Exception("updateBlogForAdmin错误" + e.toString());
        }

    }

    @PostMapping("/selectBlogByIdForAdmin/{blogId}")
    public Result selectBlogByIdForAdmin(HttpServletRequest request, @PathVariable("blogId") String blogId) throws Exception {
        Claims claims = null;
        Blog blog = null;
        try {
            claims = (Claims) request.getAttribute(CodeState.USER_CLAIMS_STR);
            int userId = (int) claims.get("userId");
            blog = blogService.selectBlogByIdForAdmin(userId, blogId);
        } catch (Exception e) {
            log.error("updateBlogForAdmin错误" + e.toString());
            throw new Exception("updateBlogForAdmin错误" + e.toString());
        }
        return ResultUtil.success(blog);
    }
}
