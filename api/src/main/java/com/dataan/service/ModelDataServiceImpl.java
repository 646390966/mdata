package com.dataan.service;

import com.alibaba.excel.EasyExcel;
import com.dataan.db.AbstractValue;
import com.dataan.db.AbstractValue.ObjectVallue;
import com.dataan.db.AbstractValue.StringValue;
import com.dataan.db.Field;
import com.dataan.db.condition.CommaCondition;
import com.dataan.db.condition.Condition;
import com.dataan.db.condition.Condition.Operator;
import com.dataan.db.condition.Expression;
import com.dataan.db.condition.NullCondition;
import com.dataan.db.impl.MysqlDataOperation;
import com.dataan.entity.Model;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.RowBounds;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * @author zhan bing liang
 * @date 2024/7/8 8:53
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ModelDataServiceImpl implements ModelDataService {

    @Autowired
    private MysqlDataOperation mysqlDataOperation;
    @Autowired
    private ModelService modelService;

    @Override
    public List<Map<String, Object>> createModelData(String modelName, List<Map<String, Object>> datas) {
        if (CollectionUtils.isEmpty(datas)) {
            return datas;
        }
        Model model = modelService.getModelByName(modelName);
        List<String> fields = model.getFields().stream().map(Field::getName).collect(Collectors.toList());
        List<List<AbstractValue<?>>> values = new ArrayList<>(datas.size());
        for (Map<String, Object> data : datas) {
            data.put("id", UUID.randomUUID().toString().replace("-", ""));
            List<String> keys = new ArrayList<>(data.keySet());
            keys.removeAll(fields);
            if (!CollectionUtils.isEmpty(keys)) {
                throw new IllegalArgumentException(MessageFormat.format("key={0}在modelName={1}中不存在", keys, modelName));
            }
            List<AbstractValue<?>> list = new ArrayList<>(fields.size());
            for (Field field : model.getFields()) {
                list.add(AbstractValue.create(field, data.get(field.getName())));
            }
            values.add(list);
        }
        mysqlDataOperation.createData(modelName, model.getFields(), values);
        return datas;
    }

    @Override
    public void updateModelData(String modelName, String id, Map<String, Object> data) {
        if (CollectionUtils.isEmpty(data)) {
            return;
        }
        Model model = modelService.getModelByName(modelName);
        List<Field> fields = model.getFields();
        Map<String, Field> fieldNameMap = fields.stream().collect(Collectors.toMap(Field::getName, Function.identity()));
        Stream<Expression> conditionStream = data.entrySet().stream().filter(s->!"id".equals(s.getKey())).map(e ->
        {
            Field key = Optional.ofNullable(fieldNameMap.get(e.getKey())).orElseThrow(() -> new IllegalArgumentException(MessageFormat.format("fieldName={0}在model={1}中不存在", e.getKey(), modelName)));
            AbstractValue<?> velue = AbstractValue.create(key, e.getValue());
            return new Condition(key.getName(), velue, Operator.EQUAL);
        });

        Expression change = conditionStream.reduce(CommaCondition::new).orElseThrow(() -> new IllegalArgumentException(MessageFormat.format("data={0}无改变", data)));
        Expression condition = new Condition("id", new StringValue(id), Operator.EQUAL);
        mysqlDataOperation.updateData(modelName, change, condition);
    }

    @Override
    public void deleteModelData(String modelName, String id) {
        Expression condition = new Condition("id", new StringValue(id), Operator.EQUAL);
        mysqlDataOperation.deleteData(modelName, condition);
    }

    @Override
    public List<Map<String, Object>> getModelDataList(String modelName, Integer page, Integer size, String orderColumn, Boolean desc, Map<String, Object> searchParams) {
        Model model = modelService.getModelByName(modelName);


        List<Field> fields = model.getFields();
        Map<String, Field> fieldNameMap = fields.stream().collect(Collectors.toMap(Field::getName, Function.identity()));
        Stream<Expression> conditionStream = searchParams.entrySet().stream().filter(e-> e.getValue()!=null&&StringUtils.hasText(e.getValue().toString())).map(e ->
        {
            Field key = Optional.ofNullable(fieldNameMap.get(e.getKey())).orElseThrow(() -> new IllegalArgumentException(MessageFormat.format("fieldName={0}在model={1}中不存在", e.getKey(), modelName)));
            AbstractValue<?> velue = new ObjectVallue(e.getValue());
            return new Condition(key.getName(), velue, Operator.LIKE);
        });
        Expression searchCondition = conditionStream.reduce( CommaCondition::new).orElseGet(()->new NullCondition());
        return mysqlDataOperation.getData(modelName, searchCondition,orderColumn,desc, new RowBounds((page - 1) * size, size));

    }

    @Override
    public Integer getTotalCount(String modelName) {
        return mysqlDataOperation.getTotalCount(modelName);
    }

    @Override
    public void exportModelDataTemplate(String modelName, HttpServletResponse response) throws IOException {
        Model model = modelService.getModelByName(modelName);
        List<Field> fields = model.getFields();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode(modelName, "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        List<List<String>> head = List.of(fields.stream().filter(s->!"id".equals(s.getName())).map(Field::getName).collect(Collectors.toList())
        ,fields.stream().filter(s->!"id".equals(s.getName())).map(Field::getComment).collect(Collectors.toList()));
        EasyExcel.write(response.getOutputStream()).head(head).sheet("模板");
    }

    @Override
    public Workbook exportModelDataList(String modelName, List<Map<String, Object>> res) throws IOException {
        Model model = modelService.getModelByName(modelName);
        List<Field> fields = model.getFields();
        Map<String, Field> fieldNameMap = fields.stream().collect(Collectors.toMap(Field::getName, Function.identity()));
        try(XSSFWorkbook workbook = new XSSFWorkbook()){
            //生成一个表格"
            XSSFSheet sheet = workbook.createSheet(modelName);
//            //设置表格列宽度为30个字节
            sheet.setDefaultColumnWidth(30);
            sheet.createFreezePane(0, 2);
//            CellStyle commonStyle = workbook.createCellStyle();
//            commonStyle.setDataFormat(workbook.createDataFormat().getFormat("@"));
//            //创建标题的显示样式
//            CellStyle headerStyle = workbook.createCellStyle();
//            Font font = workbook.createFont();
//            font.setBold(true);
//            headerStyle.setFont(font);
//            headerStyle.setLocked(true);
//            //设置居中
//            headerStyle.setAlignment(HorizontalAlignment.CENTER);
//            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
//            CellStyle unlock = workbook.createCellStyle();
//            unlock.setLocked(false);
//            //创建第一行表头
//            Row headRow = sheet.createRow(0);
//            for (int i = 0; i < fields.size(); i++) {
//                //创建一个单元格
//                Cell cell = headRow.createCell(i);
//                Field field = fields.get(i);
//                //创建一个内容对象
//                XSSFRichTextString text = new XSSFRichTextString(field.getName());
//                //将内容对象的文字内容写入到单元格中
//                cell.setCellValue(text);
//                cell.setCellStyle(headerStyle);
//            }
//            Row secondRow = sheet.createRow(1);
//            for (int i = 0; i < fields.size(); i++) {
//                //创建一个单元格
//                Cell cell = secondRow.createCell(i);
//                Field field = fields.get(i);
//                //创建一个内容对象
//                StringBuilder part = new StringBuilder();
//                part.append("类型:" + field.getType().name());
//                if (field.getType() == FieldTypeEnum.STRING || field.getType() == FieldTypeEnum.NUMBER || field.getType() == FieldTypeEnum.TIMESTAMP) {
//                    part.append(" 长度:" + field.getLength());
//                } else if (field.getType() == FieldTypeEnum.DATE) {
//                    part.append(" 案例数据格式:2023-05-25");
//                } else if (field.getType() == FieldTypeEnum.TIMESTAMP) {
//                    part.append(" 案例数据格式:2023-05-25 16:55:59");
//                } else if (field.getType() == FieldTypeEnum.BOOLEAN) {
//                    part.append(" 案例数据格式:true或者false");
//                }
//
//
//                part.append(" 是否可搜索:" + (Boolean.TRUE.equals(field.getSearchAble())?"是":"否"));
//
//                if (org.apache.commons.lang3.StringUtils.isNotBlank(field.getComment())) {
//                    part.append(" 描述:" + field.getComment());
//                }
//
//                XSSFRichTextString text = new XSSFRichTextString(part.toString());
//                //将内容对象的文字内容写入到单元格中
//                cell.setCellValue(text);
//                cell.setCellStyle(headerStyle);
//                sheet.setDefaultColumnStyle(i, commonStyle);
//
//
//            }
            return workbook;
        }
    }

}
