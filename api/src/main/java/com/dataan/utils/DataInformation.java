package com.dataan.utils;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author zhan bing liang
 * @date 2024/6/4 17:16
 */
@Component
public class DataInformation {
    @Value("${jwt.key}")
    private String jwtKey;
    public static  String JWT_KEY;


    @PostConstruct
    public void init() {
        JWT_KEY = jwtKey;
    }


}
