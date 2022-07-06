package com.tuling.tulingmall.controller;

import com.tuling.tulingmall.common.api.CommonResult;
import com.tuling.tulingmall.common.exception.BusinessException;
import com.tuling.tulingmall.service.SecKillOrderService;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author roy
 * @desc
 */
@Controller
@RequestMapping("/cartitem")
@Api(tags = "CartItemController")
public class CartItemController {

    @Resource
    private SecKillOrderService secKillOrderService;

    /**
     * 秒杀订单确认页
     * @param productId
     * @param memberId
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/miaosha/generateConfirmOrder",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult generateMiaoShaConfirmOrder(@RequestParam("productId") Long productId,
                                                    String token,
                                                    @RequestHeader("memberId") Long memberId) throws BusinessException {
        return secKillOrderService.generateConfirmMiaoShaOrder(productId,memberId,token);
    }
}
