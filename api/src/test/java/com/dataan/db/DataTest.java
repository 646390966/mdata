package com.dataan.db;

import com.dataan.db.AbstractValue.DateValue;
import com.dataan.db.AbstractValue.StringValue;
import com.dataan.db.condition.CommaCondition;
import com.dataan.db.condition.Condition;
import com.dataan.db.condition.Condition.Operator;
import com.dataan.db.impl.MysqlDataOperation;
import com.dataan.db.impl.MysqlTableOperation;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.RowBounds;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhan bing liang
 * @date 2024/5/21 10:44
 */
@SpringBootTest
@Transactional
public class DataTest {
    @Autowired
    DbTransaction dbTransaction;
    @Autowired
    MysqlTableOperation mysqlTableOperation;
    @Autowired
    MysqlDataOperation mysqlDataOperation;
    private String tableName="cs";
    @Test
    void createDataTest() {

        dbTransaction.run(()->{
            Field field = new Field();
            field.setName("id");
            field.setType(FieldTypeEnum.STRING);
            field.setLength(11);
            Field field1 = new Field();
            field1.setName("name");
            field1.setType(FieldTypeEnum.STRING);
            field1.setLength(11);
            mysqlDataOperation.createData(tableName, List.of(field,field1),List.of(List.of(new StringValue("1"),new DateValue(LocalDate.now()))));

            mysqlDataOperation.createData(tableName, List.of(field,field1),List.of(List.of(new StringValue("2"),new DateValue(LocalDate.now()))));
        });
        List<Map<String, Object>> res = mysqlDataOperation.getData(tableName, new Condition("id", new StringValue("1"), Operator.EQUAL), "", null, new RowBounds(1,1000));
        Assertions.assertThat(res).isNotEmpty();
    }
    @Test
    void deleteDataTest() {
        createDataTest();
        Condition condition = new Condition();
        condition.setFieldName("id");
        condition.setFieldValue(new StringValue("1"));
        condition.setOperator(Operator.EQUAL);
        dbTransaction.run(()->{
            mysqlDataOperation.deleteData(tableName,condition);
        });
        List<Map<String, Object>> res = mysqlDataOperation.getData(tableName, new Condition("id", new StringValue("1"), Operator.EQUAL), "", null, new RowBounds(1, 1000));
        Assertions.assertThat(res).is(new org.assertj.core.api.Condition((s)->{

            return res==null;
        },"删除数据"));

    }

    @Test
    void updateDataTest() {
        createDataTest();
        Condition condition = new Condition();
        condition.setFieldName("id");
        condition.setFieldValue(new StringValue("1"));
        condition.setOperator(Operator.EQUAL);

        CommaCondition commaCondition = new CommaCondition(new Condition("id", new StringValue("3"), Operator.EQUAL), new Condition("name",  new StringValue("ha"), Operator.EQUAL));
        dbTransaction.run(()->{
            mysqlDataOperation.updateData(tableName,commaCondition,condition);
        });

        Assertions.assertThat(mysqlDataOperation.getData(tableName, new Condition("id",  new StringValue("3"), Operator.EQUAL), "", null, new RowBounds(1,1000))).is(new org.assertj.core.api.Condition((res)->{

            return res!=null;
        },"更新数据"));
    }
}
