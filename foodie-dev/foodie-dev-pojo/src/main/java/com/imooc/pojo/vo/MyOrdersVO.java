package com.imooc.pojo.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * Created by guoming.zhang on 2021/3/29.
 */
@Getter
@Setter
public class MyOrdersVO {
    private String orderId;
    private Date createdTime;
    private Integer payMethod;
    private Integer realPayAmount;
    private Integer postAmount;
    private Integer isComment;
    private Integer orderStatus;

    private List<MySubOrderItemVO> subOrderItemList;

}
