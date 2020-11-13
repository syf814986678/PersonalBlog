package com.shiyifan.config;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dcdn.model.v20180115.PreloadDcdnObjectCachesRequest;
import com.aliyuncs.dcdn.model.v20180115.PreloadDcdnObjectCachesResponse;
import com.aliyuncs.dcdn.model.v20180115.RefreshDcdnObjectCachesRequest;
import com.aliyuncs.dcdn.model.v20180115.RefreshDcdnObjectCachesResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;
import com.shiyifan.constant.MyConstant;
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


/**
 * @author 81498
 */
@Configuration
@EnableScheduling
@Log4j2
public class ScheduleTaskConfig{

    @Autowired
    private RedisUtil redisUtill;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private MyConstant myConstant;

    @Scheduled(cron = "0 0 0 1/1 * ? ")
    private void deleteVisitRedisConfigureTasks() {
       deleteVisitRedis();
    }
    @Scheduled(cron = "0 0/30 * * * ? ")
    private void DcdnConfigureTasks() {
        refreshDcdn();
        preLoadDcdn();
    }


    private void deleteVisitRedis(){
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
    public void refreshDcdn(){
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", myConstant.getAccessKeyId(), myConstant.getAccessKeySecret());
        IAcsClient client = new DefaultAcsClient(profile);
        RefreshDcdnObjectCachesRequest request = new RefreshDcdnObjectCachesRequest();
        request.setObjectPath("https://www.chardance.cloud/#/index/bloglist/all/all");

        try {
            RefreshDcdnObjectCachesResponse response = client.getAcsResponse(request);
            log.warn(new Gson().toJson(response));
        } catch (ClientException e) {
            log.error("ErrCode:" + e.getErrCode());
            log.error("ErrMsg:" + e.getErrMsg());
            log.error("RequestId:" + e.getRequestId());
        }
    }
    public void preLoadDcdn(){
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", myConstant.getAccessKeyId(), myConstant.getAccessKeySecret());
        IAcsClient client = new DefaultAcsClient(profile);
        PreloadDcdnObjectCachesRequest request = new PreloadDcdnObjectCachesRequest();
        request.setObjectPath("https://www.chardance.cloud/#/index/bloglist/all/all");

        try {
            PreloadDcdnObjectCachesResponse response = client.getAcsResponse(request);
            log.warn(new Gson().toJson(response));
        } catch (ClientException e) {
            log.error("ErrCode:" + e.getErrCode());
            log.error("ErrMsg:" + e.getErrMsg());
            log.error("RequestId:" + e.getRequestId());
        }
    }
}
