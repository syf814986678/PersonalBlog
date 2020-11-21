package com.shiyifan.interceptor;

import com.alibaba.druid.util.StringUtils;
import com.shiyifan.constant.CodeState;
import com.shiyifan.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ZouCha
 * @name MyJwtInterceptor
 * @date 2020-11-20 15:22:10
 *
 **/
@Component
public class MyJwtInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private JwtUtil jwtUtil;
    /**
     *
     * @author ZouCha
     * @date 2020-11-20 15:22:16
     * @method preHandle
     * @params [request, response, handler]
     * @return boolean
     *
     **/
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            //1.通过request获取请求头
            String authorization = request.getHeader("Authorization");
            //2.判断请求头是否为空或者以Bearer开头
            if(!StringUtils.isEmpty(authorization)){
                if(authorization.startsWith("Bearer")){
                    String token=authorization.replace("Bearer ", "");
                    Claims claims = jwtUtil.parseToken(token);
                    if(claims!=null){
                        request.setAttribute(CodeState.USER_CLAIMS_STR,claims);
                    }
                }
            }
            else {
                request.setAttribute(CodeState.TOKEN_ERROR_STR,"token验证非法！请重新登录");
            }
        }
        catch (io.jsonwebtoken.MalformedJwtException | io.jsonwebtoken.SignatureException e){
            request.setAttribute(CodeState.TOKEN_ERROR_STR,"token验证非法！请重新登录");
        }
        catch (io.jsonwebtoken.ExpiredJwtException e){
            request.setAttribute(CodeState.TOKEN_TIME_LIMIT_STR,"token已过期！请重新登录");
        }
        return true;
    }
}
