package com.dataan.vo;

import com.dataan.db.Field;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author zhan bing liang
 * @date 2024/6/28 15:01
 */
public class ModelRequestVo {
    @NotEmpty(message = "名称不能为空")
    @Size(max=10,message = "名称长度不应超过10")
    private String name;
    @NotNull(message = "字段不能为空")
    private List<Field> fields;
    private String primaryKey;
    private String comment;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
