package com.weilin.datasource;

import com.weilin.annotation.MiddlewareType;
import jakarta.annotation.PostConstruct;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ProviderRegistry {

    private final Map<String, DataSourceProvider> providerMap = new ConcurrentHashMap<>();

    private final ConfigurableApplicationContext context;

    public ProviderRegistry(ConfigurableApplicationContext context) {
        this.context = context;
    }

    @PostConstruct
    public void init() {

        Map<String,Object> beans =
                context.getBeansWithAnnotation(MiddlewareType.class);
        for(Object bean : beans.values()){
            MiddlewareType annotation =
                    bean.getClass().getAnnotation(MiddlewareType.class);
            providerMap.put(annotation.value(),(DataSourceProvider) bean);
        }
    }

    public DataSourceProvider getProvider(String type){
        return providerMap.get(type);
    }
}
