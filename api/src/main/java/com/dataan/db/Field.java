package com.dataan.db;

import com.dataan.utils.JsonUtil;
import java.io.File;
import java.io.IOException;
import java.util.*;
import org.aspectj.util.FileUtil;

/**
 * @author zhan bing liang
 * @date 2024/3/20 13:27
 */
public class Field {

    private String name;
    private FieldTypeEnum type;
    private String comment;
    private Integer length;
    private Boolean searchAble;

    public Boolean getSearchAble() {
        return searchAble;
    }

    public void setSearchAble(Boolean searchAble) {
        this.searchAble = searchAble;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FieldTypeEnum getType() {
        return type;
    }

    public void setType(FieldTypeEnum type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Field field = (Field) o;
        return Objects.equals(name, field.name);
    }


    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
