package com.tuling.tulingmall.portal.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.tuling.tulingmall.portal.config.PromotionRedisKey;
import com.tuling.tulingmall.portal.domain.HomeContentResult;
import com.tuling.tulingmall.portal.service.HomeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/*
* 异步刷新促销信息在本地的缓存*/
@Service
@Slf4j
public class RefreshPromotionCache {

    @Autowired
    private HomeService homeService;

    @Autowired
    @Qualifier("promotion")
    private Cache<String, HomeContentResult> promotionCache;

    @Autowired
    @Qualifier("promotionBak")
    private Cache<String, HomeContentResult> promotionCacheBak;

    @Autowired
    private PromotionRedisKey promotionRedisKey;

    @Async
    @Scheduled(initialDelay=5000,fixedDelay = 1000)
    public void refreshCache(){
        if(promotionRedisKey.isAllowLocalCache()){
            final String brandKey = promotionRedisKey.getBrandKey();
            if(null == promotionCache.getIfPresent(brandKey)||null == promotionCacheBak.getIfPresent(brandKey)){
                HomeContentResult result = homeService.getFromRemote();
                if(null != result){
                    if(null == promotionCache.getIfPresent(brandKey)) {
                        promotionCache.put(brandKey,result);
                        log.debug("刷新本地缓存 promotionCache 成功");
                    }
                    if(null == promotionCacheBak.getIfPresent(brandKey)) {
                        promotionCacheBak.put(brandKey,result);
                        log.debug("刷新本地缓存 promotionCacheBak 成功");
                    }
                }else{
                    log.warn("从远程获得 promotionCache 数据失败");
                }
            }
        }
    }

}
