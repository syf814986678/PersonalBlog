package com.shiyifan;


import com.shiyifan.mapper.BlogMapper;
import com.shiyifan.pojo.Blog;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

/**
 * @author ZouCha
 * @name BlogService
 * @date 2020-11-20 15:23:33
 **/
@Service
@Order
@Log4j2
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogMapper blogMapper;


    /**
     *
     * @author ZouCha
     * @date 2020-12-01 15:47:50
     * @method selectBlogByBlogId
     * @params [blogId]
     * @return com.shiyifan.pojo.Blog
     *
     **/
    @Override
    public Blog selectBlogByBlogId(String blogId) {
        return blogMapper.selectBlogByBlogId(blogId);
    }
}
