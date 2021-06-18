package com.imooc.pojo.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 用于展示商品评价数量的VO
 * Created by guoming.zhang on 2021/2/25.
 */
@Getter
@Setter
public class CommentLevelCountsVO {
    private Integer totalCounts;
    private Integer goodCounts;
    private Integer normalCounts;
    private Integer badCounts;
}
