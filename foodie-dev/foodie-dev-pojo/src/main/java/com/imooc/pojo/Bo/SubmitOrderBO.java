package com.imooc.pojo.Bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用于创建订单的BO对象
 * Created by guoming.zhang on 2021/3/10.
 */
@Getter
@Setter
@ToString
public class SubmitOrderBO {
    private String userId;
    private String itemSpecIds;
    private String addressId;
    private Integer payMethod;
    private String leftMsg;
}
