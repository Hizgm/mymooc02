package com.imooc.enums;

import lombok.Getter;

/**
 * Created by guoming.zhang on 2021/2/25.
 */
public enum CommentLevel {
    GOOD(1, "好评"),
    NORMAL(2, "中评"),
    BAD(3, "差评");

    @Getter
    private final Integer type;
    @Getter
    private final String value;

    CommentLevel(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
