package com.dataan.db.impl;


import com.dataan.db.AbstractValue;
import com.dataan.db.Field;
import com.dataan.db.condition.Expression;
import com.dataan.db.mapper.DataMapper;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * @author zhan bing liang
 * @date 2024/5/15 15:53
 */
@Component
public class MysqlDataOperation {
    @Autowired
    private DataMapper dataMapper;

    public void createData(String tableName, List<Field> fields, List<List<AbstractValue<?>>> values) {
        if (CollectionUtils.isEmpty(fields)) {
            throw new IllegalArgumentException("MysqlDataOperation fields长度不能为0");
        }
        if (CollectionUtils.isEmpty(values)) {
            throw new IllegalArgumentException("MysqlDataOperation values长度不能为0");
        }
        String fieldSql = fields.stream().map(Field::getName).collect(Collectors.joining(","));
        StringBuilder valueSql = new StringBuilder();

        for (List<AbstractValue<?>> v : values) {
            valueSql.append("(");

            if (fields.size() != v.size()) {
                throw new IllegalArgumentException(MessageFormat.format("MysqlDataOperation fields长度为{0} Value长度为{1}",fields.size(),v.size()));
            }
            for (AbstractValue<?> vv:v) {
                valueSql.append(vv.getMysqlValue()).append(",");
            }
            valueSql.deleteCharAt(valueSql.lastIndexOf(","));
            valueSql.append(")");
            valueSql.append(",");
        }
        valueSql.deleteCharAt(valueSql.lastIndexOf(","));
        dataMapper.insertData(tableName,fieldSql,valueSql.toString());
    }

    public void deleteData(String tableName, Expression condition){
        dataMapper.deleteData(tableName,condition.interpret());
    }

    public void updateData(String tableName, Expression change, Expression condition) {
        dataMapper.updateData(tableName,change.interpret(),condition.interpret());
    }

    public List<Map<String,Object>> getData(String tableName, Expression condition, String orderColumn, Boolean desc, RowBounds rowBounds){
        return dataMapper.getData(tableName,condition.interpret(),orderColumn,desc,rowBounds);
    }

    public Integer getTotalCount(String tableName) {
        return dataMapper.getTotalCount(tableName);
    }
}
