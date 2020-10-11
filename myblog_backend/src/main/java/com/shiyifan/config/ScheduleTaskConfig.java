package com.shiyifan.config;

import com.shiyifan.service.UserServiceImpl;
import com.shiyifan.utils.RedisUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;


@Configuration
@EnableScheduling
@Log4j2
public class ScheduleTaskConfig{

    @Autowired
    private RedisUtil redisUtill;

    @Autowired
    private UserServiceImpl userService;

    @Scheduled(cron = "0 0 0 1/1 * ? ")
    private void configureTasks() {
        try {
            Date date = new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 2);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String formatDate = formatter.format(date);
            boolean wholeVisit = redisUtill.deleteHyperloglog(formatDate);
            log.warn("deleteWholeVisit->"+formatDate+"->"+wholeVisit);

            ArrayList<Integer> allId = userService.getAllId();
            Iterator<Integer> iterator = allId.iterator();
            while (iterator.hasNext()){
                Integer integer = iterator.next();
                boolean userVisit = redisUtill.deleteHyperloglog(integer + formatDate);
                log.warn("deleteUserVisit->"+formatDate+"->"+integer+"->"+userVisit);
            }
        }
        catch (Exception e){
            log.error(e);
        }
    }
}
