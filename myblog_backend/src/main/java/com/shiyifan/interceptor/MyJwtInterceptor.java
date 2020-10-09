package com.shiyifan.interceptor;

import com.alibaba.druid.util.StringUtils;
import com.shiyifan.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class MyJwtInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private JwtUtil jwtUtil;
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
                        request.setAttribute("user_claims",claims);
                    }
                }
            }
            else {
                request.setAttribute("tokenError","token验证非法！请重新登录");
            }
        }
        catch (io.jsonwebtoken.MalformedJwtException | io.jsonwebtoken.SignatureException e){
            request.setAttribute("tokenError","token验证非法！请重新登录");
        } catch (io.jsonwebtoken.ExpiredJwtException e){
            request.setAttribute("tokenError","token已过期！请重新登录");
        }
        return true;
    }
}
