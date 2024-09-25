package com.dataan.db.impl;

import com.dataan.db.DbOperation;
import com.dataan.db.DbTransaction;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

/**
 * @author zhan bing liang
 * @date 2024/5/15 14:35
 */
@Component
public class MySqlDbTransaction implements DbTransaction {


    @Resource
    private DataSourceTransactionManager dataSourceTransactionManager;
    @Autowired
    private TransactionDefinition transactionDefinition;

    @Override
    public void run(DbOperation dbOperation) {
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        try {
            dbOperation.run();
            commit(transactionStatus);
        } catch (Exception e) {
            rollback(transactionStatus);
            throw e;
        }
    }

    @Override
    public void commit(TransactionStatus transactionStatus) {
        dataSourceTransactionManager.commit(transactionStatus);
    }

    @Override
    public void rollback( TransactionStatus transactionStatus) {
        dataSourceTransactionManager.rollback(transactionStatus);
    }
}
