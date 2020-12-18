package com.shiyifan.interceptor;

import com.alibaba.druid.util.StringUtils;
import com.google.gson.Gson;
import com.shiyifan.JwtUtil;

import com.shiyifan.ResultUtil;
import com.shiyifan.pojo.CodeState;
import io.jsonwebtoken.Claims;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author ZouCha
 * @name MyJwtInterceptor
 * @date 2020-11-20 15:22:10
 **/
@Component
@Log4j2
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * @return boolean
     * @author ZouCha
     * @date 2020-11-20 15:22:16
     * @method preHandle
     * @params [request, response, handler]
     **/
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        try {
            //1.通过request获取请求头
            String authorization = request.getHeader("Authorization");
            //2.判断请求头是否为空或者以Bearer开头
            if (!StringUtils.isEmpty(authorization)) {
                if (authorization.startsWith("Bearer")) {
                    String token = authorization.replace("Bearer ", "");
                    Claims claims = jwtUtil.parseToken(token);
                    if (claims != null) {
                        request.setAttribute(CodeState.USER_CLAIMS_STR, claims);
                        return true;
                    } else {
                        returnJson(response,0);
                    }
                } else {
                    returnJson(response,0);
                }
            } else {
                returnJson(response, 0);
            }
        } catch (io.jsonwebtoken.MalformedJwtException | io.jsonwebtoken.SignatureException e) {
            returnJson(response, 0);
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            returnJson(response, 1);
        }
        return false;
    }

    /**
     * type:0 tokenError,type:1 tokenTimeLimit
     *
     * @return void
     * @author ZouCha
     * @date 2020-12-05 12:27:23
     * @method returnJson
     * @params [response, type]
     **/
    private void returnJson(HttpServletResponse response, int type) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try (PrintWriter writer = response.getWriter()) {
            if (type == 0) {
                writer.print(new Gson().toJson(ResultUtil.tokenError("TOKEN验证异常",null)));
            } else {
                writer.print(new Gson().toJson(ResultUtil.tokenTimeLimit("TOKEN已过期",null)));
            }
        } catch (IOException e) {
            log.error(e);
        }
    }

}
