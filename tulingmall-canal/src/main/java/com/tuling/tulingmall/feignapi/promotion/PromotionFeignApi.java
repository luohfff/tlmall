package com.tuling.tulingmall.feignapi.promotion;

import com.tuling.tulingmall.common.api.CommonResult;
import com.tuling.tulingmall.promotion.domain.FlashPromotionProduct;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
* @desc: 类的描述:远程调用获取首页显示内容，包括推荐和秒杀等
*/
@FeignClient(name = "tulingmall-promotion",path = "/recommend")
public interface PromotionFeignApi {

    /*获得秒杀内容*/
    @RequestMapping(value = "/getHomeSecKillProductList", method = RequestMethod.GET)
    @ResponseBody
    CommonResult<List<FlashPromotionProduct>> getHomeSecKillProductList(long secKillId);
}
