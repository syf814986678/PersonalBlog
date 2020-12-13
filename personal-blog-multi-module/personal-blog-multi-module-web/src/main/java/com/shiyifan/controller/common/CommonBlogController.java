package com.shiyifan.controller.common;

import com.shiyifan.BlogService;
import com.shiyifan.ResultUtil;
import com.shiyifan.pojo.Blog;
import com.shiyifan.pojo.CodeState;
import com.shiyifan.pojo.Result;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    @Value("${searchPath}")
    private String searchPath;

    /**
     * @return com.shiyifan.pojo.Result
     * @author ZouCha
     * @date 2020-12-12 16:25:39
     * @method selectBlogListByPageForCommon
     * @params [categoryId, pageNow, pageSize]
     **/
    @PostMapping("/selectBlogListByPageForCommon/{categoryId}/{pageNow}/{pageSize}")
    public Result selectBlogListByPageForCommon(@PathVariable("categoryId") int categoryId, @PathVariable("pageNow") int pageNow, @PathVariable("pageSize") int pageSize) throws Exception {
        ArrayList<Blog> blogListForCommon = null;
        try {
            blogListForCommon = blogService.selectBlogListByPageForCommon(categoryId, pageNow, pageSize);
        } catch (Exception e) {
            log.error("分页查找公共博客错误" + e.toString());
            throw new Exception("分页查找公共博客错误" + e.toString());
        }
        return ResultUtil.success(blogListForCommon);
    }

    /**
     * @return com.shiyifan.pojo.Result
     * @author ZouCha
     * @date 2020-12-12 16:25:35
     * @method selectTotalBlogsForCommon
     * @params [categoryId]
     **/
    @PostMapping("/selectTotalBlogsForCommon/{categoryId}")
    public Result selectTotalBlogsForCommon(@PathVariable("categoryId") int categoryId) throws Exception {
        Integer totalBlogsForCommon = null;
        try {
            totalBlogsForCommon = blogService.selectTotalBlogsForCommon(categoryId);
        } catch (Exception e) {
            log.error("查找公共博客数量错误" + e.toString());
            throw new Exception("查找公共博客数量错误" + e.toString());
        }
        return ResultUtil.success(totalBlogsForCommon);
    }

    /**
     * @return com.shiyifan.pojo.Result
     * @author ZouCha
     * @date 2020-12-12 16:25:30
     * @method selectBlogByIdForCommon
     * @params [blogId]
     **/

    @PostMapping("/selectBlogByIdForCommon/{blogId}")
    public Result selectBlogByIdForCommon(@PathVariable("blogId") String blogId) throws Exception {
        Blog blog = null;
        try {
            blog = blogService.selectBlogByIdForCommon(blogId);
            if(blog!=null){
                return ResultUtil.success(blog);
            }
            else {
                return ResultUtil.operationError("博客不存在", null);
            }
        } catch (Exception e) {
            log.error("根据ID查找公共博客错误" + e.toString());
            throw new Exception("根据ID查找公共博客错误" + e.toString());
        }
    }

    /**
     * 查找热词
     * 已修改(controller名称,restful风格)
     *
     * @return com.shiyifan.vo.Result
     * @author ZouCha
     * @date 2020-11-20 15:10:54
     * @method hotkeys
     * @params []
     **/
    @PostMapping("/hotkeys")
    public Result hotkeys() throws Exception {
        InputStreamReader in = null;
        BufferedReader br = null;
        ArrayList<HashMap<String, String>> hotkeys = new ArrayList<>();
        try {
            String s = "";
            in = new InputStreamReader(new FileInputStream(searchPath + "/remote.txt"), StandardCharsets.UTF_8);
            br = new BufferedReader(in);
            while ((s = br.readLine()) != null) {
                HashMap<String, String> stringMap = new HashMap<>(1);
                stringMap.put("value", s);
                hotkeys.add(stringMap);
            }
        } catch (Exception e) {
            log.error("查找热词错误" + e.toString());
            throw new Exception("查找热词错误" + e.toString());
        }
        return ResultUtil.success(hotkeys);
    }

    /**
     * 搜索博客
     * 已修改(controller名称,restful风格)
     *
     * @return com.shiyifan.vo.Result
     * @author ZouCha
     * @date 2020-11-20 15:10:09
     * @method search
     * @params [keyword]
     **/
    @PostMapping("/search/{keyword}")
    public Result search(@PathVariable("keyword") String keyword) throws Exception {
        ArrayList<Map<String, Object>> list = null;
        try {
            list = blogService.searchContentByPage(keyword, 1, 30);
        } catch (Exception e) {
            log.error("搜索博客错误" + e.toString());
            throw new Exception("搜索博客错误" + e.toString());
        }
        return ResultUtil.success(list);
    }
}
