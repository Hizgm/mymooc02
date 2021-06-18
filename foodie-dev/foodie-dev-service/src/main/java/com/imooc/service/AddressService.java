package com.imooc.service;

import com.imooc.pojo.Bo.UserAddressBO;
import com.imooc.pojo.UserAddress;

import java.util.List;

/**
 * Created by guoming.zhang on 2021/3/8.
 */
public interface AddressService {
    /**
     * 根据用户id查询所有地址列表
     * @param userId
     * @return
     */
    List<UserAddress> queryAll(String userId);

    /**
     * 添加收货地址
     * @param userAddressBO
     * @return
     */
    void addNewUserAddress(UserAddressBO userAddressBO);

    /**
     * 修改收货地址
     * @param userAddressBO
     */
    void updateUserAddress(UserAddressBO userAddressBO);

    /**
     * 删除收获地址
     * @param userId
     * @param addressId
     */
    void deleteUserAddress(String userId, String addressId);

    /**
     * 修改默认地址
     * @param userId
     * @param addressId
     */
    void updateDefaultAddress(String userId, String addressId);

    /**
     * 根据用户ID和地址ID查询UserAddress
     * @param userId
     * @param addressId
     * @return
     */
    UserAddress queryUserAddress(String userId, String addressId);
}
