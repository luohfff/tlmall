package com.tuling.tulingmall.promotion.domain;

import com.tuling.tulingmall.model.PmsProduct;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 秒杀信息和商品对象封装
 */
@Getter
@Setter
public class FlashPromotionProduct extends PmsProduct {
    private Long relationId;/*对应于sms_flash_promotion_product_relation中的id*/
    private Long flashPromotionId;/*对应于sms_flash_promotion中的id*/
    private BigDecimal flashPromotionPrice;
    private Integer flashPromotionCount;
    private Integer flashPromotionLimit;
    private Date flashPromotionStartDate;
    private Date flashPromotionEndDate;
    private String secKillServer;
}
