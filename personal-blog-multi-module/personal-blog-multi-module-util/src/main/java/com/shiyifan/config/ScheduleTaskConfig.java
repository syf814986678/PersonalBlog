package com.shiyifan.config;

import com.aliyuncs.exceptions.ClientException;
import com.shiyifan.AliYunUtil;
import com.shiyifan.VisitorUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;


/**
 * @author ZouCha
 * @name ScheduleTaskConfig
 * @date 2020-11-20 14:57:12
 **/
@Configuration
@EnableScheduling
@Log4j2
public class ScheduleTaskConfig {

    @Autowired
    private VisitorUtil visitorUtil;

    @Autowired
    private AliYunUtil aliYunUtil;

    /**
     * @return void
     * @author ZouCha
     * @date 2020-11-20 15:01:03
     * @method deleteVisitRedisConfigureTasks
     * @params []
     **/
    @Scheduled(cron = "0 0 0 1/1 * ? ")
    private void deleteVisitorConfigureTasks() {
        visitorUtil.deleteVisitor(1);
    }

    /**
     * @return void
     * @author ZouCha
     * @date 2020-11-20 15:00:41
     * @method DcdnConfigureTasks
     * @params []
     **/
    @Scheduled(cron = "0 0/30 * * * ? ")
    private void dcdnConfigureTasks() throws ClientException {
        try {
            aliYunUtil.refreshDcdn();
            aliYunUtil.preLoadDcdn();
        }
        catch (ClientException e) {
            log.error("阿里云Dcdn错误"+e.toString());
            throw new ClientException("阿里云Dcdn错误"+e.toString());
        }

    }

}
