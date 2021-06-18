package com.imooc.pojo.vo;

import com.imooc.pojo.Items;
import com.imooc.pojo.ItemsImg;
import com.imooc.pojo.ItemsParam;
import com.imooc.pojo.ItemsSpec;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


/**
 * 商品详情VO
 * Created by guoming.zhang on 2021/2/25.
 */
@Getter
@Setter
public class ItemInfoVo {
    private Items item;
    private List<ItemsImg> itemImgList;
    private List<ItemsSpec> itemSpecList;
    private ItemsParam itemParams;
}
