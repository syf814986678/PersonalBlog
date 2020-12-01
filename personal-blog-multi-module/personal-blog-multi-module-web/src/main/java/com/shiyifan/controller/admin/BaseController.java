package com.shiyifan.controller.admin;

import com.shiyifan.pojo.Result;
import lombok.Value;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author ZouCha
 * @name BaseController
 * @date 2020-12-01 18:12
 **/
public interface BaseController<T> {
    Result selectBlogByBlogId(String blogId);
}
