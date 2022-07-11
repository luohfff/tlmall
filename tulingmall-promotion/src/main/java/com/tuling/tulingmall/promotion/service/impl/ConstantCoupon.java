package com.tuling.tulingmall.promotion.service.impl;

public class ConstantCoupon {

    /*优惠券类型；0->全场赠券；1->会员赠券；2->购物赠券；3->注册赠券*/
    public final static int COUPON_TYPE_GENERAL = 0;
    public final static int COUPON_TYPE_USER = 0;
    public final static int COUPON_TYPE_SHOPPING = 0;
    public final static int COUPON_TYPE_REGISTER = 0;

    /*优惠券使用平台：0->全部；1->移动；2->PC*/
    public final static int COUPON_PLATFORM_ALL = 0;
    public final static int COUPON_PLATFORM_PC = 2;
    public final static int COUPON_PLATFORM_MOBIL = 1;

    /*优惠券使用类型：0->全场通用；1->指定分类；2->指定商品'*/
    public final static int COUPON_USE_TYPE_GENERAL = 0;
    public final static int COUPON_USE_TYPE_SPEC_KIND = 1;
    public final static int COUPON_USE_TYPE_SPEC_PRODUCT = 2;

    /*用户优惠券获取方式：0->后台赠送；1->主动获取'*/
    public final static int USER_COUPON_GET_TYPE_GIFT = 0;
    public final static int USER_COUPON_GET_TYPE_PROACTIVE = 1;

    /*用户优惠券使用状态：0->未使用；1->已使用；2->已过期'*/
    public final static int USER_COUPON_USE_STATE_UNUSE = 0;
    public final static int USER_COUPON_USE_STATE_USED = 1;
    public final static int USER_COUPON_USE_STATE_OVERDUE = 2;

}
