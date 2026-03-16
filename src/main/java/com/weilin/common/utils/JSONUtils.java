package com.weilin.common.utils;

import com.alibaba.fastjson.JSONArray;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JSONUtils {
    public static JSONArray getJSONArrayFromFile(String fileName) {
        JSONArray jsonArray = new JSONArray();
        try {
            ClassPathResource classPathResource = new ClassPathResource(fileName);
            String configString= IOUtils.toString(classPathResource.getInputStream(), StandardCharsets.UTF_8);
            jsonArray = JSONArray.parseArray(configString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }
}
