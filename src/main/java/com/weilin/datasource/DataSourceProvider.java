package com.weilin.datasource;

import com.alibaba.fastjson.JSONObject;

public interface DataSourceProvider {
    Object create (JSONObject config);
}
