package com.imooc.service;

import com.imooc.pojo.Stu;

/**
 * Created by guoming.zhang on 2021/1/6.
 */
public interface StuService {
    Stu getStuInfo(int id);

    void saveStu();

    void updateStu(int id);

    void deleteStu(int id);
}
