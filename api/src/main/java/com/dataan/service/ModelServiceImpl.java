package com.dataan.service;

import com.dataan.db.Field;
import com.dataan.db.FieldTypeEnum;
import com.dataan.db.impl.MysqlTableOperation;
import com.dataan.entity.Model;
import com.dataan.entity.Model.SourceEnum;
import com.dataan.mapper.ModelMapper;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhan bing liang
 * @date 2024/6/27 15:16
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ModelServiceImpl implements ModelService {

    @Autowired
    private MysqlTableOperation mysqlTableOperation;
    @Autowired
    private ModelMapper modelMapper;
    @Value("${DB_DATABASE}")
    private String db;

    @Override
    public Model createModel(Model model) {
        if (getModelByName(model.getName()) != null) {
            throw new IllegalArgumentException(MessageFormat.format("name={0}已存在", model.getName()));
        }
        List<Field> fields = model.getFields();
        fields.stream().map(Field::getName).filter(s->"id".equals(s)).findFirst().ifPresent(s-> new IllegalArgumentException("不可以创建id字段"));
        model.setId(UUID.randomUUID().toString().replace("-", ""));
        model.setCreateTime(LocalDateTime.now());
        model.setUpdateTime(LocalDateTime.now());
        model.setSource(SourceEnum.MYSQL);
        model.setDb(db);


        Field field = new Field();
        field.setName("id");
        field.setComment("id");
        field.setLength(255);
        field.setType(FieldTypeEnum.STRING);
        fields.add(0,field);
        model.setPrimaryKey("id");
        modelMapper.createModel(model);
        mysqlTableOperation.createTable(model);
        return model;
    }

    @Override
    public List<Model> getModelList(String modelName, Integer page, Integer size, Date createTimeStart, Date createTimeEnd, String orderColumn, Boolean desc) {
        return modelMapper.getModelList(modelName, createTimeStart, createTimeEnd, orderColumn, desc,size==-1?new RowBounds(0,Integer.MAX_VALUE):new RowBounds((page - 1) * size, size));
    }

    @Override
    public Integer getTotalCount() {
        return modelMapper.getTotalCount();
    }

    @Override
    public Model getModelById(String id) {
        return modelMapper.getModelById(id);
    }

    @Override
    public Model getModelByName(String name) {
        return modelMapper.getModelByName(name);
    }

    @Override
    public void deleteModel(String id) {
        Model model = getModelById(id);

        modelMapper.deleteModel(id);
        mysqlTableOperation.deleteTable(model.getName());
    }

    @Override
    public void updateModel(Model model) {
        if (getModelByName(model.getName()) == null) {
            throw new IllegalArgumentException(MessageFormat.format("name={0}不存在", model.getName()));
        }
        Model oldModel = getModelById(model.getId());

        List<Field> oldFields = oldModel.getFields();
        List<Field> fields = model.getFields();

        List<Field> needAdd = new ArrayList<>(fields);
        needAdd.removeAll(oldFields);
        List<Field> needDelete = new ArrayList<>(oldFields);
        needDelete.removeAll(fields);
        needDelete = needDelete.stream().filter(s -> !"id".equals(s.getName())).collect(Collectors.toList());
        model.setUpdateTime(LocalDateTime.now());
        model.getFields().add(oldFields.stream().filter(s -> "id".equals(s.getName())).findFirst().orElseThrow(() -> new IllegalArgumentException("无法找到id字段")));
        modelMapper.updateModel(model);
        mysqlTableOperation.addTableColumn(model.getName(), needAdd);
        mysqlTableOperation.deleteTableColumn(model.getName(), needDelete.stream().map(Field::getName).collect(Collectors.toList()));
        mysqlTableOperation.updateTableName(oldModel.getName(), model.getName());
    }
}
