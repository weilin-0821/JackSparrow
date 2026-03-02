package com.weilin.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.weilin.pojo.Result;
import com.weilin.service.ElasticSearchService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequestMapping("/es")
@RestController
public class ElasticSearchController {
    @Autowired
    private ElasticSearchService elasticSearchService;

    /**
     * 查询索引映射
     * @param indexName 映射名称
     * @return
     */
    //等价于@GetMapping
    @RequestMapping(value = "/getMapping",method = RequestMethod.GET)
    public Result getMapping(String indexName){
            Result result = new Result();
            try {
                result.setData(elasticSearchService.getMapping(indexName));
            }catch (Exception e){
                result.setCode(500);
                result.setMessage(e.getMessage());
            }
        return result;
    }

    /**
     * 插入接口
     * @param param：{indexName："字段名"，JSONArray:[数据组]}
     * @return
     */
    @RequestMapping(value = "/addDoc",method =  RequestMethod.POST)
    public Result addDoc(@RequestBody JSONObject param) {
        Result result = new Result();
        JSONArray jsonArray = param.getJSONArray("JSONArray");
        String indexName = param.getString("indexName");
        if(jsonArray == null || jsonArray.size()<=0){
            result.setCode(500);
            result.setMessage("数据为空！");
        }else if(indexName == null || StringUtils.isEmpty(indexName)){
            result.setCode(500);
            result.setMessage("索引名不能为空！");
        }
        try {
            result.setMessage("插入"+elasticSearchService.addDoc(indexName,jsonArray)+"条");
        }catch (Exception e){
            result.setCode(500);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    /**
     * 多条件查询
     * @param {}
     * @return JSONArray{indexName:"indexName",params:{}}
     * @throws IOException
     */
    @RequestMapping(value = "/select",method =  RequestMethod.POST)
    public Result select(@RequestBody JSONObject param) {
        Result result = new Result();
        String indexName = param.getString("indexName");
        JSONObject params = param.getJSONObject("params");
        try {
            result.setData(elasticSearchService.select(indexName,params));
        }catch (IOException e){
            result.setCode(500);
            result.setMessage(e.getMessage());
        }
        return result;
    }
}
