package com.shiyifan.controller.admin;

import com.shiyifan.BlogService;
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
import java.util.HashMap;


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



}
