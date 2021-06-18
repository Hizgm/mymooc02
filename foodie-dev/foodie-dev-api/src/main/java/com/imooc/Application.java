package com.imooc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * Created by guoming.zhang on 2021/1/5.
 */
@SpringBootApplication
//扫描mybatis 通用mapper所在的包
@MapperScan(basePackages = "com.imooc.mapper")
//(为什么不使用该注解，事务依然有效，那是因为@SpringBootApplication已经帮你自动装配)
//@EnableTransactionManagement
//扫描所有包以及相关组件包
@ComponentScan(basePackages = {"com.imooc", "org.n3r.idworker"})
@EnableScheduling//开启定时任务
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
