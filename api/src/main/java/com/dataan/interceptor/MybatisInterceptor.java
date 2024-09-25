package com.dataan.interceptor;

import com.dataan.utils.JsonUtil;
import java.sql.Connection;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Matcher;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

/**
 * @author zhan bing liang
 * @date 2024/6/5 9:15
 */
@Intercepts({
    @Signature(type = StatementHandler.class,
        method = "prepare",
        args = {Connection.class, Integer.class})})
public class MybatisInterceptor implements Interceptor {

    private static final  Logger LOGGER = LoggerFactory.getLogger(MybatisInterceptor.class);


    /**
     * 拦截方法
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation ) throws Throwable{

        // 耗时开始时间
        long startTime = System.currentTimeMillis();
        // 获取 StatementHandler ，默认是 RoutingStatementHandler
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        // 获取 StatementHandler 包装类
        MetaObject metaObjectHandler = SystemMetaObject.forObject(statementHandler);
        // 获取查询接口映射的相关信息
        MappedStatement mappedStatement = (MappedStatement) metaObjectHandler.getValue("delegate.mappedStatement");
        // 获取请求时的参数
        Object parameterObject = statementHandler.getParameterHandler().getParameterObject();
        // 获取sql
        String sql = showSql(mappedStatement.getConfiguration(),  mappedStatement.getBoundSql(parameterObject));
        // 获取执行sql方法
        String sqlId = mappedStatement.getId();
        // 执行sql
        Object result = invocation.proceed();
        // 计算总耗时
        long cost = System.currentTimeMillis() - startTime;
        LOGGER.info(" ======> SQL方法 : {} , 总耗时 : {}毫秒,  SQL语句 : {} ", sqlId, cost, sql);
        return result;
    }

    /**
     * 实现插件包装
     * @param target
     * @return
     */
    @Override
    public Object plugin(Object target){


        return (target instanceof StatementHandler) ? Plugin.wrap(target, this) : target;
    }

    /**
     * 实现插件参数传递
     * @param properties
     */
    @Override
    public void setProperties(Properties properties){

    }
    private static String showSql(Configuration configuration, BoundSql boundSql) {
        // 获取参数
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        // sql语句中多个空格都用一个空格代替
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (!CollectionUtils.isEmpty(parameterMappings) && parameterObject != null) {
            // 获取类型处理器注册器，类型处理器的功能是进行java类型和数据库类型的转换
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            // 如果根据parameterObject.getClass(）可以找到对应的类型，则替换
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(parameterObject)));
            } else {
                // MetaObject主要是封装了originalObject对象，提供了get和set的方法用于获取和设置originalObject的属性值,主要支持对JavaBean、Collection、Map三种类型对象的操作
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        // 该分支是动态sql
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
                    } else {
                        // 打印出缺失，提醒该参数缺失并防止错位
                        sql = sql.replaceFirst("\\?", "缺失");
                    }
                }
            }
        }

        return sql;
    }


    private static String getParameterValue(Object obj) {
        String value;

        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        }
        else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(new Date()) + "'";
        }
        else {
            if (obj != null) {
                value = JsonUtil.writeValueAsString(obj);
            } else {
                value = "";
            }
        }

        return value;
    }


}

