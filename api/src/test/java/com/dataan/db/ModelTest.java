package com.dataan.db;


import com.dataan.db.impl.MysqlDataOperation;
import com.dataan.entity.Model;
import com.dataan.entity.Model.SourceEnum;
import com.dataan.service.ModelService;
import com.dataan.utils.JsonUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhan bing liang
 * @date 2024/4/8 16:50
 */
@SpringBootTest
@Transactional
public class ModelTest {
    @Autowired
    DbTransaction dbTransaction;
    @Autowired
    ModelService modelService;
    @Autowired
    MysqlDataOperation mysqlDataOperation;
    private String tableName="cs";
    @Test
    void createAndDeleteModelTest() {
        Model model = getModel();

        Model model1 = modelService.createModel(model);

        List<Model> res = modelService.getModelList("",1,100,null,null,null,null);

        Assertions.assertThat(res).matches(r->r.stream().map(Model::getName).collect(Collectors.toList()).contains(tableName));


//        modelService.deleteModel(model1.getId());
//
//
//        Assertions.assertThat(modelService.getModelList("mdata",1,100,null,null,null,null)).matches(r->!
//            r.stream().map(Model::getName).collect(Collectors.toList()).contains(tableName));
    }
    @Test
    public void updateModelTest() {
        Model model = getModel();

        Model model1 = modelService.createModel(model);

        Model res = modelService.getModelById(model1.getId());

        Assertions.assertThat(res).isNotNull();
        res= JsonUtil.convertValue(res,Model.class);
        String fieldName = "name";
        Field field = new Field();
        field.setName(fieldName);
        field.setLength(255);
        field.setType(FieldTypeEnum.STRING);
        Field field2 = new Field();
        field2.setName("name2");
        field2.setLength(255);
        field2.setType(FieldTypeEnum.STRING);
        res.getFields().add(field);
        res.getFields().add(field2);
        modelService.updateModel(res);
        res = modelService.getModelById(model1.getId());
        Assertions.assertThat(res.getFields().stream().map(Field::getName).collect(Collectors.toList())).contains(fieldName);
        res=JsonUtil.convertValue(res,Model.class);
        res.getFields().remove(field);

        modelService.updateModel(res);
        res = modelService.getModelById(model1.getId());
        Assertions.assertThat(res.getFields().stream().map(Field::getName).collect(Collectors.toList())).doesNotContain(fieldName);

    }
    @Test
    public void getModelById() {
        String id = "188dd5c166714901b06d994fd591d2b8";
        Model res = modelService.getModelById(id);
        List<Field> fields = res.getFields();
        System.out.println(fields.get(0));
        Assertions.assertThat(res).isNotNull();
    }

    private Model getModel() {
        List<Field> fields = new ArrayList<>(16);
        Field field = new Field();
        field.setName("id");
        field.setType(FieldTypeEnum.STRING);
        field.setLength(255);
        field.setComment("id");
        Field field2 = new Field();
        field2.setName("createTime");
        field2.setType(FieldTypeEnum.DATE);
        field2.setComment("时间");
        fields.add(field);
        fields.add(field2);
        Model model = new Model();

        model.setName(tableName);
        model.setPrimaryKey("id");
        model.setComment("测试表");
        model.setSource(SourceEnum.MYSQL);
        model.setDb("111");
        model.setFields(fields);
        return model;
    }



}
