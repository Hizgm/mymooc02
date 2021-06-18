package com.imooc.controller;

import com.imooc.pojo.Bo.ShopCatBO;
import com.imooc.pojo.Bo.UserBo;
import com.imooc.pojo.Users;
import com.imooc.service.UserService;
import com.imooc.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by guoming.zhang on 2021/1/8.
 */
@Api(value = "注册登录", tags = {"用于注册登录的相关接口"})
@RestController
@RequestMapping("/passport")
public class PassportController extends BaseController{

    @Autowired
    private UserService userService;

    @Autowired
    private RedisOperator redisOperator;

    @ApiOperation(value = "用户名是否存在", notes = "用户名是否存在", httpMethod = "GET")
    @GetMapping("/usernameIsExist")
    public IMOOCJSONResult usernameIsExist(@RequestParam(value = "username", required = false) String username) {//requestParam 添加参数
        // 1.判断用户名不能为空
        if(StringUtils.isBlank(username)) {
            return IMOOCJSONResult.errorMsg("用户名不为空");
        }
        // 2.查找注册的用户名是否存在
        boolean isExist = userService.queryUserNameIsExist(username);
        if (isExist) {
            return IMOOCJSONResult.errorMsg("用户名已存在");
        }

        // 3.请求成功，用户名没有重复
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户的注册", notes = "用户的注册", httpMethod = "POST")
    @PostMapping("/regist")
    public IMOOCJSONResult regist(@RequestBody @Valid UserBo userBo,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {//request body 请求的是一个对象
        String username = userBo.getUsername();
        String password = userBo.getPassword();
        String confirmPassword = userBo.getConfirmPassword();
        //0 判断用户名和密码必须不为空
        if (StringUtils.isBlank(confirmPassword)) {
            return IMOOCJSONResult.errorMsg("用户名或密码不能为空");
        }
        //1.查询用户名是否存在
        if (userService.queryUserNameIsExist(username)) {
            return IMOOCJSONResult.errorMsg("用户名已存在");
        }
        //2.密码长度不能少于6位
        if (confirmPassword.length() < 6) {
            return IMOOCJSONResult.errorMsg("密码长度不能少于6位数");
        }
        //3.判断两次密码是否一致
        if (!password.equals(confirmPassword)) {
            return IMOOCJSONResult.errorMsg("输入的两次密码不一致");
        }
        //4.实现注册
        Users user = userService.createUser(userBo);
        // 对cookie 加密
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(user), true);
        return IMOOCJSONResult.ok(user);
    }

    @ApiOperation(value = "用户登录", notes = "用户登录", httpMethod = "POST")
    @PostMapping("/login")
    public IMOOCJSONResult login(@RequestBody @Valid UserBo userBo,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        String username = userBo.getUsername();
        String password = userBo.getPassword();
        //1. 实现登录
        Users users = userService.queryUserForLogin(username, MD5Utils.getMD5Str(password));

        if (users == null) {
            return IMOOCJSONResult.errorMsg("用户名或密码不正确");
        }

        Users usersResult = setNullProperty(users);
        // 对cookie 加密
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(usersResult), true);
        // TODO 生成用户token, 存入redis会话
        // TODO 同步购物车数据
        String shopcartCookie = CookieUtils.getCookieValue(request, FOODIE_SHOPCART, true);
        synchShopcartData(usersResult, request, response);
        return IMOOCJSONResult.ok(usersResult);
    }

    /**
     * 注册登录成功后，同步cookie和redis中的购物车数据
     */
    private void synchShopcartData(Users user, HttpServletRequest request, HttpServletResponse response) {
        /**
         * 1. redis中无数据，如果cookie中的购物车为空，那么这个时候不做任何处理
         *                 如果cookie中的购物车不为空，此时直接放入redis中
         * 2. redis中有数据，如果cookie中的购物车为空，那么直接把redis的购物车覆盖本地cookie
         *                 如果cookie中的购物车不为空，
         *                      如果cookie中的某个商品在redis中存在，
         *                      则以cookie为主，删除redis中的，
         *                      把cookie中的商品直接覆盖redis中（参考京东）
         * 3. 同步到redis中去了以后，覆盖本地cookie购物车的数据，保证本地购物车的数据是同步最新的
         */
        //从redis中获取购物车
        String shopcartRedis = redisOperator.get(FOODIE_SHOPCART+":"+user.getId());
        //从cookie中获取购物车
        String shopcartCookie = CookieUtils.getCookieValue(request, FOODIE_SHOPCART, true);
        if (StringUtils.isBlank(shopcartRedis)) {
            if (StringUtils.isNotBlank(shopcartCookie)) {
                redisOperator.set(FOODIE_SHOPCART+":"+user.getId(), shopcartCookie);
            }
        } else {
            //合并cookie和redis中的数据
            if (StringUtils.isNotBlank(shopcartCookie)) {
                /**
                 * 1. 已经存在的，把cookie中对应的数量，覆盖redis
                 * 2. 该项目商品标记为待删除，统一放入一个待删除的list
                 * 3. 从cookie中清理所有待删除的list
                 * 4. 合并redis和cookie中的数据
                 * 5. 更新redis和cookie中
                 */
                List<ShopCatBO> shopCatBOListRedis = JsonUtils.jsonToList(shopcartRedis, ShopCatBO.class);
                List<ShopCatBO> shopCatBOListCookie = JsonUtils.jsonToList(shopcartCookie, ShopCatBO.class);
                List<ShopCatBO> pendingRemoveList = new ArrayList<>();
                for (ShopCatBO shopCatBORedis: shopCatBOListRedis) {
                    String specIdRedis = shopCatBORedis.getSpecId();
                    for (ShopCatBO shopCatBOCookie: shopCatBOListCookie) {
                        String specIdCookie = shopCatBOCookie.getSpecId();
                        if (specIdRedis.equals(specIdCookie)) {
                            //把cookie的购物车商品数量覆盖到redis中
                            shopCatBORedis.setBuyCounts(shopCatBOCookie.getBuyCounts());
                            pendingRemoveList.add(shopCatBOCookie);
                        }
                    }
                }
                shopCatBOListCookie.removeAll(pendingRemoveList);
                // 要合并redis和cookie中的数据
                shopCatBOListRedis.addAll(shopCatBOListCookie);
                redisOperator.set(FOODIE_SHOPCART+":"+user.getId(), JsonUtils.objectToJson(shopCatBOListRedis));
                CookieUtils.setCookie(request, response, FOODIE_SHOPCART, JsonUtils.objectToJson(shopCatBOListRedis), true);
            } else {
                CookieUtils.setCookie(request, response, FOODIE_SHOPCART, shopcartRedis, true);
            }
        }
    }

    @ApiOperation(value = "用户退出", notes = "用户退出", httpMethod = "POST")
    @PostMapping("/logout")
    public IMOOCJSONResult logout(@RequestParam String userId,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {
        //清除用户相关信息的cookie
        CookieUtils.deleteCookie(request, response, "user");

        //TODO 用户退出登录，需要清空cookie中购物车数据
        CookieUtils.deleteCookie(request, response, FOODIE_SHOPCART);
        //TODO 分布式会话中需要清除用户数据

        return IMOOCJSONResult.ok();
    }

    private Users setNullProperty(Users userResult) {
        userResult.setPassword(null);
        userResult.setMobile(null);
        userResult.setEmail(null);
        userResult.setCreatedTime(null);
        userResult.setUpdatedTime(null);
        userResult.setBirthday(null);
        return userResult;
    }
}
