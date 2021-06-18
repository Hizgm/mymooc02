package com.imooc.pojo.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by guoming.zhang on 2021/2/24.
 */
@Getter
@Setter
public class SubCategoryVO {
    private Integer subId;
    private String subName;
    private Integer subType;
    private Integer subFatherId;
}
