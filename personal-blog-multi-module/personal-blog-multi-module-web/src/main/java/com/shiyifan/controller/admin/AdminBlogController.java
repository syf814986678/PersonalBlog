package com.shiyifan.controller.admin;

import com.shiyifan.BlogService;
import com.shiyifan.pojo.Blog;
import com.shiyifan.pojo.CodeState;
import com.shiyifan.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author ZouCha
 * @name AdminBlogController
 * @date 2020-12-02 18:35
 **/
@RestController
@CrossOrigin
@RequestMapping("/admin/blog")
public class AdminBlogController {
    @Autowired
    private BlogService blogService;

    /**
     * @return com.shiyifan.pojo.Result
     * @author ZouCha
     * @date 2020-12-02 19:17:35
     * @method selectBlogByBlogId
     * @params [blogId]
     **/
    public Result selectBlogByBlogId(String blogId) {
        Blog blog = blogService.selectBlogByBlogId("02039b6a22164e898e2f235b9e5f9cb3");
        Result result = new Result();
        result.setCodeState(CodeState.SUCCESS_CODE);
        result.setMsg(CodeState.SUCCESS_STR);
        HashMap<String, Object> hashMap = new HashMap<>(16);
        hashMap.put("Blog", blog);
        result.setData(hashMap);
        return result;
    }
}
