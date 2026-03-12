package com.weilin.datasource.provider;

import com.alibaba.fastjson.JSONObject;
import com.weilin.annotation.MiddlewareType;
import com.weilin.datasource.DataSourceProvider;
import com.weilin.enums.ConfigKeyEnum;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Objects;

@MiddlewareType("redis")
@Component
public class RedisProvider implements DataSourceProvider {

    @Override
    public Object create(JSONObject config) {
        JSONObject params = config.getJSONObject(ConfigKeyEnum.PARAMS.getKey());
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(params.getInteger(ConfigKeyEnum.MAX_TOTAL.getKey()));
        poolConfig.setMaxIdle(params.getInteger(ConfigKeyEnum.MAX_IDLE.getKey()));
        poolConfig.setMinIdle(params.getInteger(ConfigKeyEnum.MIN_IDLE.getKey()));
        // 如果有密码，在创建JedisPool时传入
        if(Objects.equals(config.getString(ConfigKeyEnum.PASSWORD.getKey()), "")) {
            return new JedisPool(poolConfig,
                    config.getString(ConfigKeyEnum.HOST.getKey()),
                    config.getInteger(ConfigKeyEnum.PORT.getKey()),
                    params.getInteger(ConfigKeyEnum.TIMEOUT.getKey())
            );
        }else {
            return new JedisPool(poolConfig,
                    config.getString(ConfigKeyEnum.HOST.getKey()),
                    config.getInteger(ConfigKeyEnum.PORT.getKey()),
                    params.getInteger(ConfigKeyEnum.TIMEOUT.getKey()),
                    config.getString(ConfigKeyEnum.PASSWORD.getKey())
            );
        }   }
}
