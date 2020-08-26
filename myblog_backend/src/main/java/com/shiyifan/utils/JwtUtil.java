package com.shiyifan.utils;

import com.shiyifan.constant.MyConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
public class JwtUtil {
    /*
        设置认证token
     */

    @Autowired
    private MyConstant myConstant;

    public String createToken(int userid,String username){
        Long exp=System.currentTimeMillis()+myConstant.getTtl();
        //创建token
        return Jwts.builder().claim("userid", userid)
                .claim("username", username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(exp))
                .signWith(SignatureAlgorithm.HS256, myConstant.getKey())
                .compact();
    }
    /*
        解析token
     */
    public Claims parseToken(String token){
        return Jwts.parser().setSigningKey(myConstant.getKey()).parseClaimsJws(token).getBody();
    }
}
