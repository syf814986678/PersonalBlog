package com.shiyifan.handler;

import com.aliyun.oss.OSSException;
import com.shiyifan.ResultUtil;
import com.shiyifan.pojo.Result;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author ZouCha
 * @name GlobalExceptionHandler
 * @date 2020-12-05 13:17
 **/
@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {
    /**
     * 表示让Spring捕获到所有抛出的SignException异常，并交由这个被注解的方法处理
     */
    @ExceptionHandler(Exception.class)
    public Result handleException() {
        return ResultUtil.exception("系统出现异常!", null);
    }

    @ExceptionHandler(OSSException.class)
    public Result handleOssException() {
        return ResultUtil.ossException("Oss回调异常！", null);
    }
}
