package com.imooc.pojo.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 二级分类VO
 * Created by guoming.zhang on 2021/2/24.
 */
@Setter
@Getter
public class CategoryVO {
    private Integer id;
    private String name;
    private Integer type;
    private Integer fatherId;

    //三级分类
    private List<SubCategoryVO> subCatList;
}
