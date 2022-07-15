package com.tuling.tulingmall.portal.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class PromotionRedisKey {

    @Value ("${namespace.promotion}")
    private String promotionNamespace;

    @Value ("${promotion.brand}")
    private String brand;

    @Value ("${promotion.newProduct}")
    private String newProduct;

    @Value ("${promotion.recProduct}")
    private String recProduct;

    @Value ("${promotion.homeAdvertise}")
    private String homeAdvertise;

    private String brandKey;
    private String newProductKey;
    private String recProductKey;
    private String homeAdvertiseKey;

    @PostConstruct
    public void initKey(){
        brandKey = promotionNamespace + "." + brand;
        newProductKey = promotionNamespace + "." + newProduct;
        recProductKey = promotionNamespace + "." + recProduct;
        homeAdvertiseKey = promotionNamespace + "." + homeAdvertise;
    }

    public String getBrandKey() {
        return brandKey;
    }

    public String getNewProductKey() {
        return newProductKey;
    }

    public String getRecProductKey() {
        return recProductKey;
    }

    public String getHomeAdvertiseKey() {
        return homeAdvertiseKey;
    }


    @Value("${promotion.allowLocalCache}")
    private boolean allowLocalCache;

    @Value("${promotion.allowRemoteCache}")
    private boolean allowRemoteCache;

    public boolean isAllowLocalCache() {
        return allowLocalCache;
    }

    public boolean isAllowRemoteCache() {
        return allowRemoteCache;
    }
}
