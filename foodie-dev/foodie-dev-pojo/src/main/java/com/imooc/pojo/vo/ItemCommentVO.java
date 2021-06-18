package com.imooc.pojo.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 用于展示商品评价VO
 * Created by guoming.zhang on 2021/2/25.
 */
@Getter
@Setter
public class ItemCommentVO {
    private Integer commentLevel;
    private String content;
    private String sepcName;
    private Date createdTime;
    private String userFace;
    private String nickname;
}
