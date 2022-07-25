package com.tuling.tulingmall.history.controller;

import com.tuling.tulingmall.common.api.CommonResult;
import com.tuling.tulingmall.history.domain.OmsOrderDetail;
import com.tuling.tulingmall.history.service.OrderMigrateService;
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
    private OrderMigrateService portalOrderService;

    @ApiOperation("获取指定订单详情")
    @RequestMapping(value = "/specificOrderDetail",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public CommonResult specificOrderDetail(@RequestParam Long orderId){
        return portalOrderService.getDetailOrder(orderId);
    }

    @ApiOperation("获取指定业务订单详情")
    @RequestMapping(value = "/specificOrderSnDetail",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public CommonResult specificOrderSnDetail(@RequestParam String orderSn){
        /*在我们的实现中，业务订单orderSn和订单内部编号orderId是同一个，
        所以这里可以简单处理，实际工作中如果两者不一样，
        保证根据一定的规则可以从orderSn获得orderId即可*/
        return portalOrderService.getDetailOrder(Long.valueOf(orderSn));
    }


    /**
     * 订单服务由会员服务调用，会员服务传来会员：ID
     * @param memberId
     * @param status
     *      null查询所有
     *      订单状态0->待付款；1->待发货；2->已发货；3->已完成;4->已关闭；
     * @return
     */
    @ApiOperation("用户订单查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberId", value = "用户ID", required = true, paramType = "query", dataType = "integer"),
            @ApiImplicitParam(name = "status", value = "订单状态:0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭",
                    allowableValues = "0,1,2,3,4", paramType = "query", dataType = "integer")})
    @RequestMapping(value = "/list/userOrder",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<List<OmsOrderDetail>> findMemberOrderList(
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "memberId") Long memberId,
            @RequestParam(value = "status",required = false) Integer status){

        if(memberId == null || (status!=null && status > 4)){
            return CommonResult.validateFailed();
        }
        return portalOrderService.findMemberOrderList(pageSize,pageNum,memberId,status);
    }



}
