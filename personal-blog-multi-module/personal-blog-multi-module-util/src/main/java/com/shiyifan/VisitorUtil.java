package com.shiyifan;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ZouCha
 * @name VisitorUtil
 * @date 2020-11-20 15:34:45
 **/
@Component
@Log4j2
public class VisitorUtil {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * @return long
     * @author ZouCha
     * @date 2020-11-20 15:35:05
     * @method setVisitCount
     * @params [request, userId]
     **/
    public long setVisitCount(HttpServletRequest request, int userId) throws Exception {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个IP值，第一个为真实IP。
            int index = ip.indexOf(',');
            if (index != -1) {
                ip = ip.substring(0, index);
            }
        } else {
            ip = request.getRemoteAddr();
        }
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formatDate = formatter.format(date);
        try {
            return redisUtil.addHyperloglog(userId + formatDate, ip);
        } catch (Exception e) {
            log.error("setVisitCount1错误" + e.toString());
            throw new Exception("setVisitCount1错误" + e.toString());
        }
    }

    /**
     * @return long
     * @author ZouCha
     * @date 2020-11-20 15:35:09
     * @method setVisitCount
     * @params [request]
     **/
    public long setVisitCount(HttpServletRequest request) throws Exception {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个IP值，第一个为真实IP。
            int index = ip.indexOf(',');
            if (index != -1) {
                ip = ip.substring(0, index);
            }
        } else {
            ip = request.getRemoteAddr();
        }
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formatDate = formatter.format(date);
        try {
            return redisUtil.addHyperloglog(formatDate, ip);
        } catch (Exception e) {
            log.error("setVisitCount2错误" + e.toString());
            throw new Exception("setVisitCount2错误" + e.toString());
        }
    }

    /**
     * @return long
     * @author ZouCha
     * @date 2020-12-19 14:03:42
     * @method getVisitCount
     * @params [userId, dayNum]
     **/
    public long getVisitCount(int userId, int dayNum) throws Exception {
        Date date = new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24 * dayNum);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formatDate = formatter.format(date);
        try {
            return redisUtil.getHyperloglogSize(userId + formatDate);
        } catch (Exception e) {
            log.error("getVisitCount1错误" + e.toString());
            throw new Exception("getVisitCount1错误" + e.toString());
        }
    }

    /**
     * @return long
     * @author ZouCha
     * @date 2020-12-19 14:04:25
     * @method getVisitCount
     * @params [dayNum]
     **/
    public long getVisitCount(int dayNum) throws Exception {
        Date date = new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24 * dayNum);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formatDate = formatter.format(date);
        try {
            return redisUtil.getHyperloglogSize(formatDate);
        } catch (Exception e) {
            log.error("getVisitCount2错误" + e.toString());
            throw new Exception("getVisitCount2错误" + e.toString());
        }
    }

    /**
     * @return void
     * @author ZouCha
     * @date 2020-12-01 15:15:17
     * @method deleteVisitor
     * @params []
     **/
    public void deleteVisitor(int userId) throws Exception {
        try {
            Date date = new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 2);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String formatDate = formatter.format(date);
            boolean wholeVisit = redisUtil.deleteHyperloglog(formatDate);
            log.info("deleteWholeVisitor->" + formatDate + "->" + wholeVisit);
            boolean userVisit = redisUtil.deleteHyperloglog(userId + formatDate);
            log.info("deleteUserVisitor->" + formatDate + "->" + userId + "->" + userVisit);
//            for (Integer integer : allUserId) {
//                boolean userVisit = deleteHyperloglog(integer + formatDate);
//                log.info("deleteUserVisitor->" + formatDate + "->" + integer + "->" + userVisit);
//            }
        } catch (Exception e) {
            log.error("deleteVisitor错误" + e.toString());
            throw new Exception("deleteVisitor错误" + e.toString());
        }
    }
}
