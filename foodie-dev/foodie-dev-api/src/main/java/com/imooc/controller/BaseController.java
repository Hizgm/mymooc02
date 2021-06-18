package com.imooc.controller;

import com.imooc.pojo.Orders;
import com.imooc.service.center.MyOrdersService;
import com.imooc.utils.IMOOCJSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import java.io.File;

/**
 * Created by guoming.zhang on 2021/2/25.
 */
@Controller
public class BaseController {

    @Autowired
    private MyOrdersService myOrdersService;

    public static final String FOODIE_SHOPCART = "shopcart";
    public static final Integer COMMON_PAGE_SIZE = 10;
    public static final Integer PAGE_SIZE = 20;

    // 支付中心的调用地址
    public static final String paymentUrl = "http://payment.t.mukewang.com/foodie-payment/payment/createMerchantOrder";// produce

    //微信支付成功 -> 支付中心 -> 天天吃货平台
    public static final String payReturnUrl = "http://localhost:8088/orders/notifyMerchantOrderPaid";

    //用户上传头像的位置
    public static final String IMAGE_USER_FACE_LOCATION = "C:" + File.separator + "Users" + File.separator + "22870" + File.separator
            + "IdeaProjects" + File.separator + "foodie-dev" + File.separator + "image";

    /**
     * 用于验证用户和订单是否有关联关系，避免非法用户调用
     * @param userId
     * @param orderId
     * @return
     */
    public IMOOCJSONResult checkUserOrder(String userId, String orderId) {
        Orders orders = myOrdersService.queryMyOrder(orderId, userId);
        if (orders == null) {
            return IMOOCJSONResult.errorMsg("订单不存在");
        }
        return IMOOCJSONResult.ok(orders);
    }
}
