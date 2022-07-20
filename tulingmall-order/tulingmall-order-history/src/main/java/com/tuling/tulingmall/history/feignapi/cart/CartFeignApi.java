package com.tuling.tulingmall.history.feignapi.cart;

import com.tuling.tulingmall.history.domain.CartPromotionItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
* @desc: 调用购物车服务
*/
@FeignClient(name = "tulingmall-cart")
public interface CartFeignApi {


//    public String getSegmentId(@PathVariable("key") String key) ;
    @RequestMapping(value = "/api/segment/get/{key}")
    public List<CartPromotionItem> listSelectedPromotion(List<Long> itemIds,Long memberId);

}
