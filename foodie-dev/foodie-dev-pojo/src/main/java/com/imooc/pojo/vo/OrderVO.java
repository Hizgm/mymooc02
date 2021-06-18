package com.imooc.pojo.vo;

import com.imooc.pojo.Bo.ShopCatBO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderVO {

    private String orderId;         // 订单号
    private MerchantOrdersVO merchantOrdersVO;   // 订单VO
    private List<ShopCatBO> shopCatBOList;
}