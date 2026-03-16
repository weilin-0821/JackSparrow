package com.weilin.datasource;

import com.weilin.common.annotation.MiddlewareType;
import jakarta.annotation.PostConstruct;
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
        /**
         * 从容器中读取所有带有@MiddlewareType标签的Bean
         * 有@Component就在容器中了
         */
        Map<String,Object> beans =
                context.getBeansWithAnnotation(MiddlewareType.class);
        for(Object bean : beans.values()){
            /**
             * 获取类上的 MiddlewareType 注解
             */
            MiddlewareType annotation = bean.getClass().getAnnotation(MiddlewareType.class);
            providerMap.put(annotation.value(),(DataSourceProvider) bean);
        }
    }

    public DataSourceProvider getProvider(String type){
        return providerMap.get(type);
    }
}
