package com.tuling.tulingmall.history.feignapi.promotion;

import com.tuling.tulingmall.common.api.CommonResult;
import com.tuling.tulingmall.history.domain.CartPromotionItem;
import com.tuling.tulingmall.history.domain.SmsCouponHistoryDetail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @desc: 调用优惠券服务
 */
@FeignClient(name = "tulingmall-promotion", path = "/coupon")
public interface PromotionFeignApi {

    /*"type", value = "使用可用:0->不可用；1->可用"*/
    @RequestMapping(value = "/listCart", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<List<SmsCouponHistoryDetail>> listCartCoupons(@RequestParam Integer type,
                    @RequestBody List<CartPromotionItem> cartPromotionItemList);
}
