package com.tuling.tulingmall.history.service.impl;

import com.github.pagehelper.PageHelper;
import com.tuling.tulingmall.common.api.CommonResult;
import com.tuling.tulingmall.history.dao.PortalOrderDao;
import com.tuling.tulingmall.history.domain.*;
import com.tuling.tulingmall.history.mapper.OmsOrderItemMapper;
import com.tuling.tulingmall.history.mapper.OmsOrderMapper;
import com.tuling.tulingmall.history.service.OrderMigrateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 订单迁移管理Service实现
 */
@Service
@Slf4j
public class OrderMigrateServiceImpl implements OrderMigrateService {


    @Autowired
    private OmsOrderMapper omsOrderMapper;

    @Autowired
    private PortalOrderDao portalOrderDao;

    @Autowired
    private OmsOrderItemMapper orderItemMapper;


    /**
     * 查询用户订单
     * @param memberId 会员ID
     * @param status  订单状态
     */
    @Override
    public CommonResult<List<OmsOrderDetail>> findMemberOrderList(Integer pageSize, Integer pageNum, Long memberId, Integer status) {
        PageHelper.startPage(pageNum,pageSize);
        return CommonResult.success(portalOrderDao.findMemberOrderList(memberId,status));
    }

    /**
     * 删除订单[逻辑删除],只能status为：3->已完成；4->已关闭；5->无效订单，才可以删除
     * ，否则只能先取消订单然后删除。
     * @param orderId
     * @return  受影响的行数
     */
    @Override
    public int deleteOrder(Long orderId){
        return portalOrderDao.deleteOrder(orderId);
    }


    /**
     * 订单详情
     * @param orderId
     */
    public CommonResult getDetailOrder(Long orderId){
        return CommonResult.success(portalOrderDao.getDetail(orderId));
    }




}
