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
import com.tuling.tulingmall.promotion.util.RedisOpsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    @Autowired
    private PromotionRedisKey promotionRedisKey;
    @Autowired
    private RedisOpsUtil redisOpsUtil;

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
        if(!redisOpsUtil.hasKey(brandKey)){
            PageHelper.startPage(0,ConstantPromotion.HOME_RECOMMEND_PAGESIZE,"sort desc");
            SmsHomeBrandExample example = new SmsHomeBrandExample();
            example.or().andRecommendStatusEqualTo(ConstantPromotion.HOME_PRODUCT_RECOMMEND_NO);
            List<Long> smsHomeBrandIds = smsHomeBrandMapper.selectBrandIdByExample(example);
            List<PmsBrand> recommendBrandList = pmsProductClientApi.getRecommendBrandList(smsHomeBrandIds);
            log.info("品牌推荐信息存入缓存，键{}" ,brandKey);
            redisOpsUtil.putListAllRight(brandKey,recommendBrandList);
            result.setBrandList(recommendBrandList);
        }else{
            log.info("品牌推荐信息已在缓存，键{}" ,brandKey);
            List<PmsBrand> recommendBrandList = redisOpsUtil.getListAll(brandKey, PmsBrand.class);
            result.setBrandList(recommendBrandList);
        }
    }

    /*获取人气推荐产品*/
    private void getRecommendProducts(HomeContentResult result){

        final String recProductKey = promotionRedisKey.getRecProductKey();
        if(!redisOpsUtil.hasKey(recProductKey)){
            PageHelper.startPage(0,ConstantPromotion.HOME_RECOMMEND_PAGESIZE,"sort desc");
            SmsHomeRecommendProductExample example2 = new SmsHomeRecommendProductExample();
            example2.or().andRecommendStatusEqualTo(ConstantPromotion.HOME_PRODUCT_RECOMMEND_NO);
            List<Long> recommendProductIds = smsHomeRecommendProductMapper.selectProductIdByExample(example2);
            List<PmsProduct> recommendProducts = pmsProductClientApi.getProductList(recommendProductIds);
            log.debug("人气推荐商品信息存入缓存，键{}" ,recProductKey);
            redisOpsUtil.putListAllRight(recProductKey,recommendProducts);
            result.setHotProductList(recommendProducts);
        }else{
            log.debug("人气推荐商品信息已在缓存，键{}" ,recProductKey);
            List<PmsProduct> recommendProducts = redisOpsUtil.getListAll(recProductKey, PmsProduct.class);
            result.setHotProductList(recommendProducts);
        }
    }

    /*获取新品推荐产品*/
    private void getHotProducts(HomeContentResult result){
        final String newProductKey = promotionRedisKey.getNewProductKey();
        if(!redisOpsUtil.hasKey(newProductKey)){
            PageHelper.startPage(0,ConstantPromotion.HOME_RECOMMEND_PAGESIZE,"sort desc");
            SmsHomeNewProductExample example = new SmsHomeNewProductExample();
            example.or().andRecommendStatusEqualTo(ConstantPromotion.HOME_PRODUCT_RECOMMEND_NO);
            List<Long> newProductIds = smsHomeNewProductMapper.selectProductIdByExample(example);
            List<PmsProduct> newProducts = pmsProductClientApi.getProductList(newProductIds);
            log.debug("新品推荐信息存入缓存，键{}" ,newProductKey);
            redisOpsUtil.putListAllRight(newProductKey,newProducts);
            result.setNewProductList(newProducts);
        }else{
            log.debug("新品推荐信息已在缓存，键{}" ,newProductKey);
            List<PmsProduct> newProducts = redisOpsUtil.getListAll(newProductKey, PmsProduct.class);
            result.setNewProductList(newProducts);
        }
    }

    /*获取轮播广告*/
    private List<SmsHomeAdvertise> getHomeAdvertiseList() {
        final String homeAdvertiseKey = promotionRedisKey.getHomeAdvertiseKey();
        if(!redisOpsUtil.hasKey(homeAdvertiseKey)){
            SmsHomeAdvertiseExample example = new SmsHomeAdvertiseExample();
            Date now = new Date();
            example.createCriteria().andTypeEqualTo(ConstantPromotion.HOME_ADVERTISE_TYPE_APP)
                    .andStatusEqualTo(ConstantPromotion.HOME_ADVERTISE_STATUS_ONLINE)
                    .andStartTimeLessThan(now).andEndTimeGreaterThan(now);
            example.setOrderByClause("sort desc");
            List<SmsHomeAdvertise> smsHomeAdvertises = advertiseMapper.selectByExample(example);
            log.debug("轮播广告存入缓存，键{}" ,homeAdvertiseKey);
            redisOpsUtil.putListAllRight(homeAdvertiseKey,smsHomeAdvertises);
            return smsHomeAdvertises;
        }else{
            log.debug("轮播广告已在缓存，键{}" ,homeAdvertiseKey);
            List<SmsHomeAdvertise> smsHomeAdvertises =
                    redisOpsUtil.getListAll(homeAdvertiseKey, SmsHomeAdvertise.class);
            return smsHomeAdvertises;
        }
    }
}
