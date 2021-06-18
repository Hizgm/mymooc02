package com.imooc.pojo.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 订单状态概览数量VO
 * Created by guoming.zhang on 2021/4/2.
 */
@Getter
@Setter
@NoArgsConstructor
public class OrderStatusCountsVO {
    private Integer waitPayCounts;
    private Integer waitDeliverCounts;
    private Integer waitReceiveCounts;
    private Integer waitCommentCounts;

    public OrderStatusCountsVO(Integer waitPayCounts, Integer waitDeliverCounts, Integer waitReceiveCounts, Integer waitCommentCounts) {
        this.waitPayCounts = waitPayCounts;
        this.waitDeliverCounts = waitDeliverCounts;
        this.waitReceiveCounts = waitReceiveCounts;
        this.waitCommentCounts = waitCommentCounts;
    }
}
