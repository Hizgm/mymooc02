package com.imooc.service.impl;

import com.imooc.enums.Sex;
import com.imooc.mapper.UsersMapper;
import com.imooc.pojo.Bo.UserBo;
import com.imooc.pojo.Users;
import com.imooc.service.UserService;
import com.imooc.utils.DateUtil;
import com.imooc.utils.MD5Utils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

/**
 * Created by guoming.zhang on 2021/1/7.
 */
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private Sid sid;

    private static final String USER_FACE = "http://122.152.205.72:88/group1/M00/00/05/CpoxxFw_8_qAIlFXAAAcIhVPdSg994.png";

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public boolean queryUserNameIsExist(String username) {
        Example useExample = new Example(Users.class);
        Example.Criteria userCriteria = useExample.createCriteria();
        userCriteria.andEqualTo("username", username);
        Users result = usersMapper.selectOneByExample(useExample);
        return result == null ? false : true;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users createUser(UserBo userBo) {
        String userId = sid.nextShort();
        Users user = new Users();
        user.setId(userId);
        user.setUsername(userBo.getUsername());
        try {
            user.setPassword(MD5Utils.getMD5Str(userBo.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        user.setNickname(userBo.getUsername());
        //默认头像
        user.setFace(USER_FACE);
        //默认生日
        user.setBirthday(DateUtil.stringToDate("1900-01-01"));
        //默认性别为 保密
        user.setSex(Sex.secret.type);
        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());

        usersMapper.insert(user);

        return user;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserForLogin(String username, String password) {
        //通过生成example实例对象，验证用户名密码是否存在
        Example useExample = new Example(Users.class);
        Example.Criteria userCriteria = useExample.createCriteria();
        userCriteria.andEqualTo("username", username);
        userCriteria.andEqualTo("password", password);

        Users users = usersMapper.selectOneByExample(useExample);

        return users;
    }
}
