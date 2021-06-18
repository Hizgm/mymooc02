package com.imooc.service.impl;

import com.github.pagehelper.PageInfo;
import com.imooc.utils.PagedGridResult;

import java.util.List;

/**
 * Created by guoming.zhang on 2021/4/2.
 */
public class BaseService {
    public PagedGridResult setterPagedGrid(List<?> list, Integer page) {
        PageInfo<?> pageList = new PageInfo<>(list);
        PagedGridResult grid = new PagedGridResult();
        grid.setPage(page);
        grid.setRows(list);
        grid.setRecords(pageList.getTotal());
        grid.setTotal(pageList.getPages());
        return grid;
    }
}
