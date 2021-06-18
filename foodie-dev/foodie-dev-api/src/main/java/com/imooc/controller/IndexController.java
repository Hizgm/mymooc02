package com.imooc.controller;

import com.imooc.enums.CategoryType;
import com.imooc.enums.YesOrNo;
import com.imooc.pojo.Carousel;
import com.imooc.pojo.Category;
import com.imooc.pojo.vo.CategoryVO;
import com.imooc.pojo.vo.NewItemsVO;
import com.imooc.service.CarouselService;
import com.imooc.service.CategoryService;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guoming.zhang on 2021/2/23.
 */
@Api(value = "首页", tags = {"首页展示的相关接口"})
@RestController
@RequestMapping("/index")
public class IndexController {

    @Autowired
    private CarouselService carouselService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisOperator redisOperator;

    @ApiOperation(value = "获取轮播图列表", notes = "获取轮播图列表", httpMethod = "GET")
    @GetMapping("/carousel")
    public IMOOCJSONResult carousel() {
        List<Carousel> list = new ArrayList<>();
        String carousel = redisOperator.get("carousel");
        if (StringUtils.isBlank(carousel)) {
            list = carouselService.queryAll(YesOrNo.YES.type);
            redisOperator.set("carousel", JsonUtils.objectToJson(list));
        } else {
            list = JsonUtils.jsonToList(carousel, Carousel.class);
        }
        return IMOOCJSONResult.ok(list);
    }

    /**
     * 首页分类展示需求：
     * 1. 第一次刷新主页查询大分类， 渲染展示到首页
     * 2. 如果鼠标移到大分类，则加载其子分类的内容，如果已经存在子分类，则不需要加载（懒加载）
     */
    @ApiOperation(value = "获取商品一级分类", notes = "获取商品一级分类", httpMethod = "GET")
    @GetMapping("/cats")
    public IMOOCJSONResult cats() {
        List<Category> list = new ArrayList<>();
        String categoriesStr = redisOperator.get("categories");
        if (StringUtils.isBlank(categoriesStr)) {
            list = categoryService.queryAllRootLevelCat(CategoryType.first.type);
            redisOperator.set("categories", JsonUtils.objectToJson(list));
        } else {
            list = JsonUtils.jsonToList(categoriesStr, Category.class);
        }
        return IMOOCJSONResult.ok(list);
    }

    @ApiOperation(value = "获取商品二级分类", notes = "获取商品二级分类", httpMethod = "GET")
    @GetMapping("/subCat/{rootCatId}")
    public IMOOCJSONResult subCat(
            @ApiParam(name = "rootCatId", value = "一级分类ID", required = true)
            @PathVariable Integer rootCatId) {
        if (rootCatId == null) {
            return IMOOCJSONResult.errorMsg("分类不存在");
        }
        List<CategoryVO> list = new ArrayList<>();
        String subCatListStr = redisOperator.get("categoryVO:"+rootCatId);
        if (StringUtils.isBlank(subCatListStr)) {
            list = categoryService.getSubCatList(rootCatId);
            /**
             * 问题一：
             * 查询的key在redis中不存在，对应的id在数据库也不存在，此时被非法用户进行攻击
             * 大量的请求会直接访问db上，可能会造成宕机，从而影响整个系统
             * 这种现象称之为缓存穿透。
             * 解决方案1： 把空的数据也缓存起来，比如空字符串，空对象，空数组或list
             * 方案二：布隆过滤器 不推荐使用
             *
             * 问题二:
             * 如果redis缓存在同一时间失效，恰好从客户端有大量的请求过来，这样会使数据库崩溃，
             * 这种现象称之为缓存雪崩
             * 解决方案：设置不同的缓存时间、用第三方redis、多缓存相结合
              */

            redisOperator.set("categoryVO:"+rootCatId, JsonUtils.objectToJson(list));
        } else {
            list = JsonUtils.jsonToList(subCatListStr, CategoryVO.class);
        }
        return IMOOCJSONResult.ok(list);
    }

    @ApiOperation(value = "查询每个一级分类下的最新6条商品数据", notes = "获取最新商品数据", httpMethod = "GET")
    @GetMapping("/sixNewItems/{rootCatId}")
    public IMOOCJSONResult sixNewItems(
            @ApiParam(name = "rootCatId", value = "一级分类ID", required = true)
            @PathVariable Integer rootCatId) {
        if (rootCatId == null) {
            return IMOOCJSONResult.errorMsg("分类不存在");
        }
        List<NewItemsVO> sixNewItemsList = categoryService.getSixNewItemsLazy(rootCatId);
        return IMOOCJSONResult.ok(sixNewItemsList);
    }
}
