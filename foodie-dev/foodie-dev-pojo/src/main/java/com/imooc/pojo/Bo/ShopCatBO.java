package com.imooc.pojo.Bo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by guoming.zhang on 2021/3/4.
 */
@ApiModel(value = "用户添加购物车数据", description = "用户添加购物车数据，实体对象")
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class ShopCatBO {

    @ToString.Include
    private String itemId;
    private String itemImgUrl;
    private String itemName;

    @ToString.Include
    private String specId;
    private String specName;
    private Integer buyCounts;
    private String priceDiscount;
    private String priceNormal;
}
