package com.tuling.tulingmall.history.dao;

import com.tuling.tulingmall.history.model.OmsOrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单商品信息自定义Dao
 * Created by macro on 2018/9/3.
 */
@Mapper
public interface PortalOrderItemDao {
    int insertList(@Param("list") List<OmsOrderItem> list);
}
