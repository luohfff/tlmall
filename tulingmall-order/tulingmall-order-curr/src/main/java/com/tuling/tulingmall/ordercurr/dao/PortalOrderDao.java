package com.tuling.tulingmall.ordercurr.dao;

import com.tuling.tulingmall.ordercurr.domain.OmsOrderDetail;
import com.tuling.tulingmall.ordercurr.model.OmsOrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 前台订单自定义Dao
 */
@Mapper
public interface PortalOrderDao {
    /**
     * 获取订单及下单商品详情
     */
    OmsOrderDetail getDetail(@Param("orderId") Long orderId);

    /**
     * 获取超时订单
     * @param minute 超时时间（分）
     */
    List<OmsOrderDetail> getTimeOutOrders(@Param("minute") Integer minute);

    /**
     * 批量修改订单状态
     */
    int updateOrderStatus(@Param("ids") List<Long> ids, @Param("status") Integer status);

    /**
     * 删除订单[逻辑删除],只能status为：3->已完成；4->已关闭；5->无效订单，才可以删除
     * ，否则只能先取消订单然后删除。
     * @param orderId
     * @return
     *      受影响的行
     */
    int deleteOrder(@Param("orderId") Long orderId);

    /**
     * 查询会员的订单
     * @param memberId
     *      会员ID
     * @param status
     *      订单状态
     * @return
     */
    List<OmsOrderDetail> findMemberOrderList(@Param("memberId") Long memberId, @Param("status") Integer status);


}
