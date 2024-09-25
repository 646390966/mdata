package com.dataan.handler;

import com.dataan.utils.JsonUtil;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.apache.ibatis.type.JdbcType;

/**
 * @author zhan bing liang
 * @date 2024/6/13 11:26
 */
public class ListTypeHandler extends BaseTypeHandler<List<String>> {
    @Override
    public void setParameter(PreparedStatement preparedStatement, int position, List<String> list, JdbcType jdbcType) throws SQLException {
        preparedStatement.setObject(position, JsonUtil.writeValueAsString(list));
    }
}
