package com.imooc.pojo.Bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 用户新增或修改地址的BO
 * Created by guoming.zhang on 2021/3/8.
 */
@ApiModel(value = "用户地址", description = "用户传过来的地址实体")
@Getter
@Setter
public class UserAddressBO {
    private String addressId;

    private String userId;

    @ApiModelProperty(name = "receiver", notes = "收货人", required = true)
    @NotBlank(message = "收货人不能为空")
    @Length(message = "收货人姓名不能超过12位", max = 12)
    private String receiver;

    @ApiModelProperty(name = "province", notes = "收货人所在省", required = true)
    @NotBlank(message = "收货人所在省不能为空")
    private String province;

    @ApiModelProperty(name = "province", notes = "收货人所在市", required = true)
    @NotBlank(message = "收货人所在市不能为空")
    private String city;

    @ApiModelProperty(name = "province", notes = "收货人所在区", required = true)
    @NotBlank(message = "收货人所在区不能为空")
    private String district;

    @ApiModelProperty(name = "province", notes = "收货人详细地址", required = true)
    @NotBlank(message = "收货人详细地址不能为空")
    private String detail;

    @ApiModelProperty(name = "mobile", notes = "收货人手机号", required = true)
    @NotBlank(message = "收货人手机号不能为空")
    @Length(message = "收货人手机号不能超过11位", max = 11)
    @Pattern(message = "手机号的正则验证", regexp = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$")
    private String mobile;
}
