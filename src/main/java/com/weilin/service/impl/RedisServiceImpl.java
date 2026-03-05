package com.weilin.service.impl;

import com.weilin.config.DataSourceConfig;
import com.weilin.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private DataSourceConfig dataSourceConfig;


}
