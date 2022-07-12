package com.tuling.tulingmall.promotion.service.impl;

import com.github.pagehelper.PageHelper;
import com.tuling.tulingmall.promotion.dao.HomeDao;
import com.tuling.tulingmall.promotion.domain.HomeContentResult;
import com.tuling.tulingmall.promotion.mapper.SmsHomeAdvertiseMapper;
import com.tuling.tulingmall.promotion.mapper.SmsHomeBrandMapper;
import com.tuling.tulingmall.promotion.model.*;
import com.tuling.tulingmall.promotion.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 首页内容管理Service实现类
 * Created by tuling on 2019/1/28.
 */
@Service
public class HomeServiceImpl implements HomeService {
    @Autowired
    private SmsHomeAdvertiseMapper advertiseMapper;
//    @Autowired
//    private HomeDao homeDao;
    @Autowired
    private SmsHomeBrandMapper smsHomeBrandMapper;
    @Autowired
    private PmsProductFeignApi pmsProductFeignApi;
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
        result.setBrandList(homeDao.getRecommendBrandList(0,4));
        //获取秒杀信息 首页显示
// todo       result.setHomeFlashPromotion(pmsProductFeignApi.getHomeSecKillProductList().getData());
        //获取新品推荐
        result.setNewProductList(homeDao.getNewProductList(0,4));
        //获取人气推荐
        result.setHotProductList(homeDao.getHotProductList(0,4));
//        //获取推荐专题
//        result.setSubjectList(homeDao.getRecommendSubjectList(0,4));
        return result;
    }

    private void getRecommendBrand(HomeContentResult result){
        SmsHomeBrandExample example = new SmsHomeBrandExample();

        List<SmsHomeBrand> smsHomeBrands = smsHomeBrandMapper.selectByExample();

    }

    private void getNewHotProducts(HomeContentResult result){


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
        example.createCriteria().andTypeEqualTo(1).andStatusEqualTo(1);
        example.setOrderByClause("sort desc");
        return advertiseMapper.selectByExample(example);
    }
}
