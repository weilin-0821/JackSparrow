package com.weilin.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.weilin.service.ElasticSearchService;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetMappingsRequest;
import org.elasticsearch.client.indices.GetMappingsResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ElasticServiceServiceImpl implements ElasticSearchService {

    @Autowired
    private RestHighLevelClient esRestClient;

    /**
     * 查询索引映射
     * @param indexName 映射名称
     * @return
     */
    @Override
    public JSONObject getMapping(String indexName) throws IOException {
        GetMappingsRequest request = new GetMappingsRequest();
        request.indices(indexName);
        GetMappingsResponse response = esRestClient
                .indices()
                .getMapping(request, RequestOptions.DEFAULT);
        JSONObject jsonObject = JSON.parseObject(
            JSON.toJSONString(
                response
                    .mappings()
                    .get(indexName)
                    .getSourceAsMap()
            )
        );
        jsonObject.put("indexName",indexName);
        return jsonObject;
    }

    /**
     * 批量插入
     * @param jsonArray 数据
     * @param indexName 索引名
     * @return
     * @throws IOException
     */
    public int addDoc(String indexName,JSONArray jsonArray ) throws IOException {
        BulkRequest request = new BulkRequest();
        for (int i = 0; i < jsonArray.size(); i++) {
            IndexRequest indexRequest = new IndexRequest().index(indexName);
            indexRequest.source(jsonArray.getJSONObject(i).toString(), XContentType.JSON);
            request.add(indexRequest);
        }
        BulkResponse response=esRestClient.bulk(request, RequestOptions.DEFAULT);
        return response.getItems().length;
    }

    /**
     * 多条件查询
     * @param indexName 索引名称
     * @param params 限制条件
     * @return JSONArray
     * @throws IOException
     */
    @Override
    public JSONArray select(String indexName,JSONObject params) throws IOException {
        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for(String key:params.keySet()){
            boolQueryBuilder.must(QueryBuilders.termQuery(key,params.get(key)));
        }
        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse=esRestClient.search(searchRequest, RequestOptions.DEFAULT);
        JSONArray jsonArray = new JSONArray();
        searchResponse.getHits().forEach(searchHit -> {
            JSONObject jsonObject = JSON.parseObject(searchHit.getSourceAsString());
            jsonObject.put("id",searchHit.getId());
            jsonArray.add(jsonObject);
        });
        return jsonArray;
    }
}