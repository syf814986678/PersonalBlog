package com.shiyifan;

import com.rabbitmq.client.Channel;
import com.shiyifan.pojo.Blog;
import org.springframework.amqp.core.Message;

import java.util.HashMap;

/**
 * @author ZouCha
 * @name RabbitmqService
 * @date 2023-02-16 14:29
 **/
public interface RabbitmqService {

    boolean insertBlogProvider(Blog blog, int userId) throws Exception;

    void insertBlogConsumer(HashMap<String, Object> map, Message message, Channel channel) throws Exception;

}
