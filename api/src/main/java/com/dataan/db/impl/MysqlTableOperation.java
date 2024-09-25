package com.dataan.db.impl;

import com.dataan.db.Field;
import com.dataan.db.FieldUtil;
import com.dataan.db.mapper.BaseMapper;
import com.dataan.entity.Model;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * @author zhan bing liang
 * @date 2024/3/29 11:05
 */
@Component
public class MysqlTableOperation{
    @Autowired
    private BaseMapper baseMapper;

    public Model createTable(Model model) {
        if (CollectionUtils.isEmpty(model.getFields())) {
            throw new IllegalArgumentException("CreateMysqlTableOperation fields length can not be 0");
        }
        if (!StringUtils.hasText(model.getName())) {
            throw new IllegalArgumentException("CreateMysqlTableOperation table name can not be blank");
        }
        baseMapper.createTable(FieldUtil.judgeSqlInjection(model.getName()),buildValues(model),buildEnd(model));
        return model;
    }

    public void deleteTable(String tableName){
        baseMapper.deleteTable(tableName);
    }


    private String buildEnd(Model table) {
        StringBuilder stringBuilder = new StringBuilder();
        if (StringUtils.hasText(table.getComment())) {
            stringBuilder.append(" ").append("comment ").append("'").append(FieldUtil.judgeSqlInjection(table.getComment())).append("'");
        }
        return stringBuilder.toString();
    }

    private String buildValues(Model table) {
        String primaryKey = table.getPrimaryKey();
        List<Field> fields = table.getFields();
        StringBuilder stringBuilder = new StringBuilder();
        for (Field field : fields) {
            stringBuilder.append(FieldUtil.judgeSqlInjection(field.getName()))
                .append(" ")
                .append(FieldUtil.mysqlFieldTransfer(field.getType(),field.getLength()))
                .append(" ")
                .append(StringUtils.hasText(field.getComment())?"comment "+"'"+FieldUtil.judgeSqlInjection(field.getComment())+"'":"")
                .append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        if (StringUtils.hasText(primaryKey)) {
            stringBuilder.append(",").append("primary key").append("(").append(FieldUtil.judgeSqlInjection(primaryKey)).append(")");
        }
        return stringBuilder.toString();
    }


    public void addTableColumn(String name, List<Field> fields) {
        if (CollectionUtils.isEmpty(fields)) {
            return;
        }
        List<String> sqls = fields.stream().map(field -> {
            StringBuilder res = new StringBuilder();
            res.append(FieldUtil.judgeSqlInjection(field.getName())).append(" ").append(FieldUtil.mysqlFieldTransfer(field.getType(), field.getLength())).append(" ");
            if (StringUtils.hasText(field.getComment())) {
                res.append("comment").append(" ").append("'").append(FieldUtil.judgeSqlInjection(field.getComment())).append("'");
            }
            return res.toString();
        }).collect(Collectors.toList());
        baseMapper.addTableColumn( FieldUtil.judgeSqlInjection(name), sqls);
    }

    public void deleteTableColumn(String name, List<String> fields) {
        if (CollectionUtils.isEmpty(fields)) {
            return;
        }
        baseMapper.deleteTableColumn(FieldUtil.judgeSqlInjection(name),fields.stream().map(FieldUtil::judgeSqlInjection).collect(Collectors.toList()));
    }

    public void updateTableName(String oldName, String newName) {
        if (oldName.equals(newName)) {
            return;
        }
        baseMapper.updateTableName(FieldUtil.judgeSqlInjection(oldName),FieldUtil.judgeSqlInjection(newName));
    }
}
