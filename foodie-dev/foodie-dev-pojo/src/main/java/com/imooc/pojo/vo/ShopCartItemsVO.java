package com.imooc.pojo.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by guoming.zhang on 2021/3/4.
 */
@Getter
@Setter
public class ShopCartItemsVO {

    private String itemId;
    private String itemName;
    private String imgUrl;
    private String specId;
    private String specName;
    private Integer priceDiscount;
    private Integer priceNormal;
}
