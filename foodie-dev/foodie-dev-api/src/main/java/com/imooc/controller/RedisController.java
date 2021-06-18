package com.imooc.controller;

import com.imooc.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Created by guoming.zhang on 2021/5/24.
 */
@ApiIgnore
@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    private RedisOperator redisOperator;

    @GetMapping("/set")
    public Object setValue(String key, String value) {
        redisOperator.set(key, value);
        return "OK";
    }

    @GetMapping("/get")
    public Object getValue(String key) {
        return redisOperator.get(key);
    }

    @GetMapping("/delete")
    public Object deleteValue(String key) {
        redisOperator.del(key);
        return "OK";
    }
}
