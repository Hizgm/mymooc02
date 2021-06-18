package com.imooc.controller.center;

import com.imooc.controller.BaseController;
import com.imooc.pojo.Orders;
import com.imooc.pojo.vo.OrderStatusCountsVO;
import com.imooc.service.center.MyOrdersService;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by guoming.zhang on 2021/3/30.
 */
@Api(value = "我的订单", tags = {"我的订单相关接口"})
@RequestMapping("/myorders")
@RestController
public class MyOrderController extends BaseController{

    @Autowired
    private MyOrdersService myOrdersService;


    @ApiOperation(value = "查询订单列表", notes = "查询订单列表", httpMethod = "POST")
    @PostMapping("/query")
    public IMOOCJSONResult  query(
            @ApiParam(name = "userId", value = "用户", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderStatus", value = "订单状态")
            @RequestParam Integer orderStatus,
            @ApiParam(name = "page", value = "查询下一页第几页")
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页每一页显示的条数")
            @RequestParam Integer pageSize) {
        if (StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }
        PagedGridResult pagedGridResult = myOrdersService.queryMyOrders(userId, orderStatus, page, pageSize);
        return IMOOCJSONResult.ok(pagedGridResult);
    }

    // 商家发货没有后端，所以这个接口仅仅只是用于模拟
    @ApiOperation(value="商家发货", notes="商家发货", httpMethod = "GET")
    @GetMapping("/deliver")
    public IMOOCJSONResult deliver(
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @RequestParam String orderId) throws Exception {

        if (StringUtils.isBlank(orderId)) {
            return IMOOCJSONResult.errorMsg("订单ID不能为空");
        }
        myOrdersService.updateDeliverOrderStatus(orderId);
        return IMOOCJSONResult.ok();
    }


    @ApiOperation(value="用户确认收获", notes="用户确认收获", httpMethod = "POST")
    @PostMapping("/confirmReceive")
    public IMOOCJSONResult deliver(
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @RequestParam String orderId,
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId) throws Exception {

        if (StringUtils.isBlank(orderId)) {
            return IMOOCJSONResult.errorMsg("订单ID不能为空");
        }
        if (StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg("用户ID不能为空");
        }
        IMOOCJSONResult checkResult = checkUserOrder(userId, orderId);
        if (checkResult.getStatus() != HttpStatus.OK.value()) {
            return checkResult;
        }
        boolean res = myOrdersService.updateReceiveOrderStatus(orderId);
        if (!res) {
            return IMOOCJSONResult.errorMsg("订单确认收货失败");
        }
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value="删除订单", notes="删除订单", httpMethod = "POST")
    @PostMapping("/delete")
    public IMOOCJSONResult delete(
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @RequestParam String orderId,
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId) throws Exception {

        if (StringUtils.isBlank(orderId)) {
            return IMOOCJSONResult.errorMsg("订单ID不能为空");
        }
        if (StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg("用户ID不能为空");
        }

        boolean res = myOrdersService.deleteOrder(userId, orderId);
        if (!res) {
            IMOOCJSONResult.errorMsg("删除订单失败");
        }
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "查询订单状态数", notes = "查询订单状态数", httpMethod = "POST")
    @PostMapping("/statusCounts")
    public IMOOCJSONResult  statusCounts(
            @ApiParam(name = "userId", value = "用户", required = true)
            @RequestParam String userId) {
        if (StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg(null);
        }
        OrderStatusCountsVO orderStatusCounts = myOrdersService.getOrderStatusCounts(userId);
        return IMOOCJSONResult.ok(orderStatusCounts);
    }

    @ApiOperation(value = "查询订单动向", notes = "查询订单动向", httpMethod = "POST")
    @PostMapping("/trend")
    public IMOOCJSONResult  trend(
            @ApiParam(name = "userId", value = "用户", required = true)
            @RequestParam String userId,
            @ApiParam(name = "page", value = "查询下一页第几页")
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页每一页显示的条数")
            @RequestParam Integer pageSize) {
        if (StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }
        PagedGridResult pagedGridResult = myOrdersService.getOrderTrend(userId, page, pageSize);
        return IMOOCJSONResult.ok(pagedGridResult);
    }
}
