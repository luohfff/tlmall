package com.tuling.tulingmall.history.service.impl;

import com.tuling.tulingmall.history.domain.OmsOrderDetail;
import com.tuling.tulingmall.history.service.MigrateCentreService;
import com.tuling.tulingmall.history.service.OperateDbService;
import com.tuling.tulingmall.history.service.OperateMgDbService;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class MigrateCentreServiceImpl implements MigrateCentreService {

    private static final int TABLE_NO = 31;
    private static final int FETCH_RECORD_NUMBERS = 2000;
    private static final int DB_SLEEP_RND = 5;

    private static BlockingQueue<Runnable> tableQueue= new ArrayBlockingQueue<>(TABLE_NO + 1);

    private ThreadPoolExecutor executor = new ThreadPoolExecutor(0,Runtime.getRuntime().availableProcessors(),
            10,TimeUnit.SECONDS,tableQueue);

    @Autowired
    private OperateMgDbService operateMgDbService;
    @Autowired
    private OperateDbService operateDbService;

    public void migrateTablesOrders(){
        for(int i = 0; i <= TABLE_NO; i++){
            final int tableCount = i;
            executor.execute(()->migrateSingleTableOrders(tableCount));
        }
    }

    /*完成单表从MySQL到MongoDB的数据迁移，依然需要分次进行，
    每次的数据条数由FETCH_RECORD_NUMBERS控制，
    该控制阈值可以写入配置中心动态调整，建议不要超过10000*/
    @Override
    public void migrateSingleTableOrders(int tableNo) {
        try {
            /*获得3个月前的时间*/
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -3);
            Date date = calendar.getTime();
            /*开始迁移*/
            while(true){
                String factTableName = OrderConstant.OMS_ORDER_NAME_PREFIX + tableNo;
                /*获得上次处理的最大OrderId，作为本次迁移的起始ID*/
                long curMaxOrderId = operateMgDbService.getMaxOrderId(factTableName);
                List<OmsOrderDetail> fetchRecords = operateDbService.getOrders(curMaxOrderId,
                        tableNo,date,FETCH_RECORD_NUMBERS);
                if(!CollectionUtils.isEmpty(fetchRecords)){
                    int fetchSize = fetchRecords.size();
                    /*更新最大OrderId，记录本次迁移的最小ID*/
                    curMaxOrderId = fetchRecords.get(fetchRecords.size()-1).getId();
                    long curMinOrderId = fetchRecords.get(0).getId();
                    log.info("开始进行表[{}]数据迁移，应该迁移记录截止时间{},记录条数{}，min={},max={}",
                            factTableName,date,fetchSize,curMinOrderId,curMaxOrderId);
                    operateMgDbService.saveToMgDb(fetchRecords,curMaxOrderId,factTableName);
                    log.info("表[{}]本次数据迁移已完成，准备删除记录",factTableName);
                    operateDbService.deleteOrders(tableNo,date,curMaxOrderId);
                    /*考虑到数据库的负载，每次迁移后休眠随机数时间*/
                    int rnd = ThreadLocalRandom.current().nextInt(DB_SLEEP_RND);
                    log.info("表[{}]本次数据删除已完成，休眠[{}]S",factTableName,rnd);
                    TimeUnit.SECONDS.sleep(rnd);
                }else{
                    log.info("表[{}]本次数据迁移已完成",factTableName);
                    break;
                }
            }
        } catch (Exception e) {
            log.error("表[{}]本次数据迁移异常，已终止，请检查并手工修复：{}",
                    OrderConstant.OMS_ORDER_NAME_PREFIX + tableNo,e);
        }
    }
}
