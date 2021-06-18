package com.imooc.service.center;

import com.imooc.pojo.Orders;
import com.imooc.pojo.vo.OrderStatusCountsVO;
import com.imooc.utils.PagedGridResult;


/**
 * Created by guoming.zhang on 2021/3/30.
 */
public interface MyOrdersService {
    /**
     * 查询我的订单
     * @param userId
     * @param orderStatus
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult queryMyOrders(String userId, Integer orderStatus, Integer page, Integer pageSize);

    /**
     * 订单状态 --> 商家发货
     * @param orderId
     */
    void updateDeliverOrderStatus(String orderId);

    /**
     * 查询我的订单
     * @param orderId
     * @param userId
     * @return
     */
    Orders queryMyOrder(String orderId, String userId);

    /**
     * 修改订单状态 -> 确认收货
     * @param orderId
     */
    boolean updateReceiveOrderStatus(String orderId);

    /**
     * 删除订单
     * @param userId
     * @param orderId
     * @return
     */
    boolean deleteOrder(String userId, String orderId);

    /**
     *  查询用户订单数
     * @param userId
     * @return
     */
    OrderStatusCountsVO getOrderStatusCounts(String userId);

    PagedGridResult getOrderTrend(String userId, Integer page, Integer pageSize);
}
