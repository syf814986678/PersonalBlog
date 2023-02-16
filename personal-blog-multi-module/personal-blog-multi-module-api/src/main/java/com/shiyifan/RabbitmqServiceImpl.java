package com.shiyifan;

import com.rabbitmq.client.Channel;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @author ZouCha
 * @name RabbitmqServiceImpl
 * @date 2023-02-16 14:29
 **/
@Service
@Log4j2
public class RabbitmqServiceImpl implements RabbitmqService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.blogQueue}")
    private String bookQueue;

    @Override
    public boolean insertBlogProvider(String bookUrl, int regId, int userId) throws Exception {
//        try {
//            HashMap<String, Object> map = new HashMap<>(6);
//            map.put("bookUrl", bookUrl);
//            map.put("regId", regId);
//            map.put("userId", userId);
//            rabbitTemplate.convertAndSend(bookQueue, map);
//        } catch (Exception e) {
//            log.error(e);
//            e.printStackTrace();
//            throw new Exception(e.getMessage());
//        }
//        return true;
    }

    @Override
    @RabbitListener(queuesToDeclare = @Queue("${spring.rabbitmq.blogQueue}"))
    public void insertBlogConsumer(HashMap<String, Object> map, Message message, Channel channel) throws Exception {
//        String bookUrl = (String) map.get("bookUrl");
//        int regId = (int) map.get("regId");
//        int userId = (int) map.get("userId");
//        try {
//            bookService.insertBook(bookUrl, regId, userId);
//        } catch (Exception e) {
//            DingTalkUtil.sendMsg(e.getMessage());
//            //添加队列重试
//            // channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
//        } finally {
//            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//        }

    }

}
