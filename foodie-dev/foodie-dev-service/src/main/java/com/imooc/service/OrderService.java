package com.imooc.service;

import com.imooc.pojo.Bo.ShopCatBO;
import com.imooc.pojo.Bo.SubmitOrderBO;
import com.imooc.pojo.OrderStatus;
import com.imooc.pojo.vo.OrderVO;

import java.util.List;

/**
 * Created by guoming.zhang on 2021/3/11.
 */
public interface OrderService {
    /**
     * 创建订单
     * @param submitOrderBO
     */
    OrderVO createOrder(List<ShopCatBO> shopcartBOList, SubmitOrderBO submitOrderBO);

    /**
     * 修改订单状态
     * @param orderId
     * @param orderStatus
     */
    void updateOrderSatus(String orderId, Integer orderStatus);

    /**
     * 查询订单状态
     * @param orderId
     * @return
     */
    OrderStatus queryOrderStatusInfo(String orderId);

    /**
     * 关闭超时未支付的订单
     */
    void closeOrder();
}
