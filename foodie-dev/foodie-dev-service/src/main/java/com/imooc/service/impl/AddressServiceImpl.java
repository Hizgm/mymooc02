package com.imooc.service.impl;

import com.imooc.enums.YesOrNo;
import com.imooc.mapper.UserAddressMapper;
import com.imooc.pojo.Bo.UserAddressBO;
import com.imooc.pojo.UserAddress;
import com.imooc.service.AddressService;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * Created by guoming.zhang on 2021/3/8.
 */
@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<UserAddress> queryAll(String userId) {

        //方法一：
//        Example example = new Example(UserAddress.class);
//        Example.Criteria criteria = example.createCriteria();
//        criteria.andEqualTo(userId);
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);

        return userAddressMapper.select(userAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void addNewUserAddress(UserAddressBO userAddress) {
        //1.判断当前用户是否存在地址，如果没有，则新增为默认地址
        Integer isDefault = 0;
        List<UserAddress> userAddresses = this.queryAll(userAddress.getUserId());
        if (userAddress == null || userAddresses.isEmpty() || userAddresses.size() == 0) {
            isDefault = 1;
        }
        //2.保存地址到数据库
        UserAddress address = new UserAddress();
        BeanUtils.copyProperties(userAddress, address);
        address.setId(sid.nextShort());
        address.setIsDefault(isDefault);
        address.setCreatedTime(new Date());
        address.setUpdatedTime(new Date());

        userAddressMapper.insert(address);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUserAddress(UserAddressBO userAddressBO) {
        UserAddress updateAddress = new UserAddress();
        BeanUtils.copyProperties(userAddressBO, updateAddress);
        updateAddress.setId(userAddressBO.getAddressId());
        updateAddress.setUpdatedTime(new Date());
        userAddressMapper.updateByPrimaryKeySelective(updateAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteUserAddress(String userId, String addressId) {
        UserAddress deleteAddress = new UserAddress();
        deleteAddress.setId(addressId);
        deleteAddress.setUserId(userId);
        userAddressMapper.delete(deleteAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateDefaultAddress(String userId, String addressId) {
        //1.查找默认地址，设置位不默认
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        userAddress.setIsDefault(YesOrNo.YES.type);
        UserAddress noDefaultAddress = userAddressMapper.selectOne(userAddress);
        if (noDefaultAddress != null) {
            noDefaultAddress.setIsDefault(0);
            userAddressMapper.updateByPrimaryKeySelective(noDefaultAddress);
        }
        //2.根据地址id修改为默认
        UserAddress defaultAddress = new UserAddress();
        defaultAddress.setId(addressId);
        defaultAddress.setUserId(userId);
        defaultAddress.setIsDefault(1);
        userAddressMapper.updateByPrimaryKeySelective(defaultAddress);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public UserAddress queryUserAddress(String userId, String addressId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        userAddress.setId(addressId);
        UserAddress userAddress1 = userAddressMapper.selectOne(userAddress);
        return userAddress1;
    }
}
