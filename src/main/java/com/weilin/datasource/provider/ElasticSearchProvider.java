package com.weilin.datasource.provider;

import com.alibaba.fastjson.JSONObject;
import com.weilin.common.annotation.MiddlewareType;
import com.weilin.datasource.DataSourceProvider;
import com.weilin.enums.ConfigKeyEnum;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Component;

@MiddlewareType("elasticSearch")
@Component
public class ElasticSearchProvider implements DataSourceProvider {
    @Override
    public Object create(JSONObject config) {
        RestClientBuilder builder = RestClient.builder(
                new HttpHost(config.getString(ConfigKeyEnum.HOST.getKey()),
                        config.getInteger(ConfigKeyEnum.PORT.getKey()),
                        config.getString(ConfigKeyEnum.SCHEME.getKey())
                )
        );
        RestHighLevelClient client = new RestHighLevelClient(builder);
        return client;
    }
}
