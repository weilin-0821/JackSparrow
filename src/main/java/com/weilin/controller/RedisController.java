package com.weilin.controller;

import com.alibaba.fastjson.JSONObject;
import com.weilin.config.DataSourceConfig;
import com.weilin.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

@RequestMapping("/redis")
@RestController
public class RedisController {
    @Autowired
    private DataSourceConfig dataSourceConfig;

    @RequestMapping(value = "/set",method = RequestMethod.PUT)
    public Result set(@RequestBody JSONObject param){
        Result result = new Result();
        String sourceName = param.getString("sourceName");
        Jedis jedis=dataSourceConfig.getJedis(sourceName);
        jedis.select(1);
        result.setMessage(jedis.set(param.getString("key"),param.getString("value")));
        return result;
    }

    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public Result get(@RequestBody JSONObject param){
        Result result = new Result();
        String sourceName = param.getString("sourceName");
        Jedis jedis=dataSourceConfig.getJedis(sourceName);
        jedis.select(1);
        result.setMessage( jedis.get(param.getString("key")));
        return result;
    }

}
