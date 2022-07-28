package com.tuling.tulingmall.history.controller;

import com.tuling.tulingmall.common.api.CommonResult;
import com.tuling.tulingmall.history.domain.OmsOrderDetail;
import com.tuling.tulingmall.history.service.MigrateCentreService;
import com.tuling.tulingmall.history.service.OperateDbService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单迁移管理Controller
 */
@Slf4j
@Controller
@Api(tags = "OrderMigrateController",description = "订单迁移管理")
@RequestMapping("/order/migrate")
public class OrderMigrateController {

    @Autowired
    private MigrateCentreService migrateCentreService;

    @ApiOperation("指定数据表执行数据迁移")
    @RequestMapping(value = "/specificTableMigrate",method = {RequestMethod.POST,RequestMethod.GET})
    public void migrateSpecificTable(@RequestParam int tableNo){
        migrateCentreService.migrateSingleTableOrders(tableNo);
    }

    @ApiOperation("全部数据表进行迁移")
    @RequestMapping(value = "/migrateTables",method = {RequestMethod.POST,RequestMethod.GET})
    public void migrateTables(){
        migrateCentreService.migrateTablesOrders();
    }
}
