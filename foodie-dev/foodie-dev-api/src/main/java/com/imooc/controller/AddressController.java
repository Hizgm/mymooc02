package com.imooc.controller;

import com.imooc.pojo.Bo.UserAddressBO;
import com.imooc.pojo.UserAddress;
import com.imooc.service.AddressService;
import com.imooc.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 有关地址的接口
 * Created by guoming.zhang on 2021/3/8.
 */
@Api(value = "有关地址的接口", tags = "地址的增删改查")
@RequestMapping("/address")
@RestController
public class AddressController {

    /**
     * 用户在确认订单页面，可以针对收获地址做如下操作:
     * 1.查询用户的所有收货地址列表
     * 2.新增收货地址
     * 3.删除收货地址
     * 4.修改收货地址
     * 5.设置默认地址
     */
    @Autowired
    private AddressService addressService;

    @ApiOperation(value = "查询用户的所有收货地址列表", notes = "查询用户的所有收货地址列表", httpMethod = "POST")
    @PostMapping("/list")
    public IMOOCJSONResult list(@ApiParam(name = "userId", value = "用户id", required = true)@RequestParam String userId) {
        if (StringUtils.isBlank(userId)) {
            IMOOCJSONResult.errorMsg("用户id不为空");
        }
        List<UserAddress> userAddresses = addressService.queryAll(userId);
        return IMOOCJSONResult.ok(userAddresses);
    }

    @ApiOperation(value = "新增收货地址", notes = "新增收获地址", httpMethod = "POST")
    @PostMapping("/add")
    public IMOOCJSONResult add(@ApiParam(name = "userAddressBo", value = "用户传输过来的地址实体")@Valid@RequestBody UserAddressBO userAddressBO) {

        addressService.addNewUserAddress(userAddressBO);
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "修改收货地址", notes = "修改收获地址", httpMethod = "POST")
    @PostMapping("/update")
    public IMOOCJSONResult update(@ApiParam(name = "userAddressBo", value = "用户传输过来的地址实体")@Valid@RequestBody UserAddressBO userAddressBO) {
        if (StringUtils.isBlank(userAddressBO.getAddressId())) {
            return IMOOCJSONResult.errorMsg("修改地址的addressId不能为空");
        }
        addressService.updateUserAddress(userAddressBO);
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "删除收货地址", notes = "删除收货地址", httpMethod = "POST")
    @PostMapping("/delete")
    public IMOOCJSONResult update(
            @ApiParam(name = "userId", value = "用户ID")@RequestParam String userId,
            @ApiParam(name = "addressId", value = "地址ID")@RequestParam String addressId) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)) {
            return IMOOCJSONResult.errorMsg("修改地址的userId或addressId不能为空");
        }
        addressService.deleteUserAddress(userId, addressId);
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "设置收货默认地址", notes = "设置收货默认地址", httpMethod = "POST")
    @PostMapping("/setDefalut")
    public IMOOCJSONResult setDefalut(
            @ApiParam(name = "userId", value = "用户ID")@RequestParam String userId,
            @ApiParam(name = "addressId", value = "地址ID")@RequestParam String addressId) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)) {
            return IMOOCJSONResult.errorMsg("地址的userId或addressId不能为空");
        }
        addressService.updateDefaultAddress(userId, addressId);
        return IMOOCJSONResult.ok();
    }

}
