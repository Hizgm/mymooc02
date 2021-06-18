package com.imooc.enums;

/**
 * Created by guoming.zhang on 2021/2/24.
 */
public enum CategoryType {
    first(1, "一级分类"),
    second(2, "二级分类"),
    third(3, "三级分类");
    public Integer type;
    public String value;

    CategoryType(Integer type, String value) {
       this.type = type;
       this.value = value;
    }
}
