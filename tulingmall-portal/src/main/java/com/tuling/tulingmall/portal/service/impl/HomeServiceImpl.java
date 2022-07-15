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
import java.util.List;

/**
 * 首页内容管理Service实现类
 * Created by tuling on 2019/1/28.
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
    private Cache<String, HomeContentResult> caffeineCache;

    @Override
    public HomeContentResult cmsContent(HomeContentResult content) {
        //获取推荐专题
        content.setSubjectList(homeDao.getRecommendSubjectList(0,4));
        return content;
    }

    /*处理首页推荐品牌和商品内容*/
    public HomeContentResult recommendContent(){
        /*品牌和产品在缓存中统一处理，有则视为同有，无则视为同无*/
        final String brandKey = promotionRedisKey.getBrandKey();
        /*先从本地缓存中获取推荐内容*/
        HomeContentResult result = promotionRedisKey.isAllowLocalCache() ?
                caffeineCache.getIfPresent(brandKey) : null;
        if(result == null){
            final String recProductKey = promotionRedisKey.getRecProductKey();
            final String newProductKey = promotionRedisKey.getNewProductKey();
            final String homeAdvertiseKey = promotionRedisKey.getHomeAdvertiseKey();
            List<PmsBrand> recommendBrandList = null;
            List<SmsHomeAdvertise> smsHomeAdvertises = null;
            List<PmsProduct> newProducts  = null;
            List<PmsProduct> recommendProducts  = null;
            /*本地缓存中没有则从redis获取*/
            if(promotionRedisKey.isAllowRemoteCache()){
                recommendBrandList = redisOpsUtil.getListAll(brandKey, PmsBrand.class);
                smsHomeAdvertises = redisOpsUtil.getListAll(homeAdvertiseKey, SmsHomeAdvertise.class);
                newProducts = redisOpsUtil.getListAll(newProductKey, PmsProduct.class);
                recommendProducts = redisOpsUtil.getListAll(recProductKey, PmsProduct.class);
            }
            /*redis没有则从微服务中获取*/
            if(CollectionUtil.isEmpty(recommendBrandList)
                    ||CollectionUtil.isEmpty(smsHomeAdvertises)
                    ||CollectionUtil.isEmpty(newProducts)
                    ||CollectionUtil.isEmpty(recommendProducts)){
                result = promotionFeignApi.content(0).getData();
            }else{
                result = new HomeContentResult();
                result.setBrandList(recommendBrandList);
                result.setAdvertiseList(smsHomeAdvertises);
                result.setHotProductList(recommendProducts);
                result.setNewProductList(newProducts);
            }
            if(null != result) caffeineCache.put(brandKey,result);
        }
        return result;
    }

    /*缓存预热*/
//    @PostConstruct
//    public void preheatCache(){
//        try {
//            this.recommendContent();
//        } catch (Exception e) {
//            log.error("缓存预热失败：{}",e);
//        }
//    }

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

    private List<SmsHomeAdvertise> getHomeAdvertiseList() {
        SmsHomeAdvertiseExample example = new SmsHomeAdvertiseExample();
        example.createCriteria().andTypeEqualTo(1).andStatusEqualTo(1);
        example.setOrderByClause("sort desc");
        return advertiseMapper.selectByExample(example);
    }
}
