package com.imooc.service;

import com.imooc.pojo.Bo.UserBo;
import com.imooc.pojo.Users;

/**
 * Created by guoming.zhang on 2021/1/7.
 */
public interface UserService {

    /**
     * 判断用户名是否存在
     * @return boolean
     */
    boolean queryUserNameIsExist(String username);

    /**
     * 创建用户
     * @return Users
     */
    Users createUser(UserBo userBo);

    /**
     *  查询用户名密码是否存在，验证登录成功
     * @param username
     * @param password
     * @return Users
     */
    Users queryUserForLogin(String username, String password);
}
