package com.tuling.tulingmall.promotion.service.impl;

import com.github.pagehelper.PageHelper;
import com.tuling.tulingmall.promotion.clientapi.PmsProductClientApi;
import com.tuling.tulingmall.promotion.domain.HomeContentResult;
import com.tuling.tulingmall.promotion.mapper.SmsHomeAdvertiseMapper;
import com.tuling.tulingmall.promotion.mapper.SmsHomeBrandMapper;
import com.tuling.tulingmall.promotion.mapper.SmsHomeNewProductMapper;
import com.tuling.tulingmall.promotion.mapper.SmsHomeRecommendProductMapper;
import com.tuling.tulingmall.promotion.model.*;
import com.tuling.tulingmall.promotion.service.HomePromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 首页内容管理Service实现类
 * Created by tuling on 2019/1/28.
 */
@Service
public class HomePromotionServiceImpl implements HomePromotionService {
    @Autowired
    private SmsHomeAdvertiseMapper advertiseMapper;
//    @Autowired
//    private HomeDao homeDao;
    @Autowired
    private SmsHomeBrandMapper smsHomeBrandMapper;
    @Autowired
    private SmsHomeNewProductMapper smsHomeNewProductMapper;
    @Autowired
    private SmsHomeRecommendProductMapper smsHomeRecommendProductMapper;
    @Autowired
    private PmsProductClientApi pmsProductClientApi;
//    @Autowired
//    private PmsProductMapper productMapper;
//    @Autowired
//    private PmsProductCategoryMapper productCategoryMapper;
//    @Autowired
//    private CmsSubjectMapper subjectMapper;

    @Override
    public HomeContentResult content() {
        HomeContentResult result = new HomeContentResult();
        //获取首页广告
        result.setAdvertiseList(getHomeAdvertiseList());
        //获取推荐品牌
        getRecommendBrand(result);
        //获取秒杀信息 首页显示
// todo       result.setHomeFlashPromotion(pmsProductFeignApi.getHomeSecKillProductList().getData());
        //获取新品推荐
        //result.setNewProductList(homeDao.getNewProductList(0,4));
        //获取人气推荐
        //result.setHotProductList(homeDao.getHotProductList(0,4));
        getRecommendProducts(result);
//        //获取推荐专题
//        result.setSubjectList(homeDao.getRecommendSubjectList(0,4));
        return result;
    }

    /*获取推荐品牌*/
    private void getRecommendBrand(HomeContentResult result){
        PageHelper.startPage(0,ConstantPromotion.HOME_RECOMMEND_PAGESIZE,"sort desc");
        SmsHomeBrandExample example = new SmsHomeBrandExample();
        example.or().andRecommendStatusEqualTo(ConstantPromotion.HOME_PRODUCT_RECOMMEND_NO);
        List<Long> smsHomeBrandIds = smsHomeBrandMapper.selectBrandIdByExample(example);
        List<PmsBrand> recommendBrandList = pmsProductClientApi.getRecommendBrandList(smsHomeBrandIds);
        result.setBrandList(recommendBrandList);
    }

    /*获取推荐产品*/
    private void getRecommendProducts(HomeContentResult result){
        PageHelper.startPage(0,ConstantPromotion.HOME_RECOMMEND_PAGESIZE,"sort desc");
        SmsHomeNewProductExample example = new SmsHomeNewProductExample();
        example.or().andRecommendStatusEqualTo(ConstantPromotion.HOME_PRODUCT_RECOMMEND_NO);
        List<Long> newProductIds = smsHomeNewProductMapper.selectProductIdByExample(example);
        List<PmsProduct> newProducts = pmsProductClientApi.getProductList(newProductIds);
        result.setNewProductList(newProducts);

        PageHelper.startPage(0,ConstantPromotion.HOME_RECOMMEND_PAGESIZE,"sort desc");
        SmsHomeRecommendProductExample example2 = new SmsHomeRecommendProductExample();
        example2.or().andRecommendStatusEqualTo(ConstantPromotion.HOME_PRODUCT_RECOMMEND_NO);
        List<Long> recommendProductIds = smsHomeRecommendProductMapper.selectProductIdByExample(example2);
        List<PmsProduct> recommendProducts = pmsProductClientApi.getProductList(recommendProductIds);
        result.setHotProductList(recommendProducts);

    }
//    @Override
//    public List<PmsProduct> recommendProductList(Integer pageSize, Integer pageNum) {
//        // TODO: 2019/1/29 暂时默认推荐所有商品
//        PageHelper.startPage(pageNum,pageSize);
//        PmsProductExample example = new PmsProductExample();
//        example.createCriteria()
//                .andDeleteStatusEqualTo(0)
//                .andPublishStatusEqualTo(1);
//        return productMapper.selectByExample(example);
//    }

//    @Override
//    public List<PmsProductCategory> getProductCateList(Long parentId) {
//        PmsProductCategoryExample example = new PmsProductCategoryExample();
//        example.createCriteria()
//                .andShowStatusEqualTo(1)
//                .andParentIdEqualTo(parentId);
//        example.setOrderByClause("sort desc");
//        return productCategoryMapper.selectByExample(example);
//    }

//    @Override
//    public List<CmsSubject> getSubjectList(Long cateId, Integer pageSize, Integer pageNum) {
//        PageHelper.startPage(pageNum,pageSize);
//        CmsSubjectExample example = new CmsSubjectExample();
//        CmsSubjectExample.Criteria criteria = example.createCriteria();
//        criteria.andShowStatusEqualTo(1);
//        if(cateId!=null){
//            criteria.andCategoryIdEqualTo(cateId);
//        }
//        return subjectMapper.selectByExample(example);
//    }

    private List<SmsHomeAdvertise> getHomeAdvertiseList() {
        SmsHomeAdvertiseExample example = new SmsHomeAdvertiseExample();
        Date now = new Date();
        example.createCriteria().andTypeEqualTo(ConstantPromotion.HOME_ADVERTISE_TYPE_APP)
                .andStatusEqualTo(ConstantPromotion.HOME_ADVERTISE_STATUS_ONLINE)
                .andStartTimeLessThan(now).andEndTimeGreaterThan(now);
        example.setOrderByClause("sort desc");
        return advertiseMapper.selectByExample(example);
    }
}
