package com.imooc.controller;

import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.PayMethod;
import com.imooc.pojo.Bo.ShopCatBO;
import com.imooc.pojo.Bo.SubmitOrderBO;
import com.imooc.pojo.OrderStatus;
import com.imooc.pojo.vo.MerchantOrdersVO;
import com.imooc.pojo.vo.OrderVO;
import com.imooc.service.OrderService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by guoming.zhang on 2021/3/10.
 */
@Api(value = "订单相关", tags = {"订单相关的接口"})
@RequestMapping("/orders")
@RestController
public class OrdersController extends BaseController{

    @Autowired
    private OrderService orderService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisOperator redisOperator;

    @ApiOperation(value = "创建订单", notes = "创建订单方法", httpMethod = "POST")
    @PostMapping("/create")
    public IMOOCJSONResult create(@RequestBody SubmitOrderBO submitOrderBO,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {
        Integer payType = submitOrderBO.getPayMethod();
        if (payType != PayMethod.WENXIN.type && payType != PayMethod.ZHIFUBAO.type ) {
            return IMOOCJSONResult.errorMsg("支付方式不支持");
        }
        System.out.println(submitOrderBO.toString());

        String shopcartJson = redisOperator.get(FOODIE_SHOPCART +":"+ submitOrderBO.getUserId());
        if (StringUtils.isBlank(shopcartJson)) {
            return IMOOCJSONResult.errorMsg("购物车数据不正确");
        }
        List<ShopCatBO> shopcartBOList = JsonUtils.jsonToList(shopcartJson, ShopCatBO.class);

        //1.创建订单
        OrderVO order = orderService.createOrder(shopcartBOList, submitOrderBO);
        //2.创建订单以后，移除购物车中已结算的商品
        shopcartBOList.removeAll(order.getShopCatBOList());
        redisOperator.set(FOODIE_SHOPCART+":"+submitOrderBO.getUserId(), JsonUtils.objectToJson(shopcartBOList));
        //TODO 整合redis之后，完善购物车中的已结算商品清除，并且同步到前端的cookie
        CookieUtils.setCookie(request, response, FOODIE_SHOPCART, JsonUtils.objectToJson(shopcartBOList), true);
        //3.向支付中心发送当前订单，用于保存支付中心的订单数据
        MerchantOrdersVO merchantOrdersVO = order.getMerchantOrdersVO();
        merchantOrdersVO.setReturnUrl(payReturnUrl);
        //设置headers
        HttpHeaders headers = new HttpHeaders();
        //以json格式传入参数
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.add("immoocUserId", "imooc");
        headers.add("password", "imooc");
        HttpEntity<MerchantOrdersVO> entity = new HttpEntity<>(merchantOrdersVO, headers);

//        ResponseEntity<IMOOCJSONResult> responseEntity = restTemplate.postForEntity(paymentUrl, entity, IMOOCJSONResult.class);
//        IMOOCJSONResult paymentResult = responseEntity.getBody();

//        if (paymentResult.getStatus() != 200) {
//            return IMOOCJSONResult.errorMsg("返回状态码:" +paymentResult.getStatus() + ",支付中心订单创建失败， 请联系管理员");
//        }
        return IMOOCJSONResult.ok(order.getOrderId());
    }

    @PostMapping("notifyMerchantOrderPaid")
    public Integer notifyMerchantOrderPaid(String merchantOrderId) {
        orderService.updateOrderSatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER.type);
        return HttpStatus.OK.value();
    }

    /**
     * 提供给大家查询的方法，用于查询订单信息
     * @param orderId
     * @return
     */
    @PostMapping("getPaidOrderInfo")
    public IMOOCJSONResult getPaidOrderInfo(String orderId) {

        OrderStatus orderStatus = orderService.queryOrderStatusInfo(orderId);
        return IMOOCJSONResult.ok(orderStatus);
    }


}
