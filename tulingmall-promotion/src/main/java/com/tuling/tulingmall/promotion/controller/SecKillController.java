package com.tuling.tulingmall.promotion.controller;

import com.tuling.tulingmall.common.api.CommonResult;
import com.tuling.tulingmall.promotion.dao.MiaoShaStockDao;
import com.tuling.tulingmall.promotion.domain.CartPromotionItem;
import com.tuling.tulingmall.promotion.domain.FlashPromotionProduct;
import com.tuling.tulingmall.promotion.domain.SmsCouponHistoryDetail;
import com.tuling.tulingmall.promotion.model.SmsCouponHistory;
import com.tuling.tulingmall.promotion.service.HomePromotionService;
import com.tuling.tulingmall.promotion.service.UserCouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 秒杀管理Controller
 */
@Controller
@Api(tags = "SecKillController", description = "秒杀管理")
@RequestMapping("/seckill")
public class SecKillController {

    @Autowired
    private HomePromotionService homePromotionService;
    @Autowired
    private MiaoShaStockDao miaoShaStockDao;

    /*获得秒杀内容*/
    @ApiOperation("获取秒杀产品")
    @RequestMapping(value = "/getHomeSecKillProductList", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<FlashPromotionProduct>> getHomeSecKillProductList(
            @RequestParam(value = "-1",required = false) long secKillId){
        List<FlashPromotionProduct> result = homePromotionService.secKillContent(secKillId);
        return CommonResult.success(result);
    }

    @ApiOperation("开启秒杀")
    @ApiImplicitParam(name = "secKillId", value = "秒杀活动ID")
    @RequestMapping(value = "/content", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<Integer> turnOnSecKill(@RequestParam long secKillId){
        int result = homePromotionService.turnOnSecKill(secKillId);
        return CommonResult.success(result);
    }

    /*扣减库存 要防止库存超卖*/
    @ApiOperation("扣减库存")
    @RequestMapping(value = "/descStock", method = RequestMethod.GET)
    @ResponseBody
    public Integer descStock(@RequestParam("id") Long flashPromotionRelationId,
                             @RequestParam("stock") Integer stock){
        return miaoShaStockDao.descStock(flashPromotionRelationId,stock);
    }

    @ApiOperation("增加库存")
    @RequestMapping(value = "/incStock", method = RequestMethod.GET)
    @ResponseBody
    public Integer incStock(@RequestParam("id") Long flashPromotionRelationId,
                             @RequestParam("stock") Integer stock){
        return miaoShaStockDao.incStock(flashPromotionRelationId,stock);
    }

    @ApiOperation("查询库存")
    @RequestMapping(value = "/getStock", method = RequestMethod.GET)
    @ResponseBody
    public Integer getStock(@RequestParam("id") Long flashPromotionRelationId){
        return miaoShaStockDao.getStock(flashPromotionRelationId);
    }

}
