package com;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zhan bing liang
 * @date 2024/3/14 16:42
 */
@SpringBootApplication
@MapperScan(value = {"com.dataan.mapper"}, sqlSessionFactoryRef="metaDataSourceFactory")
@MapperScan(value = {"com.dataan.db.mapper"}, sqlSessionFactoryRef="dataSourceFactory")
public class ApiApplication {
    public static void main(String[] args) {
        ProducerRecord<Object, Object> objectObjectProducerRecord = new ProducerRecord<>("your-topic", "key-" + 1, "value-" + 1);
        SpringApplication.run(ApiApplication.class, args);
    }


}
