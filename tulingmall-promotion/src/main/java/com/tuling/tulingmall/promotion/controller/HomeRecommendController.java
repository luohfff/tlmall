package com.tuling.tulingmall.promotion.controller;

import com.tuling.tulingmall.common.api.CommonResult;
import com.tuling.tulingmall.promotion.domain.HomeContentResult;
import com.tuling.tulingmall.promotion.service.HomePromotionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 首页推荐管理Controller
 */
@Controller
@Api(tags = "HomeRecommendController", description = "品牌和产品推荐内容")
@RequestMapping("/recommend")
public class HomeRecommendController {
    @Autowired
    private HomePromotionService homePromotionService;

    @ApiOperation("首页品牌和产品推荐")
    @RequestMapping(value = "/content", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<HomeContentResult> content() {
        HomeContentResult contentResult = homePromotionService.content();
        return CommonResult.success(contentResult);
    }

//    @ApiOperation("分页获取推荐商品")
//    @RequestMapping(value = "/recommendProductList", method = RequestMethod.GET)
//    @ResponseBody
//    public CommonResult<List<PmsProduct>> recommendProductList(@RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize,
//                                                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
//        List<PmsProduct> productList = homeService.recommendProductList(pageSize, pageNum);
//        return CommonResult.success(productList);
//    }

//    @ApiOperation("获取首页商品分类")
//    @RequestMapping(value = "/productCateList/{parentId}", method = RequestMethod.GET)
//    @ResponseBody
//    public CommonResult<List<PmsProductCategory>> getProductCateList(@PathVariable Long parentId) {
//        List<PmsProductCategory> productCategoryList = homeService.getProductCateList(parentId);
//        return CommonResult.success(productCategoryList);
//    }

//    @ApiOperation("根据分类获取专题")
//    @RequestMapping(value = "/subjectList", method = RequestMethod.GET)
//    @ResponseBody
//    public CommonResult<List<CmsSubject>> getSubjectList(@RequestParam(required = false) Long cateId,
//                                                         @RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize,
//                                                         @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
//        List<CmsSubject> subjectList = homeService.getSubjectList(cateId,pageSize,pageNum);
//        return CommonResult.success(subjectList);
//    }
}
