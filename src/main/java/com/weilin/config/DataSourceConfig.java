package com.weilin.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.weilin.utils.JSONUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 读取所有数据库配置
 */
@Configuration
public class DataSourceConfig implements InitializingBean {

    private ConcurrentHashMap<String, RestHighLevelClient> esClientMap=new ConcurrentHashMap<>();

    /**
     * 读取es的配置信息存放到esClientMap
     */
    @Override
    public void afterPropertiesSet(){
        JSONArray jsonArray = JSONUtils.getJSONArrayFromFile("dataSource.json");
        for (int index = 0; index < jsonArray.size(); index++) {
            JSONObject jsonObject = jsonArray.getJSONObject(index);
            switch (jsonObject.getString("sourceType")) {
                case "ElasticSearch":
                    esClientMap.put(jsonObject.getString("sourceName"),
                            newESClient(jsonObject.getString("hostname"),
                                    jsonObject.getInteger("port"),
                                    jsonObject.getString("scheme")
                            )
                    );break;
                default:
                    System.out.println("dataSource.json 存在问题请检查");
            }
        }
    }

    /**
     * 新建ElasticSearchClient
     * @param hostname ip地址
     * @param port 端口号
     * @param scheme 前缀http
     * @return RestHighLevelClient
     */
    private RestHighLevelClient newESClient(String hostname,Integer port,String scheme){
        RestClientBuilder builder = RestClient.builder(
                new HttpHost(hostname, port, scheme));

        RestHighLevelClient client = new RestHighLevelClient(builder);
        return client;
    }

    /**
     * 获取ElasticSearchClient
     * @param sourceName 名称
     * @return RestHighLevelClient
     */
    public RestHighLevelClient getESClient(String sourceName){
        return esClientMap.get(sourceName);
    }

}
