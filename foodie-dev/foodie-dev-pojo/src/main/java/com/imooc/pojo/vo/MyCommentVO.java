package com.imooc.pojo.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by guoming.zhang on 2021/4/2.
 */
@Getter
@Setter
public class MyCommentVO {
    private String commentId;
    private String content;
    private String createdTime;
    private String itemId;
    private String itemName;
    private String specName;
    private String itemImg;
}
