package com.tuling.tulingmall.history.service;

import com.tuling.tulingmall.common.api.CommonResult;
import com.tuling.tulingmall.history.domain.OmsOrderDetail;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 订单迁移管理Service接口
 */
public interface OrderMigrateService {

    /**
     * 订单详情
     * @param orderId
     * @return
     */
    CommonResult getDetailOrder(Long orderId);

    /**
     * 删除订单[逻辑删除],只能status为：3->已完成；4->已关闭；5->无效订单，才可以删除
     * ，否则只能先取消订单然后删除。
     * @param orderId
     * @return
     *      受影响的行
     */
    @Transactional
    int deleteOrder(Long orderId);

    /**
     * 查询会员的订单
     * @param pageSize
     * @param pageNum
     * @param memberId
     *      会员ID
     * @param status
     *      订单状态
     * @return
     */
    CommonResult<List<OmsOrderDetail>> findMemberOrderList(Integer pageSize, Integer pageNum, Long memberId, Integer status);
}
