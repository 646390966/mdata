package com.dataan.config;

import com.dataan.interceptor.MybatisInterceptor;
import javax.sql.DataSource;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.session.AutoMappingBehavior;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * @author zhan bing liang
 * @date 2024/6/5 9:15
 */
@Configuration
public class MybatisConfig {

    @Bean
    @Primary
    public DataSourceTransactionManager metaTransactionManager(@Qualifier("metaDataSource")DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager(@Qualifier("dataSource")DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
    @Value("${spring.datasource.meta.datasource-id}")
    private String metaDatasourceId;

    @Bean(name = "metaDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.meta")
    public javax.sql.DataSource getMetaDateSource() {

        return DataSourceBuilder.create().build();
    }

    @Bean(name = "metaDataSourceFactory")
    public SqlSessionFactory metaDataSourceFactory(@Qualifier("metaDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setAutoMappingBehavior(AutoMappingBehavior.FULL);
        configuration.setCacheEnabled(true);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setDatabaseId(metaDatasourceId);
        configuration.setLogImpl(StdOutImpl.class);
        bean.setPlugins(new MybatisInterceptor());
        bean.setConfiguration(configuration);

        return bean.getObject();
    }

    @Bean(name = "metaDataSourceTemplate")
    public SqlSessionTemplate metaDataSourceTemplate(@Qualifier("metaDataSourceFactory") SqlSessionFactory sqlSessionFactory)  {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Value("${spring.datasource.data.datasource-id}")
    private String datasourceId;

    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource.data")
    public javax.sql.DataSource getDateSource() {

        return DataSourceBuilder.create().build();
    }

    @Bean(name = "dataSourceFactory")
    public SqlSessionFactory dataSourceFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setAutoMappingBehavior(AutoMappingBehavior.FULL);
        configuration.setCacheEnabled(true);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setDatabaseId(datasourceId);
        configuration.setLogImpl(StdOutImpl.class);
        bean.setConfiguration(configuration);
        bean.setPlugins(new MybatisInterceptor());
        return bean.getObject();
    }

    @Bean(name = "dataSourceTemplate")
    public SqlSessionTemplate dataSourceTemplate(@Qualifier("dataSourceFactory") SqlSessionFactory sqlSessionFactory)  {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
