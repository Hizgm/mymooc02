package com.imooc;


import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Created by guoming.zhang on 2021/4/28.
 */
public class WarStarterApplication extends SpringBootServletInitializer{
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        //指向Application这个springboot启动类
        return builder.sources(Application.class);
    }
}
