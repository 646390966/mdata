package com.dataan.db;

import org.springframework.transaction.TransactionStatus;

/**
 * @author zhan bing liang
 * @date 2024/3/20 11:08
 */
public interface DbTransaction {
    /**
     * 事务方式运行
     * @param dbOperation
     */
    void run(DbOperation dbOperation);

    /**
     * 提交事务
     * @param transactionStatus
     */
    void commit(TransactionStatus transactionStatus);

    /**
     * 回滚事务
     * @param transactionStatus
     */
    void rollback(TransactionStatus transactionStatus);


}
