package com.imooc.pojo.Bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;


/**
 * Created by guoming.zhang on 2021/2/2.
 */
@ApiModel(value = "用户传输对象Bo", description = "从客户端，由用户传入的数据封装在此entity中")
@Getter
@Setter
public class UserBo {

    @ApiModelProperty(value = "用户名", name = "username", example = "imooc", notes = "用户名不能为空", required = true)
    @NotBlank(message = "用户名不能为空")
    private String username;

    @ApiModelProperty(value = "密码", name = "password", example = "123456", notes = "密码不能为空,密码长度不能少于6位数", required = true)
    @NotBlank(message = "密码不能为空")
    @Length(message = "密码长度不能少于6位数", min = 6)
    private String password;

    @ApiModelProperty(value = "确认密码", name = "confirmPassword", example = "123456", notes = "密码不能为空,密码长度不能少于6位数")
//    @NotBlank(message = "密码不能为空")
//    @Length(message = "密码长度不能少于6位数", min = 6)
    private String confirmPassword;
}
