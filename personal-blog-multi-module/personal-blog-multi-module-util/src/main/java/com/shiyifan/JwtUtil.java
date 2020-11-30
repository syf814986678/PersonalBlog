package com.shiyifan;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author ZouCha
 * @name JwtUtil
 * @date 2020-11-30 13:45
 **/
@Component
@Log4j2
public class JwtUtil {
    @Value("${jwt.ttl}")
    private Long ttl;
    @Value("${jwt.key}")
    private String key;
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
        Long exp=System.currentTimeMillis()+ttl;
        //创建token
        return Jwts.builder().claim("userId", userId)
                .claim("userName", userName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(exp))
                .signWith(SignatureAlgorithm.HS256, key)
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
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }
}
