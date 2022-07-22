package com.tuling.tulingmall.controller;

import com.tuling.tulingmall.common.api.CommonResult;
import com.tuling.tulingmall.domain.CartPromotionItem;
import com.tuling.tulingmall.domain.StockChanges;
import com.tuling.tulingmall.service.StockManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ：图灵学院
 **/
@RestController
@RequestMapping("/stock")
public class StockManageController {

    @Autowired
    private StockManageService stockManageService;

    /**
     * 减库存
     * @param productId
     *        产品ID
     * @param skuId
     *        sku-ID
     * @param quanlity
     *        数量
     * @param miaosha
     *      1->减秒杀库存,0->减sku库存
     * @return
     */
    @RequestMapping(value = "/descStock",method = {RequestMethod.GET,RequestMethod.POST})
    public CommonResult descStock(@RequestParam Long productId, Long skuId, Integer quanlity, Integer miaosha){

        return null;
    }

    /**
     * 加库存
     * @param productId
     *        产品ID
     * @param skuId
     *        sku-ID
     * @param quanlity
     *        数量
     * @param miaosha
     *        1->加回秒杀库存,0->加回sku库存
     * @return
     */
    @RequestMapping(value = "/incrStock",method = {RequestMethod.GET,RequestMethod.POST})
    public CommonResult incrStock(@RequestParam Long productId,Long skuId,Integer quanlity,Integer miaosha){
        return null;
    }

    /**
     * 查询当前产品的库存
     * @param productId
     * @return
     */
    @RequestMapping(value = "/selectStock",method = {RequestMethod.GET,RequestMethod.POST})
    public CommonResult<Integer> selectStock(@RequestParam("productId") Long productId,@RequestParam("flashPromotionRelationId") Long flashPromotionRelationId){
        return stockManageService.selectStock(productId,flashPromotionRelationId);
    }

    /**
    * @desc: 类的描述:锁定库存
    */
    @RequestMapping("/lockStock")
    public CommonResult lockStock(@RequestBody List<CartPromotionItem> cartPromotionItemList) {
        return stockManageService.lockStock(cartPromotionItemList);
    }

    @RequestMapping("/reduceStock")
    CommonResult reduceStock(@RequestBody List<StockChanges> stockChangesList){
        return stockManageService.reduceStock(stockChangesList);
    }

    @RequestMapping("/recoverStock")
    CommonResult recoverStock(@RequestBody List<StockChanges> stockChangesList){
        return stockManageService.recoverStock(stockChangesList);
    }
}
