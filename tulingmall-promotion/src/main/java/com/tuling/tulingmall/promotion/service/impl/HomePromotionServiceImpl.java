package com.tuling.tulingmall.promotion.service.impl;

import com.github.pagehelper.PageHelper;
import com.tuling.tulingmall.promotion.clientapi.PmsProductClientApi;
import com.tuling.tulingmall.promotion.config.PromotionRedisKey;
import com.tuling.tulingmall.promotion.domain.HomeContentResult;
import com.tuling.tulingmall.promotion.mapper.SmsHomeAdvertiseMapper;
import com.tuling.tulingmall.promotion.mapper.SmsHomeBrandMapper;
import com.tuling.tulingmall.promotion.mapper.SmsHomeNewProductMapper;
import com.tuling.tulingmall.promotion.mapper.SmsHomeRecommendProductMapper;
import com.tuling.tulingmall.promotion.model.*;
import com.tuling.tulingmall.promotion.service.HomePromotionService;
import com.tuling.tulingmall.rediscomm.util.RedisDistrLock;
import com.tuling.tulingmall.rediscomm.util.RedisOpsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 首页促销内容Service实现类
 */
@Slf4j
@Service
public class HomePromotionServiceImpl implements HomePromotionService {
    @Autowired
    private SmsHomeAdvertiseMapper advertiseMapper;
    @Autowired
    private SmsHomeBrandMapper smsHomeBrandMapper;
    @Autowired
    private SmsHomeNewProductMapper smsHomeNewProductMapper;
    @Autowired
    private SmsHomeRecommendProductMapper smsHomeRecommendProductMapper;
    @Autowired
    private PmsProductClientApi pmsProductClientApi;
//    @Autowired
//    private PmsProductFeignApi pmsProductFeignApi;
    @Autowired
    private PromotionRedisKey promotionRedisKey;
    @Autowired
    private RedisOpsUtil redisOpsUtil;
    @Autowired
    private RedisDistrLock redisDistrLock;

    @Override
    public HomeContentResult content(int getType) {
        HomeContentResult result = new HomeContentResult();
        if(ConstantPromotion.HOME_GET_TYPE_ALL == getType
                ||ConstantPromotion.HOME_GET_TYPE_BARND == getType){
            //获取推荐品牌
            getRecommendBrand(result);
        }
        if(ConstantPromotion.HOME_GET_TYPE_ALL == getType
                ||ConstantPromotion.HOME_GET_TYPE_NEW == getType){
            getRecommendProducts(result);
        }
        if(ConstantPromotion.HOME_GET_TYPE_ALL == getType
                ||ConstantPromotion.HOME_GET_TYPE_HOT == getType){
            getHotProducts(result);
        }
        if(ConstantPromotion.HOME_GET_TYPE_ALL == getType
                ||ConstantPromotion.HOME_GET_TYPE_AD == getType){
            //获取首页广告
            result.setAdvertiseList(getHomeAdvertiseList());
        }
        //获取秒杀信息 首页显示
// todo       result.setHomeFlashPromotion(pmsProductFeignApi.getHomeSecKillProductList().getData());
        return result;
    }

    /*获取推荐品牌*/
    private void getRecommendBrand(HomeContentResult result){
        final String brandKey = promotionRedisKey.getBrandKey();
        List<PmsBrand> recommendBrandList = redisOpsUtil.getListAll(brandKey, PmsBrand.class);
        if(CollectionUtils.isEmpty(recommendBrandList)){
            redisDistrLock.lock(promotionRedisKey.getDlBrandKey(),promotionRedisKey.getDlTimeout());
            try {
                PageHelper.startPage(0,ConstantPromotion.HOME_RECOMMEND_PAGESIZE,"sort desc");
                SmsHomeBrandExample example = new SmsHomeBrandExample();
                example.or().andRecommendStatusEqualTo(ConstantPromotion.HOME_PRODUCT_RECOMMEND_NO);
                List<Long> smsHomeBrandIds = smsHomeBrandMapper.selectBrandIdByExample(example);
//                pmsProductFeignApi.getHomeSecKillProductList();
//                log.info("---------------------------");
                recommendBrandList = pmsProductClientApi.getRecommendBrandList(smsHomeBrandIds);
                redisOpsUtil.putListAllRight(brandKey,recommendBrandList);
            } finally {
                redisDistrLock.unlock(promotionRedisKey.getDlBrandKey());
            }
            result.setBrandList(recommendBrandList);
            log.info("品牌推荐信息存入缓存，键{}" ,brandKey);
        }else{
            log.info("品牌推荐信息已在缓存，键{}" ,brandKey);
            result.setBrandList(recommendBrandList);
        }
    }

    /*获取人气推荐产品*/
    private void getRecommendProducts(HomeContentResult result){
        final String recProductKey = promotionRedisKey.getRecProductKey();
        List<PmsProduct> recommendProducts = redisOpsUtil.getListAll(recProductKey, PmsProduct.class);
        if(CollectionUtils.isEmpty(recommendProducts)){
            redisDistrLock.lock(promotionRedisKey.getDlRecProductKey(),promotionRedisKey.getDlTimeout());
            try {
                PageHelper.startPage(0,ConstantPromotion.HOME_RECOMMEND_PAGESIZE,"sort desc");
                SmsHomeRecommendProductExample example2 = new SmsHomeRecommendProductExample();
                example2.or().andRecommendStatusEqualTo(ConstantPromotion.HOME_PRODUCT_RECOMMEND_NO);
                List<Long> recommendProductIds = smsHomeRecommendProductMapper.selectProductIdByExample(example2);
                recommendProducts = pmsProductClientApi.getProductList(recommendProductIds);
                redisOpsUtil.putListAllRight(recProductKey,recommendProducts);
            } finally {
                redisDistrLock.unlock(promotionRedisKey.getDlRecProductKey());
            }
            log.debug("人气推荐商品信息存入缓存，键{}" ,recProductKey);
            result.setHotProductList(recommendProducts);
        }else{
            log.debug("人气推荐商品信息已在缓存，键{}" ,recProductKey);
            result.setHotProductList(recommendProducts);
        }
    }

    /*获取新品推荐产品*/
    private void getHotProducts(HomeContentResult result){
        final String newProductKey = promotionRedisKey.getNewProductKey();
        List<PmsProduct> newProducts = redisOpsUtil.getListAll(newProductKey, PmsProduct.class);
        if(CollectionUtils.isEmpty(newProducts)){
            redisDistrLock.lock(promotionRedisKey.getDlNewProductKey(),promotionRedisKey.getDlTimeout());
            try {
                PageHelper.startPage(0,ConstantPromotion.HOME_RECOMMEND_PAGESIZE,"sort desc");
                SmsHomeNewProductExample example = new SmsHomeNewProductExample();
                example.or().andRecommendStatusEqualTo(ConstantPromotion.HOME_PRODUCT_RECOMMEND_NO);
                List<Long> newProductIds = smsHomeNewProductMapper.selectProductIdByExample(example);
                newProducts = pmsProductClientApi.getProductList(newProductIds);
                redisOpsUtil.putListAllRight(newProductKey,newProducts);
            } finally {
                redisDistrLock.unlock(promotionRedisKey.getDlNewProductKey());
            }
            log.debug("新品推荐信息存入缓存，键{}" ,newProductKey);
            result.setNewProductList(newProducts);
        }else{
            log.debug("新品推荐信息已在缓存，键{}" ,newProductKey);
            result.setNewProductList(newProducts);
        }
    }

    /*获取轮播广告*/
    private List<SmsHomeAdvertise> getHomeAdvertiseList() {
        final String homeAdvertiseKey = promotionRedisKey.getHomeAdvertiseKey();
        List<SmsHomeAdvertise> smsHomeAdvertises =
                redisOpsUtil.getListAll(homeAdvertiseKey, SmsHomeAdvertise.class);
        if(CollectionUtils.isEmpty(smsHomeAdvertises)){
            redisDistrLock.lock(promotionRedisKey.getDlHomeAdvertiseKey(),promotionRedisKey.getDlTimeout());
            try {
                SmsHomeAdvertiseExample example = new SmsHomeAdvertiseExample();
                Date now = new Date();
                example.createCriteria().andTypeEqualTo(ConstantPromotion.HOME_ADVERTISE_TYPE_APP)
                        .andStatusEqualTo(ConstantPromotion.HOME_ADVERTISE_STATUS_ONLINE)
                        .andStartTimeLessThan(now).andEndTimeGreaterThan(now);
                example.setOrderByClause("sort desc");
                smsHomeAdvertises = advertiseMapper.selectByExample(example);
                redisOpsUtil.putListAllRight(homeAdvertiseKey,smsHomeAdvertises);
            } finally {
                redisDistrLock.unlock(promotionRedisKey.getDlHomeAdvertiseKey());
            }
            log.debug("轮播广告存入缓存，键{}" ,homeAdvertiseKey);
            return smsHomeAdvertises;
        }else{
            log.debug("轮播广告已在缓存，键{}" ,homeAdvertiseKey);
            return smsHomeAdvertises;
        }
    }
}
