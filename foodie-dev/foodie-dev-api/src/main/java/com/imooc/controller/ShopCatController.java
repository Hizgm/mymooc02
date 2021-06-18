package com.imooc.controller;

import com.imooc.pojo.Bo.ShopCatBO;
import com.imooc.pojo.ItemsSpec;
import com.imooc.pojo.vo.ShopCartItemsVO;
import com.imooc.service.ItemService;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by guoming.zhang on 2021/3/4.
 */
@Api(value = "购物车相关接口", tags = "购物车相关接口")
@RequestMapping("/shopcart")
@RestController
public class ShopCatController extends BaseController{

    @Autowired
    private RedisOperator redisOperator;

    @ApiOperation(value = "添加购物车", notes = "添加购物车", httpMethod = "POST")
    @PostMapping("/add")
    public IMOOCJSONResult add(@ApiParam(name = "userId", value = "用户id", required = true)@RequestParam String userId,
                               @ApiParam(name = "shopCatBo", value = "客户端商品传过来的实体")@RequestBody ShopCatBO shopCatBO,
                               HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg("");
        }
        //TODO 前端用户在登录的情况下，添加商品到购物车，会同时在后端同步购物车到redis缓存
        //需要判断当前购物车中包含已经存在的商品，如果存在则累加购买数量
        String shopcartJson = redisOperator.get(FOODIE_SHOPCART + ":"+userId);
        List<ShopCatBO> shopCatList = null;
        if (StringUtils.isNotBlank(shopcartJson)) {
            // redis 中已经有购物车
            shopCatList = JsonUtils.jsonToList(shopcartJson, ShopCatBO.class);
            Boolean isHaving = false;
            // 判断购物车中是否存在已有商品，如果有的话counts累加
            for (ShopCatBO shopcat : shopCatList) {
                if (shopCatBO.getSpecId().equals(shopcat.getSpecId())) {
                    shopcat.setBuyCounts(shopcat.getBuyCounts() + shopCatBO.getBuyCounts());
                    isHaving = true;
                }
            }
            if (!isHaving) {
                shopCatList.add(shopCatBO);
            }
        } else {
            shopCatList = new ArrayList<>();
            shopCatList.add(shopCatBO);
        }
        redisOperator.set(FOODIE_SHOPCART + ":" + userId, JsonUtils.objectToJson(shopCatList));
        System.out.println("shopCat:" + shopCatBO);

        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "删除购物车中的商品", notes = "删除购物车中的商品", httpMethod = "POST")
    @PostMapping("/del")
    public IMOOCJSONResult add(@ApiParam(name = "userId", value = "用户id", required = true)@RequestParam String userId,
                               @ApiParam(name = "itemSpecId", value = "商品规格id")@RequestParam String itemSpecId,
                               HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(itemSpecId)) {
            return IMOOCJSONResult.errorMsg("用户没有登录");
        }
        //TODO 用户在页面删除购物车中的商品数据，如果此时用户已经登录，则需要同步删除后端购物车中的数据
        //判断redis中有没有这个用户的购物车
        String shopcartStr = redisOperator.get(FOODIE_SHOPCART + ":" + userId);
        if (StringUtils.isNotBlank(shopcartStr)) {
            List<ShopCatBO> shopCatBOList = JsonUtils.jsonToList(shopcartStr, ShopCatBO.class);
            for (ShopCatBO sc: shopCatBOList) {
                if (sc.getSpecId().equals(itemSpecId)) {
                    shopCatBOList.remove(sc);
                    redisOperator.set(FOODIE_SHOPCART +":"+ userId, JsonUtils.objectToJson(shopCatBOList));
                    break;
                }
            }
            if (shopCatBOList.size() == 0) {
                redisOperator.del(FOODIE_SHOPCART + ":" + userId);
            }
        } else {
            System.out.println("删除前端cookie里购物车的数据");
        }
        return IMOOCJSONResult.ok();
    }
}
