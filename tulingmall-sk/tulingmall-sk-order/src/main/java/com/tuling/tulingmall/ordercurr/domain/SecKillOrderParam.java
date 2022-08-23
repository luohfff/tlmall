package com.tuling.tulingmall.ordercurr.domain;

import java.util.List;

/**
 * 生成秒杀订单时传入的参数
 */
public class SecKillOrderParam {

    /*可用于避免重复生成订单*/
    private Long orderId;

    /*秒杀活动的ID*/
    private Long flashPromotionId;

    //收货地址id
    private Long memberReceiveAddressId;
    //支付方式
    private Integer payType;
    //选择购买的购物车商品
    private List<Long> itemIds;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public List<Long> getItemIds() {
        return itemIds;
    }

    public void setItemIds(List<Long> itemIds) {
        this.itemIds = itemIds;
    }

    public Long getMemberReceiveAddressId() {
        return memberReceiveAddressId;
    }

    public void setMemberReceiveAddressId(Long memberReceiveAddressId) {
        this.memberReceiveAddressId = memberReceiveAddressId;
    }


    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Long getFlashPromotionId() {
        return flashPromotionId;
    }

    public void setFlashPromotionId(Long flashPromotionId) {
        this.flashPromotionId = flashPromotionId;
    }

    @Override
    public String toString() {
        return "SecKillOrderParam{" +
                "orderId=" + orderId +
                ", flashPromotionId=" + flashPromotionId +
                ", memberReceiveAddressId=" + memberReceiveAddressId +
                ", payType=" + payType +
                ", itemIds=" + itemIds +
                '}';
    }
}
