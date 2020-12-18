package com.shiyifan.controller.admin;

import com.shiyifan.BlogUtil;
import com.shiyifan.ResultUtil;
import com.shiyifan.VisitorUtil;
import com.shiyifan.pojo.CodeState;
import com.shiyifan.pojo.Result;
import io.jsonwebtoken.Claims;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * @author ZouCha
 * @name AdminBlogController
 * @date 2020-12-02 18:35
 **/

@RestController
@Log4j2
@RequestMapping("/admin/blog")
public class AdminBlogController {

    @Autowired
    private VisitorUtil visitorUtil;

    @Autowired
    private BlogUtil blogUtil;

    /**
     * @return java.lang.String
     * @author ZouCha
     * @date 2020-12-17 13:57:13
     * @method hello
     * @params []
     **/
    @PostMapping("/selectTotalBlogsForAdmin")
    public Result selectTotalBlogsForAdmin(HttpServletRequest request) throws Exception {
        Claims claims = null;
        Integer totalBlogsForAdmin = null;
        try {
            claims = (Claims) request.getAttribute(CodeState.USER_CLAIMS_STR);
            int userId = (int) claims.get("userId");
            totalBlogsForAdmin = blogUtil.getTotalBlogsForAdmin(userId);
            if (totalBlogsForAdmin == null) {
                blogUtil.setTotalBlogsForAdmin(userId);
            }
            totalBlogsForAdmin = blogUtil.getTotalBlogsForAdmin(userId);
            return ResultUtil.success(totalBlogsForAdmin);
        } catch (Exception e) {
            log.error("selectTotalBlogNumsForAdmin错误" + e.toString());
            throw new Exception("selectTotalBlogNumsForAdmin错误" + e.toString());
        }
    }
}
