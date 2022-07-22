package com.tuling.tulingmall.portal.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.pagehelper.PageHelper;
import com.tuling.tulingmall.mapper.CmsSubjectMapper;
import com.tuling.tulingmall.mapper.PmsProductCategoryMapper;
import com.tuling.tulingmall.mapper.PmsProductMapper;
import com.tuling.tulingmall.mapper.SmsHomeAdvertiseMapper;
import com.tuling.tulingmall.model.*;
import com.tuling.tulingmall.portal.config.PromotionRedisKey;
import com.tuling.tulingmall.portal.dao.HomeDao;
import com.tuling.tulingmall.portal.domain.FlashPromotionProduct;
import com.tuling.tulingmall.portal.domain.HomeContentResult;
import com.tuling.tulingmall.portal.feignapi.pms.PmsProductFeignApi;
import com.tuling.tulingmall.portal.feignapi.promotion.PromotionFeignApi;
import com.tuling.tulingmall.portal.service.HomeService;
import com.tuling.tulingmall.portal.util.RedisOpsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * 首页内容管理Service实现类
 */
@Slf4j
@Service
public class HomeServiceImpl implements HomeService {
    @Autowired
    private SmsHomeAdvertiseMapper advertiseMapper;
    @Autowired
    private HomeDao homeDao;
    @Autowired
    private PmsProductFeignApi pmsProductFeignApi;
    @Autowired
    private PmsProductMapper productMapper;
    @Autowired
    private PmsProductCategoryMapper productCategoryMapper;
    @Autowired
    private CmsSubjectMapper subjectMapper;
    @Autowired
    private PromotionFeignApi promotionFeignApi;
    @Autowired
    private PromotionRedisKey promotionRedisKey;
    @Autowired
    private RedisOpsUtil redisOpsUtil;

    @Autowired
    @Qualifier("promotion")
    private Cache<String, HomeContentResult> promotionCache;

    @Autowired
    @Qualifier("promotionBak")
    private Cache<String, HomeContentResult> promotionCacheBak;

    @Override
    public HomeContentResult cmsContent(HomeContentResult content) {
        //获取推荐专题
        content.setSubjectList(homeDao.getRecommendSubjectList(0,4));
        return content;
    }

    /*处理首页推荐品牌和商品内容*/
    public HomeContentResult recommendContent(){
        /*品牌和产品在本地缓存中统一处理，有则视为同有，无则视为同无*/
        final String brandKey = promotionRedisKey.getBrandKey();
        final boolean allowLocalCache = promotionRedisKey.isAllowLocalCache();
        /*先从本地缓存中获取推荐内容*/
        HomeContentResult result = allowLocalCache ?
                promotionCache.getIfPresent(brandKey) : null;
        if(result == null){
            result = allowLocalCache ?
                    promotionCacheBak.getIfPresent(brandKey) : null;
        }
        /*本地缓存中没有*/
        if(result == null){
            log.warn("从本地缓存中获取推荐品牌和商品失败，可能出错或禁用了本地缓存[allowLocalCache = {}]",allowLocalCache);
            result = getFromRemote();
            if(null != result) {
                promotionCache.put(brandKey,result);
                promotionCacheBak.put(brandKey,result);
            }
        }
        // fixme 增加秒杀空集合和CMS推荐空集合
        result.setHomeFlashPromotion(new ArrayList<FlashPromotionProduct>());
        result.setSubjectList(new ArrayList<CmsSubject>());
        return result;
    }

    /*从远程(Redis或者对应微服务)获取推荐内容*/
    public HomeContentResult getFromRemote(){
        List<PmsBrand> recommendBrandList = null;
        List<SmsHomeAdvertise> smsHomeAdvertises = null;
        List<PmsProduct> newProducts  = null;
        List<PmsProduct> recommendProducts  = null;
        HomeContentResult result = null;
        /*从redis获取*/
        if(promotionRedisKey.isAllowRemoteCache()){
            recommendBrandList = redisOpsUtil.getListAll(promotionRedisKey.getBrandKey(), PmsBrand.class);
            smsHomeAdvertises = redisOpsUtil.getListAll(promotionRedisKey.getHomeAdvertiseKey(), SmsHomeAdvertise.class);
            newProducts = redisOpsUtil.getListAll(promotionRedisKey.getNewProductKey(), PmsProduct.class);
            recommendProducts = redisOpsUtil.getListAll(promotionRedisKey.getRecProductKey(), PmsProduct.class);
        }
        /*redis没有则从微服务中获取*/
        if(CollectionUtil.isEmpty(recommendBrandList)
                ||CollectionUtil.isEmpty(smsHomeAdvertises)
                ||CollectionUtil.isEmpty(newProducts)
                ||CollectionUtil.isEmpty(recommendProducts)) {
            result = promotionFeignApi.content(0).getData();
        }else{
            result = new HomeContentResult();
            result.setBrandList(recommendBrandList);
            result.setAdvertiseList(smsHomeAdvertises);
            result.setHotProductList(recommendProducts);
            result.setNewProductList(newProducts);
        }
        return result;
    }

    /*缓存预热*/
    public void preheatCache(){
        try {
            if(promotionRedisKey.isAllowLocalCache()){
                final String brandKey = promotionRedisKey.getBrandKey();
                HomeContentResult result = getFromRemote();
                promotionCache.put(brandKey,result);
                promotionCacheBak.put(brandKey,result);
                log.info("promotionCache 数据缓存预热完成");
            }
        } catch (Exception e) {
            log.error("promotionCache 数据缓存预热失败：{}",e);
        }
    }

    @Override
    public List<PmsProduct> recommendProductList(Integer pageSize, Integer pageNum) {
        // TODO: 2019/1/29 暂时默认推荐所有商品
        PageHelper.startPage(pageNum,pageSize);
        PmsProductExample example = new PmsProductExample();
        example.createCriteria()
                .andDeleteStatusEqualTo(0)
                .andPublishStatusEqualTo(1);
        return productMapper.selectByExample(example);
    }

    @Override
    public List<PmsProductCategory> getProductCateList(Long parentId) {
        PmsProductCategoryExample example = new PmsProductCategoryExample();
        example.createCriteria()
                .andShowStatusEqualTo(1)
                .andParentIdEqualTo(parentId);
        example.setOrderByClause("sort desc");
        return productCategoryMapper.selectByExample(example);
    }

    @Override
    public List<CmsSubject> getSubjectList(Long cateId, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        CmsSubjectExample example = new CmsSubjectExample();
        CmsSubjectExample.Criteria criteria = example.createCriteria();
        criteria.andShowStatusEqualTo(1);
        if(cateId!=null){
            criteria.andCategoryIdEqualTo(cateId);
        }
        return subjectMapper.selectByExample(example);
    }

}
