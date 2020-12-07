package com.shiyifan.controller.common;

import com.shiyifan.BlogService;
import com.shiyifan.ResultUtil;
import com.shiyifan.pojo.Blog;
import com.shiyifan.pojo.Result;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * @author ZouCha
 * @name AdminBlogController
 * @date 2020-12-02 18:35
 **/
@RestController
@CrossOrigin
@Log4j2
@RequestMapping("/common/blog")
public class CommonBlogController {
    @Autowired
    private BlogService blogService;

    @PostMapping("/selectBlogListByPageForCommon/{categoryId}/{pageNow}/{pageSize}")
    public Result selectBlogListByPageForCommon(@PathVariable("categoryId") int categoryId, @PathVariable("pageNow") int pageNow, @PathVariable("pageSize") int pageSize) throws Exception {
        ArrayList<Blog> blogListForCommon = null;
        try {
            blogListForCommon = blogService.selectBlogListByPageForCommon(categoryId, pageNow, pageSize);
        } catch (Exception e) {
            log.error("分页查找公共博客错误"+e.toString());
            throw new Exception("分页查找公共博客错误"+e.toString());
        }
        return ResultUtil.success(blogListForCommon);
    }

    @PostMapping("/selectTotalBlogsForCommon/{categoryId}")
    public Result selectTotalBlogsForCommon(@PathVariable("categoryId") int categoryId) throws Exception {
        Integer totalBlogsForCommon = null;
        try {
            totalBlogsForCommon = blogService.selectTotalBlogsForCommon(categoryId);
        } catch (Exception e) {
            log.error("查找公共博客数量错误"+e.toString());
            throw new Exception("查找公共博客数量错误"+e.toString());
        }
        return ResultUtil.success(totalBlogsForCommon);
    }

    @PostMapping("/selectBlogByIdForCommon/{blogId}")
    public Result selectBlogByIdForCommon(@PathVariable("blogId") String blogId) throws Exception {
        Blog blog = null;
        try {
            blog = blogService.selectBlogByIdForCommon(blogId);
        } catch (Exception e) {
            log.error("根据ID查找公共博客错误"+e.toString());
            throw new Exception("根据ID查找公共博客错误"+e.toString());
        }
        return ResultUtil.success(blog);
    }
}
