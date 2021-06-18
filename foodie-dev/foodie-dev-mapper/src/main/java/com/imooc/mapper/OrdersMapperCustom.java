package com.imooc.mapper;

import com.imooc.pojo.OrderStatus;
import com.imooc.pojo.vo.MyOrdersVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by guoming.zhang on 2021/3/29.
 */
public interface OrdersMapperCustom {
    List<MyOrdersVO> queryMyOrders(@Param(value = "paramsMap") Map<String, Object> map);

    int getMyOrderStatusCounts(@Param(value = "paramsMap") Map<String, Object> map);

    List<OrderStatus> getMyOrderTrend(@Param(value = "paramsMap") Map<String, Object> map);
}
