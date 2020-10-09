package com.shiyifan.utils;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Log4j2
public class CountUvUtil {

    @Autowired
    private RedisUtil redisUtill;

    public long setVisitor(int userId){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formatDate = formatter.format(date);
        String remoteAddr = request.getRemoteAddr();
        System.out.println(remoteAddr);
        try {
            return redisUtill.addHyperloglog(userId+formatDate, remoteAddr);
        }
        catch (Exception e){
            log.error(e);
            return 0;
        }
    }

    public long getVisitor(int userId,int dayNum){
        Date date = new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24*dayNum);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formatDate = formatter.format(date);
        System.out.println(formatDate);
        try {
            return redisUtill.getHyperloglogSize(userId+formatDate);
        }
        catch (Exception e){
            log.error(e);
            return 0;
        }
    }
}
