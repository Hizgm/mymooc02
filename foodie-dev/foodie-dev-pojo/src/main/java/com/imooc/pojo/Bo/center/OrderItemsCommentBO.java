package com.imooc.pojo.Bo.center;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by guoming.zhang on 2021/4/1.
 */
@Getter
@Setter
@ToString(of = {"commentLevel", "content"})
public class OrderItemsCommentBO {
    private String commentId;
    private String itemId;
    private String itemName;
    private String itemSpecId;
    private String itemSpecName;
    private Integer commentLevel;
    private String content;
}
