package com.dataan.handler;

import com.dataan.db.Field;
import com.dataan.utils.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.ibatis.type.JdbcType;

/**
 * @author zhan bing liang
 * @date 2024/7/1 17:15
 */
public class ListFieldTypeHandler  extends BaseTypeHandler<List<Field>>{
    @Override
    public void setParameter(PreparedStatement preparedStatement, int position, List<Field> list, JdbcType jdbcType) throws SQLException {
        preparedStatement.setObject(position, JsonUtil.writeValueAsString(list));
    }
    @Override
    public List<Field> getResult(ResultSet resultSet, String column) throws SQLException {
        Object res = resultSet.getObject(column);
        return JsonUtil.convertValue(res, new TypeReference<List<Field>>() {
        });

    }
}
