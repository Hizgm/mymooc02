package com.imooc.enums;

/**
 * Created by guoming.zhang on 2021/2/23.
 */
public enum YesOrNo {
    YES(1, "是"),
    NO(0, "否");

    public final Integer type;
    public final String value;

    YesOrNo(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
