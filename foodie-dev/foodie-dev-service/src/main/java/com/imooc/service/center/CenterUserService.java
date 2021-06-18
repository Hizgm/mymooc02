package com.imooc.service.center;

import com.imooc.pojo.Bo.center.CenterUserBO;
import com.imooc.pojo.Users;

/**
 * Created by guoming.zhang on 2021/3/18.
 */
public interface CenterUserService {
    /**
     * 根据用户id查询用户信息
     * @param userId
     * @return
     */
     Users queryUserInfo(String userId);

    /**
     * 修改用户信息
     * @param userId
     * @param centerUserBO
     */
     Users updateUserInfo(String userId, CenterUserBO centerUserBO);

    /**
     * 用户头像更新
     * @param userId
     * @param faceUrl
     * @return
     */
     Users updateUserFace(String userId, String faceUrl);
}
