package com.dataan.db.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

/**
 * @author zhan bing liang
 * @date 2024/5/15 15:34
 */
@Mapper
public interface DataMapper {
    /**
     * 插入数据
     * @param tableName
     * @param fields
     * @param value
     */
    public void insertData(String tableName, String fields, String value);

    /**
     * 修改数据
     * @param tableName
     * @param change
     * @param condition
     */
    public void updateData(String tableName, String change, String condition);

    /**
     * 删除数据
     * @param tableName
     * @param condition
     */
    public void deleteData(String tableName, String condition);

    /**
     * 获取数据
     *
     * @param name
     * @param interpret
     * @param tableName
     * @param condition
     * @param rowBounds
     * @return
     */
    List<Map<String, Object>> getData(String tableName, String condition,String orderColumn,Boolean desc,  RowBounds rowBounds);

    /**
     * 获取总数据量
     * @param tableName
     * @return
     */
    Integer getTotalCount(String tableName);
}
