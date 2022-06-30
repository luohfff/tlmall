package com.tuling.tulingmall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tuling.tulingmall.dto.OmsOrderDeliveryParam;
import com.tuling.tulingmall.dto.OmsOrderDetail;
import com.tuling.tulingmall.dto.OmsOrderQueryParam;
import com.tuling.tulingmall.model.OmsOrder;
import com.tuling.tulingmall.model.OmsOrderReturnReason;
import com.tuling.tulingmall.model.OmsOrderReturnReasonExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OmsOrderReturnReasonMapper extends BaseMapper<OmsOrderReturnReason> {

    /**
     * 条件查询订单
     */
    List<OmsOrder> getList(@Param("queryParam") OmsOrderQueryParam queryParam);

    /**
     * 批量发货
     */
    int delivery(@Param("list") List<OmsOrderDeliveryParam> deliveryParamList);

    /**
     * 获取订单详情
     */
    OmsOrderDetail getDetail(@Param("id") Long id);
}