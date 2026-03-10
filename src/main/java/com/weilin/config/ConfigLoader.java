package com.weilin.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.weilin.utils.JSONUtils;

import java.util.List;
import java.util.stream.Collectors;

public class ConfigLoader {
    public static List<JSONObject> load(String fileName) {
        JSONArray jsonArray = JSONUtils.getJSONArrayFromFile(fileName);
        return jsonArray.stream()
                .map(obj->(JSONObject)obj)
                .collect(Collectors.toList());
    }
}
