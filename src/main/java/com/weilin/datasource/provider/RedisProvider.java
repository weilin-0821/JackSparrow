package com.weilin.datasource.provider;

import com.alibaba.fastjson.JSONObject;
import com.weilin.annotation.MiddlewareType;
import com.weilin.datasource.DataSourceProvider;
import com.weilin.enums.ConfigKeyEnum;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@MiddlewareType("redis")
@Component
public class RedisProvider implements DataSourceProvider {

    @Override
    public Object create(JSONObject config) {
        JSONObject params = config.getJSONObject("params");
        JedisPoolConfig poolConfig = new JedisPoolConfig();

        poolConfig.setMaxTotal(
                params.getInteger(ConfigKeyEnum.MAX_TOTAL.getKey()));

        poolConfig.setMaxIdle(
                params.getInteger(ConfigKeyEnum.MAX_IDLE.getKey()));

        poolConfig.setMinIdle(
                params.getInteger(ConfigKeyEnum.MIN_IDLE.getKey()));

        return new JedisPool(
                poolConfig,
                config.getString(ConfigKeyEnum.HOST.getKey()),
                config.getInteger(ConfigKeyEnum.PORT.getKey()),
                params.getInteger(ConfigKeyEnum.TIMEOUT.getKey()),
                config.getString(ConfigKeyEnum.PASSWORD.getKey())
        );    }
}
