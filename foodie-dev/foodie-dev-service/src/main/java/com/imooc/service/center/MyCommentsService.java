package com.imooc.service.center;

import com.imooc.pojo.Bo.center.OrderItemsCommentBO;
import com.imooc.pojo.OrderItems;
import com.imooc.utils.PagedGridResult;

import java.util.List;

/**
 * Created by guoming.zhang on 2021/4/1.
 */
public interface MyCommentsService {

    /**
     * 根据订单id查询评价信息
     * @param orderId
     * @return
     */
    List<OrderItems> queryPendingComment(String orderId);

    /**
     * 保存评论
     * @param orderId
     * @param userId
     * @param comments
     */
    void saveComments(String orderId, String userId, List<OrderItemsCommentBO> comments);

    /**
     * 查询我的评价
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult queryMyComments(String userId, Integer page, Integer pageSize);
}
