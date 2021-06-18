package com.imooc.enums;

/**
 * Created by guoming.zhang on 2021/3/11.
 */
public enum PayMethod {
    WENXIN(1, "微信"),
    ZHIFUBAO(2, "支付宝");

    public final Integer type;
    public final String value;

    PayMethod(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
