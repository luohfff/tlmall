package com.tuling.tulingmall.search.config;

/*
@author 图灵学院-白起老师
*/

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig {

    //http://45.125.57.198/
    @Bean
    public RestHighLevelClient restHighLevelClient(){
        RestHighLevelClient client = new RestHighLevelClient(
                /*RestClient.builder(
                       new HttpHost("192.168.21.130", 9200, "http"),
                        new HttpHost("192.168.21.131", 9200, "http"),
                        new HttpHost("192.168.21.132", 9200, "http")));*/
               RestClient.builder(
                        new HttpHost("192.168.65.219", 9200, "http")));
        return client;
    }
}
