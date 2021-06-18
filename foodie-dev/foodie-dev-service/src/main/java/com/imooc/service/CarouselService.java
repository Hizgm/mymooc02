package com.imooc.service;

import com.imooc.pojo.Carousel;

import java.util.List;

/**
 * Created by guoming.zhang on 2021/2/23.
 */
public interface CarouselService {
    /**
     *  查询所有轮播图列表
     * @param isShow
     * @return
     */
    List<Carousel> queryAll(Integer isShow);

}
