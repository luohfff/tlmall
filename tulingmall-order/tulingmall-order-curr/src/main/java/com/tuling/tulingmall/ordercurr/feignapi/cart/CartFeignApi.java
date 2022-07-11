package com.tuling.tulingmall.ordercurr.feignapi.cart;

import com.tuling.tulingmall.ordercurr.domain.CartPromotionItem;

import java.util.List;

/**
* @desc: 调用购物车服务
*/
//@FeignClient(name = "tulingmall-unqid")
public interface CartFeignApi {

//    todo @RequestMapping(value = "/api/segment/get/{key}")
//    public String getSegmentId(@PathVariable("key") String key) ;

    public List<CartPromotionItem> listSelectedPromotion(List<Long> itemIds,Long memberId);

}
