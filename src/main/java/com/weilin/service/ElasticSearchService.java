package com.weilin.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

public interface ElasticSearchService {
    /**
     * 查询索引映射
     * @param indexName 映射名称
     */
    JSONObject getMapping(String indexName) throws IOException;

    /**
     * 批量插入
     * @param jsonArray 数据
     * @param indexName 索引名
     */
    int addDoc(String indexName,JSONArray jsonArray) throws IOException;

    /**
     * 多条件查询
     * @param indexName 索引名称
     * @param params 限制条件
     * @return JSONArray
     */
    JSONArray select(String indexName,JSONObject params)throws IOException;

}
