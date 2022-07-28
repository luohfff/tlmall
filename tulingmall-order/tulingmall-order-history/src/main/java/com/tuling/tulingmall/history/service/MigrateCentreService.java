package com.tuling.tulingmall.history.service;

/**
 * 订单迁移调度中心
 */
public interface MigrateCentreService {
    void migrateSingleTableOrders(int tableNo);

    void migrateTablesOrders();
}
