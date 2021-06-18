package com.imooc.enums;



/**
 * Created by guoming.zhang on 2021/2/2.
 */

public enum Sex {
    woman(0, "女"),
    man(1, "男"),
    secret(2, "保密");

    public final Integer type;

    public final String value;

    Sex(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
