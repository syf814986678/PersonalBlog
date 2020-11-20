package com.shiyifan.utils;

import com.shiyifan.constant.MyConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
/**
 *
 * @author ZouCha
 * @name JwtUtil
 * @date 2020-11-20 15:31:58
 *
 **/
@Component
public class JwtUtil {

    @Autowired
    private MyConstant myConstant;
    /**
     * 设置认证token
     * @author ZouCha
     * @date 2020-11-20 15:32:10
     * @method createToken
     * @params [userId, userName]
     * @return java.lang.String
     *
     **/
    public String createToken(int userId, String userName){
        Long exp=System.currentTimeMillis()+myConstant.getTtl();
        //创建token
        return Jwts.builder().claim("userId", userId)
                .claim("userName", userName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(exp))
                .signWith(SignatureAlgorithm.HS256, myConstant.getKey())
                .compact();
    }
    /**
     * 解析token
     * @author ZouCha
     * @date 2020-11-20 15:32:24
     * @method parseToken
     * @params [token]
     * @return io.jsonwebtoken.Claims
     *
     **/
    public Claims parseToken(String token){
        return Jwts.parser().setSigningKey(myConstant.getKey()).parseClaimsJws(token).getBody();
    }
}
