package com.dataan.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhan bing liang
 * @date 2024/6/4 14:18
 */
@Configuration
public class SpringDocConfig {
    /**
     * spring doc配置
     */
    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
            // 设置标题、版本等信息
            .info(new Info()
                .title("数据管理系统")
                .version("0.0.1-SNAPSHOT")
                .description("数据管理系统")
                );
    }
}
