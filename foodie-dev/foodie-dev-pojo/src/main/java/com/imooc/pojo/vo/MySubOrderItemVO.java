package com.imooc.pojo.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by guoming.zhang on 2021/3/29.
 */
@Getter
@Setter
public class MySubOrderItemVO {
    private String itemId;
    private String itemImg;
    private String itemName;
    private String itemSpecName;
    private Integer buyCounts;
    private Integer price;
}
