package com.dataan.db.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zhan bing liang
 * @date 2024/6/27 15:31
 */
@Mapper
public interface BaseMapper {

    /**
     * 创建表
     * @param tableName
     * @param values
     * @param end
     */
    void createTable(String tableName, String values, String end);

    /**
     * 删除表
     * @param tableName
     */
    void deleteTable(String tableName);

    /**
     * 开启事务
     */
    void startTransaction();

    /**
     * 提交事务
     */
    void commit();

    /**
     * 回滚
     */
    void rollback();

    /**
     * 更新表名
     * @param oldName
     * @param newName
     */
    void updateTableName(String oldName, String newName);

    /**
     * 添加表字段
     * @param tableName
     * @param sqls
     */
    void addTableColumn(String tableName, List<String> sqls);

    /**
     * 删除表字段
     * @param tableName
     * @param fields
     */
    void deleteTableColumn(String tableName, List<String> fields);
}
