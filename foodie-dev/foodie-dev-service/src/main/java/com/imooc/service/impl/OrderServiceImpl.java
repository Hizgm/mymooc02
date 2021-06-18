package com.imooc.service.impl;

import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.YesOrNo;
import com.imooc.mapper.OrderItemsMapper;
import com.imooc.mapper.OrderStatusMapper;
import com.imooc.mapper.OrdersMapper;
import com.imooc.pojo.*;
import com.imooc.pojo.Bo.ShopCatBO;
import com.imooc.pojo.Bo.SubmitOrderBO;
import com.imooc.pojo.vo.MerchantOrdersVO;
import com.imooc.pojo.vo.OrderVO;
import com.imooc.service.AddressService;
import com.imooc.service.ItemService;
import com.imooc.service.OrderService;
import com.imooc.utils.DateUtil;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by guoming.zhang on 2021/3/11.
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderItemsMapper orderItemsMapper;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Autowired
    private Sid sid;

    @Autowired
    private AddressService addressService;
    
    @Autowired
    private ItemService itemService;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public OrderVO createOrder(List<ShopCatBO> shopCatBOList, SubmitOrderBO submitOrderBO) {
        String userId = submitOrderBO.getUserId();
        String addressId = submitOrderBO.getAddressId();
        String itemSpecId = submitOrderBO.getItemSpecIds();
        Integer payMethod = submitOrderBO.getPayMethod();
        String leftMsg = submitOrderBO.getLeftMsg();
        Integer postAmount = 0;//包邮费用为0
        String orderId = sid.nextShort();
        UserAddress userAddress = addressService.queryUserAddress(userId, addressId);
        //1.新订单数据保存
        Orders orders = new Orders();
        orders.setId(orderId);
        orders.setUserId(userId);
        orders.setReceiverName(userAddress.getReceiver());
        orders.setReceiverAddress(userAddress.getProvince() + " "
                + userAddress.getCity() + " "
                + userAddress.getDistrict() + " "
                + userAddress.getDetail());
        orders.setReceiverMobile(userAddress.getMobile());
        orders.setPostAmount(postAmount);
        orders.setPayMethod(payMethod);
        orders.setIsComment(YesOrNo.NO.type);
        orders.setIsDelete(YesOrNo.NO.type);
        orders.setCreatedTime(new Date());
        orders.setUpdatedTime(new Date());

        //2.循环根据itemSpecIds保存订单商品信息表
        String[] specIds = itemSpecId.split(",");
        Integer totalAmount = 0; //商品原价累计
        Integer realPayAmount = 0; //优惠后的商品实际支付价格累计
        List<ShopCatBO> toBeRemoveShopCatBO = new ArrayList<>();
        for (String specId: specIds) {
            //TODO 整合redis后，商品购买的数量重新从redis的购物车中获取
            ShopCatBO shopCatBO = getBuyCountsFromShopcart(shopCatBOList, specId);
            toBeRemoveShopCatBO.add(shopCatBO);
            System.out.println("toBeRemove:" +toBeRemoveShopCatBO);
            int buyCounts = 1;
            if (shopCatBO != null) {
                buyCounts = shopCatBO.getBuyCounts();
            }
            // 2.1 根据规格的id查询商品规格对象的具体信息，获取价格
            ItemsSpec itemsSpec = itemService.queryItemsSpecById(specId);
            totalAmount += itemsSpec.getPriceNormal() * buyCounts;
            realPayAmount += itemsSpec.getPriceDiscount() * buyCounts;
            // 2.2 根据商品id， 获得商品信息以及商品图片
            String itemId = itemsSpec.getItemId();
            Items items = itemService.queryItemById(itemId);
            ItemsImg itemsImg = itemService.queryItemsImgById(itemId);
            // 2.3 循环保存子订单数据到数据库
            OrderItems subOrderItems = new OrderItems();
            String subOrderId = sid.nextShort();
            subOrderItems.setId(subOrderId);
            subOrderItems.setOrderId(orderId);
            subOrderItems.setItemId(items.getId());
            subOrderItems.setItemName(items.getItemName());
            subOrderItems.setItemImg(itemsImg.getUrl());
            subOrderItems.setBuyCounts(buyCounts);
            subOrderItems.setItemSpecId(specId);
            subOrderItems.setItemSpecName(itemsSpec.getName());
            subOrderItems.setPrice(itemsSpec.getPriceDiscount());
            orderItemsMapper.insert(subOrderItems);
            // 2.4 在用户提交订单以后，规格表中需要扣除库存
            itemService.decreaseItemSpecStock(specId, buyCounts);
        }
        orders.setTotalAmount(totalAmount);
        orders.setRealPayAmount(realPayAmount);
        ordersMapper.insert(orders);

        //3.保存订单状态表
        OrderStatus waitPayOrderStatus = new OrderStatus();
        waitPayOrderStatus.setOrderId(orderId);
        waitPayOrderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        waitPayOrderStatus.setCreatedTime(new Date());
        orderStatusMapper.insert(waitPayOrderStatus);

        //4.构建商户订单，用于传给支付中心
        MerchantOrdersVO merchantOrdersVO = new MerchantOrdersVO();
        merchantOrdersVO.setMerchantOrderId(orderId);
        merchantOrdersVO.setMerchantUserId(userId);
        merchantOrdersVO.setAmount(realPayAmount + postAmount);
        merchantOrdersVO.setPayMethod(payMethod);
        //5.构建自定义订单VO
        OrderVO orderVO = new OrderVO();
        orderVO.setOrderId(orderId);
        orderVO.setMerchantOrdersVO(merchantOrdersVO);
        orderVO.setShopCatBOList(toBeRemoveShopCatBO);

        return orderVO;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateOrderSatus(String orderId, Integer orderStatus) {
        OrderStatus paidStatus = new OrderStatus();
        paidStatus.setOrderId(orderId);
        paidStatus.setOrderStatus(orderStatus);
        paidStatus.setPayTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(paidStatus);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public OrderStatus queryOrderStatusInfo(String orderId) {
        return orderStatusMapper.selectByPrimaryKey(orderId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void closeOrder() {
        //查询所有未付款订单，判断时间是否超时（1天），超时则关闭交易
        OrderStatus queryOrder = new OrderStatus();
        queryOrder.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        List<OrderStatus> list = orderStatusMapper.select(queryOrder);
        for (OrderStatus os: list) {
            //获得订单创建时间
            Date createdTime = os.getCreatedTime();
            //和当前时间进行对比
            int days = DateUtil.daysBetween(createdTime, new Date());
            if (days >= 1) {
                //超过一天，关闭订单
                doCloseOrder(os.getOrderId());
            }

        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void doCloseOrder(String orderId) {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setOrderStatus(OrderStatusEnum.CLOSE.type);
        orderStatusMapper.updateByPrimaryKeySelective(orderStatus);
    }

    /**
     * 从redis中的购物车里获取商品，目的：counts
     * @param shopCatBOList
     * @param specId
     * @return
     */
    private ShopCatBO getBuyCountsFromShopcart(List<ShopCatBO> shopCatBOList, String specId){
        for (ShopCatBO cart: shopCatBOList) {
            if (cart.getSpecId().equals(specId)) {
                System.out.println("specId:" + specId);
                System.out.println("cart:" + cart);
                return cart;
            }
        }
        return null;
    }

}
