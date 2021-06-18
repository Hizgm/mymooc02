package com.imooc.service;

import com.imooc.pojo.*;
import com.imooc.pojo.vo.CommentLevelCountsVO;
import com.imooc.pojo.vo.ItemCommentVO;
import com.imooc.pojo.vo.ShopCartItemsVO;
import com.imooc.utils.PagedGridResult;

import java.util.List;

/**
 * Created by guoming.zhang on 2021/2/25.
 */
public interface ItemService {
    /**
     * 根据商品ID查询商品详情
     * @param itemId
     * @return
     */
    Items queryItemById(String itemId);

    /**
     * 根据商品ID获取商品图片列表
     * @param itemId
     * @return
     */
    List<ItemsImg> queryItemImgList(String itemId);

    /**
     * 根据商品ID获取商品的规格
     * @param itemId
     * @return
     */
    List<ItemsSpec> queryItemSpecList(String itemId);

    /**
     * 根据商品ID获取商品参数
     * @param itemId
     * @return
     */
    ItemsParam queryItemParm(String itemId);

    /**
     * 根据商品ID查询商品的评价等级数量
     * @param itemId
     * @return
     */
    CommentLevelCountsVO queryCommentCounts(String itemId);

    /**
     * 根据商品ID查询商品评价
     * @param itemId
     * @param level
     * @return
     */
    PagedGridResult queryPagedComments(String itemId, Integer level, Integer page, Integer pageSize);

    /**
     * 搜索查询并分页
     * @param keywords
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult querySearchItems(String keywords, String sort, Integer page, Integer pageSize);

    /**
     * 根据第三级分类ID，查询搜索商品
     * @param catId
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult querySearchItemsByThirdCat(Integer catId, String sort, Integer page, Integer pageSize);

    /**
     * 根据商品规格ids查询购物车列表
     * @param specId
     * @return
     */
    List<ShopCartItemsVO> queryItemsBySpecIds(String specId);

    /**
     * 根据商品规格id查询商品规格对象的具体信息
     * @param specId
     * @return
     */
    ItemsSpec queryItemsSpecById(String specId);

    /**
     * 根据商品id,获取商品的图片信息
     * @param itemId
     */
    ItemsImg queryItemsImgById(String itemId);

    /**
     * 减少库存
     * @param specId
     * @param buyCounts
     */
    void decreaseItemSpecStock(String specId, int buyCounts);
}

