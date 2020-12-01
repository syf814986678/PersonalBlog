package com.shiyifan.Interceptor;

import com.alibaba.druid.util.StringUtils;
import com.aliyuncs.dcdn.transform.v20180115.DescribeDcdnRefreshTasksResponseUnmarshaller;
import com.google.gson.Gson;
import com.shiyifan.JwtUtil;

import com.shiyifan.pojo.CodeState;
import com.shiyifan.pojo.Result;
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
                        returnJson(response, CodeState.TOKEN_ERROR_CODE, CodeState.TOKEN_ERROR_STR);
                    }
                } else {
                    returnJson(response, CodeState.TOKEN_ERROR_CODE, CodeState.TOKEN_ERROR_STR);
                }
            } else {
                returnJson(response, CodeState.TOKEN_ERROR_CODE, CodeState.TOKEN_ERROR_STR);
            }
        } catch (io.jsonwebtoken.MalformedJwtException | io.jsonwebtoken.SignatureException e) {
            returnJson(response, CodeState.TOKEN_ERROR_CODE, CodeState.TOKEN_ERROR_STR);
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            returnJson(response, CodeState.TOKEN_TIME_LIMIT_CODE, CodeState.TOKEN_TIME_LIMIT_STR);
        }
        return false;
    }

    /**
     * @return void
     * @author ZouCha
     * @date 2020-12-01 14:46:45
     * @method returnJson
     * @params [response, json]
     **/
    private void returnJson(HttpServletResponse response, int codeState, String msg) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try (PrintWriter writer = response.getWriter()) {
            Result result = new Result();
            result.setCodeState(codeState);
            result.setMsg(msg);
            writer.print(new Gson().toJson(result));
        } catch (IOException e) {
            log.error(e);
        }
    }

}
