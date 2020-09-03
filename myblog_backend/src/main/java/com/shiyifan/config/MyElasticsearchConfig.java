package com.shiyifan.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 81498
 */
@Configuration
public class MyElasticsearchConfig {
    @Value("${elasticsearch.host}")
    private String host;

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        // 构建客户端对象
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(host, 9200, "http")));
        return client;
    }

}
