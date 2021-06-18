package com.imooc.controller;

import com.imooc.pojo.Items;
import com.imooc.pojo.ItemsImg;
import com.imooc.pojo.ItemsParam;
import com.imooc.pojo.ItemsSpec;
import com.imooc.pojo.vo.CommentLevelCountsVO;
import com.imooc.pojo.vo.ItemInfoVo;
import com.imooc.pojo.vo.ShopCartItemsVO;
import com.imooc.service.ItemService;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by guoming.zhang on 2021/2/25.
 */
@Api(value = "商品接口", tags = "商品信息展示的相关接口")
@RequestMapping("/items")
@RestController
public class ItemsController extends BaseController{

    @Autowired
    private ItemService itemService;

    @ApiOperation(value = "查询商品详情", notes = "查询商品详情", httpMethod = "GET")
    @GetMapping("/info/{itemId}")
    public IMOOCJSONResult info(
            @ApiParam(name = "itemId", value = "商品ID", required = true) @PathVariable String itemId) {
        if (StringUtils.isBlank(itemId)) {
            return IMOOCJSONResult.errorMsg(null);
        }
        Items items = itemService.queryItemById(itemId);
        List<ItemsImg> itemsImgs = itemService.queryItemImgList(itemId);
        List<ItemsSpec> itemsSpecs = itemService.queryItemSpecList(itemId);
        ItemsParam itemsParam = itemService.queryItemParm(itemId);
        ItemInfoVo itemInfoVo = new ItemInfoVo();
        itemInfoVo.setItem(items);
        itemInfoVo.setItemImgList(itemsImgs);
        itemInfoVo.setItemSpecList(itemsSpecs);
        itemInfoVo.setItemParams(itemsParam);
        return IMOOCJSONResult.ok(itemInfoVo);
    }

    @ApiOperation(value = "查询商品评价等级", notes = "查询商品评价等级", httpMethod = "GET")
    @GetMapping("/commentLevel")
    public IMOOCJSONResult commentLevel(
            @ApiParam(name = "itemId", value = "商品ID", required = true) @RequestParam String itemId) {
        if (StringUtils.isBlank(itemId)) {
            return IMOOCJSONResult.errorMsg(null);
        }
        CommentLevelCountsVO countsVO = itemService.queryCommentCounts(itemId);

        return IMOOCJSONResult.ok(countsVO);
    }

    @ApiOperation(value = "查询商品评价内容", notes = "查询商品评价内容", httpMethod = "GET")
    @GetMapping("/comments")
    public IMOOCJSONResult commentLevel(
            @ApiParam(name = "itemId", value = "商品ID", required = true) @RequestParam String itemId,
            @ApiParam(name = "level", value = "商品评价等级") @RequestParam Integer  level,
            @ApiParam(name = "page", value = "查询第几页") @RequestParam Integer  page,
            @ApiParam(name = "pageSize", value = "当前页返回记录数") @RequestParam Integer  pageSize) {
        if (StringUtils.isBlank(itemId)) {
            return IMOOCJSONResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult pagedGridResult = itemService.queryPagedComments(itemId, level, page, pageSize);

        return IMOOCJSONResult.ok(pagedGridResult);
    }

    @ApiOperation(value = "搜索商品查询", notes = "搜索商品查询", httpMethod = "GET")
    @GetMapping("/search")
    public IMOOCJSONResult search(
            @ApiParam(name = "keywords", value = "搜索关键词", required = true) @RequestParam String keywords,
            @ApiParam(name = "sort", value = "排序") @RequestParam String  sort,
            @ApiParam(name = "page", value = "查询第几页") @RequestParam Integer  page,
            @ApiParam(name = "pageSize", value = "当前页返回记录数") @RequestParam Integer  pageSize) {
        if (StringUtils.isBlank(keywords)) {
            return IMOOCJSONResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }

        PagedGridResult pagedGridResult = itemService.querySearchItems(keywords, sort, page, pageSize);

        return IMOOCJSONResult.ok(pagedGridResult);
    }

    @ApiOperation(value = "根据三级分类进行商品查询", notes = "根据三级分类进行商品查询", httpMethod = "GET")
    @GetMapping("/catItems")
    public IMOOCJSONResult catItems(
            @ApiParam(name = "catId", value = "三级分类ID", required = true) @RequestParam Integer catId,
            @ApiParam(name = "sort", value = "排序") @RequestParam String  sort,
            @ApiParam(name = "page", value = "查询第几页") @RequestParam Integer  page,
            @ApiParam(name = "pageSize", value = "当前页返回记录数") @RequestParam Integer  pageSize) {
        if (catId == null) {
            return IMOOCJSONResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }

        PagedGridResult pagedGridResult = itemService.querySearchItemsByThirdCat(catId, sort, page, pageSize);

        return IMOOCJSONResult.ok(pagedGridResult);
    }

    @ApiOperation(value = "根据商品规格，查询购物车列表", notes = "根据商品规格，查询购物车列表", httpMethod = "GET")
    @GetMapping("/refresh")
    public IMOOCJSONResult refresh(
            @ApiParam(name = "itemSpecIds", value = "拼接的规格ids", required = true, example = "1001,1002,1003")
            @RequestParam String itemSpecIds) {
        if (StringUtils.isBlank(itemSpecIds)) {
            return IMOOCJSONResult.ok();
        }
        List<ShopCartItemsVO> shopCartItemsVOS = itemService.queryItemsBySpecIds(itemSpecIds);
        return IMOOCJSONResult.ok(shopCartItemsVOS);
    }
}
