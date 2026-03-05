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
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 读取所有数据库配置
 */
@Configuration
public class DataSourceConfig implements InitializingBean {

    private ConcurrentHashMap<String, RestHighLevelClient> esClientMap;

    private ConcurrentHashMap<String, JedisPool> jedisMap;

    /**
     * 读取es的配置信息存放到esClientMap
     */
    @Override
    public void afterPropertiesSet(){
        esClientMap=new ConcurrentHashMap<>();//初始化es
        jedisMap=new ConcurrentHashMap<>();//初始化redis
        //取配置文件中信息
        JSONArray jsonArray = JSONUtils.getJSONArrayFromFile("dataSource.json");
        for (int index = 0; index < jsonArray.size(); index++) {
            JSONObject jsonObject = jsonArray.getJSONObject(index);
            switch (jsonObject.getString("sourceType")) {
                case "elasticSearch":
                    esClientMap.put(jsonObject.getString("sourceName"), newESClient(jsonObject));
                    break;
                case "redis":
                    jedisMap.put(jsonObject.getString("sourceName"),newJedisPool(jsonObject));
                    break;
                default:
                    System.out.println("dataSource.json 存在问题请检查");
                    break;
            }
        }
    }

    /**
     * 新建ElasticSearchClient
     * @param param { hostname:ip地址 ,port:端口号 ,scheme:前缀http}
     * @return RestHighLevelClient
     */
    private RestHighLevelClient newESClient(JSONObject param){
        RestClientBuilder builder = RestClient.builder(
                new HttpHost(param.getString("host"),
                        param.getInteger("port"),
                        param.getString("scheme")
                )
        );
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

    /**
     * 返回 JedisPool
     * @param param {
     *     sourceType:数据源类型,
     *     "sourceName":数据源名称,
     *     "host": 数据源,
     *     "port": 端口号,
     *     "password":密码,
     *     "params": {
     *       "timeOut": 连接超时时间,
     *       "maxTotal": 最大连接数,
     *       "maxIdle": 最大空闲连接,
     *       "minIdle": 最小空闲连接
     *     }
     * @return JedisPool
     */
    private JedisPool newJedisPool(JSONObject param){
        JSONObject otherParams = param.getJSONObject("params");
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(otherParams.getInteger("maxTotal")); // 最大连接数
        jedisPoolConfig.setMaxIdle(otherParams.getInteger("maxIdle"));   // 最大空闲连接
        jedisPoolConfig.setMinIdle(otherParams.getInteger("minIdle"));    // 最小空闲连接
        // 如果有密码，在创建JedisPool时传入
        if(Objects.equals(param.getString("password"), "")) {
            return new JedisPool(jedisPoolConfig,
                    param.getString("host"),
                    param.getInteger("port"),
                    otherParams.getInteger("timeOut")
            );
        }else {
            return new JedisPool(jedisPoolConfig,
                    param.getString("host"),
                    param.getInteger("port"),
                    otherParams.getInteger("timeOut"),
                    param.getString("password")
            );
        }
    }

    /**
     * 获取Jedis
     * @param sourceName 名称
     * @return Jedis
     */
    public  Jedis getJedis(String sourceName){
        return jedisMap.get(sourceName).getResource();
    }
}
