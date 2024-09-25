package com.dataan.entity;

import com.dataan.db.Field;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhan bing liang
 * @date 2024/6/27 15:20
 */
public class Model {
    private String id;
    private List<Field> fields;
    private String primaryKey;
    private String name;
    private String comment;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String db;
    private SourceEnum source;

    public   enum SourceEnum{
        /**
         * MYSQL数据库
         */
        MYSQL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public SourceEnum getSource() {
        return source;
    }

    public void setSource(SourceEnum source) {
        this.source = source;
    }
}
