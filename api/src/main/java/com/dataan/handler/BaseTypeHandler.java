package com.dataan.handler;

import com.dataan.utils.JsonUtil;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

/**
 * @author zhan bing liang
 * @date 2024/6/13 11:01
 */
public class BaseTypeHandler<T> implements TypeHandler<T> {
    @Override
    public void setParameter(PreparedStatement preparedStatement, int position, T t, JdbcType jdbcType) throws SQLException {
        preparedStatement.setObject(position,t);
    }

    @Override
    public T getResult(ResultSet resultSet, String column) throws SQLException {
        Object res = resultSet.getObject(column);
        if (res == null) {
            return null;
        }
        ParameterizedType genericSuperclass = (ParameterizedType)this.getClass().getGenericSuperclass();
        Type actualTypeArgument = genericSuperclass.getActualTypeArguments()[0];
        Class<T> clazz = null;
        if (actualTypeArgument instanceof Class) {
            clazz = (Class)actualTypeArgument;
        } else if (actualTypeArgument instanceof ParameterizedType) {
            clazz = (Class)((ParameterizedType) actualTypeArgument).getRawType();
            if (clazz == List.class) {

                List<Object> list = JsonUtil.convertValue(res, List.class);
                List<Object> r = new ArrayList<>(list.size());
                Type actualTypeArguments = ((ParameterizedType) actualTypeArgument).getActualTypeArguments()[0];
                for (Object t : list) {
                    r.add(JsonUtil.convertValue(t,(Class)actualTypeArguments));
                }
                return (T)r;
            }
        } else {
            throw new UnsupportedOperationException(String.format("actualTypeArgument=%s in ObjTypeHandler not support", actualTypeArgument.getTypeName()));
        }

        if(clazz.isInstance(res)) {
            return clazz.cast(res);
        } else {
            return JsonUtil.convertValue(res,clazz);
        }
    }

    @Override
    public T getResult(ResultSet resultSet, int i) throws SQLException {
        return null;
    }

    @Override
    public T getResult(CallableStatement callableStatement, int i) throws SQLException {
        return null;
    }

}
