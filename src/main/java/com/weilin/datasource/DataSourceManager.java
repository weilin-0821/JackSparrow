package com.weilin.datasource;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.weilin.enums.ConfigKeyEnum;
import com.weilin.common.utils.JSONUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DataSourceManager {

    @Autowired
    private ProviderRegistry registry;

    private final Map<String,Object> dataSourceMap=new ConcurrentHashMap<>();

    @PostConstruct
    public void init(){
        JSONArray configs= JSONUtils.getJSONArrayFromFile("dataSource.json");
        for(Object object : configs){
            JSONObject config=(JSONObject)object;
            String type = config.getString(ConfigKeyEnum.SOURCE_TYPE.getKey());
            String name = config.getString(ConfigKeyEnum.SOURCE_NAME.getKey());
            DataSourceProvider provider = registry.getProvider(type);
            Object client = provider.create(config);
            dataSourceMap.put(name,client);
        }
    }

    public <T> T get(String name){
        return (T)dataSourceMap.get(name);
    }
}
