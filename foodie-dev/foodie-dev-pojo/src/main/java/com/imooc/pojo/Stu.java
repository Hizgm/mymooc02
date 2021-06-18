package com.imooc.pojo;

import javax.persistence.Column;
import javax.persistence.Id;

public class Stu {
    /**
     * 学生主健id
     */
    @Id
    @Column(name = "stu_id")
    private Integer stuId;

    /**
     * 学生姓名
     */
    private String name;

    /**
     * 学生年龄
     */
    private Integer age;

    /**
     * 获取学生主健id
     *
     * @return stu_id - 学生主健id
     */
    public Integer getStuId() {
        return stuId;
    }

    /**
     * 设置学生主健id
     *
     * @param stuId 学生主健id
     */
    public void setStuId(Integer stuId) {
        this.stuId = stuId;
    }

    /**
     * 获取学生姓名
     *
     * @return name - 学生姓名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置学生姓名
     *
     * @param name 学生姓名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取学生年龄
     *
     * @return age - 学生年龄
     */
    public Integer getAge() {
        return age;
    }

    /**
     * 设置学生年龄
     *
     * @param age 学生年龄
     */
    public void setAge(Integer age) {
        this.age = age;
    }
}