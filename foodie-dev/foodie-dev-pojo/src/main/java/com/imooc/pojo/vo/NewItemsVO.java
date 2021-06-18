package com.imooc.pojo.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 最新商品的VO
 * Created by guoming.zhang on 2021/2/25.
 */
@Getter
@Setter
public class NewItemsVO {

    private Integer rootCatId;
    private String rootCatName;
    private String slogan;
    private String catImage;
    private String bgColor;
    private List<SimpleItemVO> simpleItemList;
}
