package com.imooc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Created by guoming.zhang on 2021/1/5.
 */
@ApiIgnore
@RestController
public class HelloController {

    final static Logger logger = LoggerFactory.getLogger(HelloController.class);

    @GetMapping("/hello")
    public Object hello() {
        logger.debug("hello debug");
        logger.info("hello info ");
        logger.warn("hello warn");
        logger.error("hello error");
        return "hello world";
    }
}
