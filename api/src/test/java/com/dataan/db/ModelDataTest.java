package com.dataan.db;

import com.dataan.service.ModelDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhan bing liang
 * @date 2024/7/9 15:38
 */
@SpringBootTest
@Transactional
public class ModelDataTest {
    @Autowired
    ModelDataService modelDataService;


}
