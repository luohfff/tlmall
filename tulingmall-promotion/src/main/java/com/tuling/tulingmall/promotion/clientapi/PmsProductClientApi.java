package com.tuling.tulingmall.promotion.clientapi;

import com.tuling.tulingmall.promotion.clientapi.interceptor.config.FeignConfig;
import com.tuling.tulingmall.promotion.model.PmsBrand;
import com.tuling.tulingmall.promotion.model.PmsProduct;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @desc: 类的描述:Feign远程调用商品服务接口
 */
@FeignClient(name = "tulingmall-product", configuration = FeignConfig.class)
public interface PmsProductClientApi {

    @RequestMapping(value = "/getRecommendBrandList", method = RequestMethod.GET)
    @ResponseBody
    List<PmsBrand> getRecommendBrandList(List<Long> brandIdList);

    @RequestMapping(value = "/getProductList", method = RequestMethod.GET)
    @ResponseBody
    List<PmsProduct> getProductList(List<Long> productIdList);
}
