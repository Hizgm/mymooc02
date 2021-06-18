package com.imooc.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.enums.CommentLevel;
import com.imooc.enums.YesOrNo;
import com.imooc.mapper.*;
import com.imooc.pojo.*;
import com.imooc.pojo.vo.CommentLevelCountsVO;
import com.imooc.pojo.vo.ItemCommentVO;
import com.imooc.pojo.vo.SearchItemsVO;
import com.imooc.pojo.vo.ShopCartItemsVO;
import com.imooc.service.ItemService;
import com.imooc.utils.DesensitizationUtil;
import com.imooc.utils.PagedGridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * Created by guoming.zhang on 2021/2/25.
 */
@Service
public class ItemServiceImpl implements ItemService{

    @Autowired
    private ItemsMapper itemsMapper;

    @Autowired
    private ItemsImgMapper itemsImgMapper;

    @Autowired
    private ItemsSpecMapper itemsSpecMapper;

    @Autowired
    private ItemsParamMapper itemsParamMapper;

    @Autowired
    private ItemsCommentsMapper itemsCommentsMapper;

    @Autowired
    private ItemsMapperCustom itemsMapperCustom;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Items queryItemById(String itemId) {
        return itemsMapper.selectByPrimaryKey(itemId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsImg> queryItemImgList(String itemId) {
        Example example = new Example(ItemsImg.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId", itemId);
        return itemsImgMapper.selectByExample(example);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsSpec> queryItemSpecList(String itemId) {
        Example example = new Example(ItemsSpec.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId", itemId);
        return itemsSpecMapper.selectByExample(example);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ItemsParam queryItemParm(String itemId) {
        Example example = new Example(ItemsParam.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId", itemId);
        return itemsParamMapper.selectOneByExample(example);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public CommentLevelCountsVO queryCommentCounts(String itemId) {
        Integer goodCounts = getCommentCounts(itemId, CommentLevel.GOOD.getType());
        Integer normalCounts = getCommentCounts(itemId, CommentLevel.NORMAL.getType());
        Integer badCounts = getCommentCounts(itemId, CommentLevel.BAD.getType());
        Integer totalCounts = goodCounts + normalCounts + badCounts;

        CommentLevelCountsVO countsVO = new CommentLevelCountsVO();
        countsVO.setTotalCounts(totalCounts);
        countsVO.setGoodCounts(goodCounts);
        countsVO.setNormalCounts(normalCounts);
        countsVO.setBadCounts(badCounts);
        return countsVO;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryPagedComments(String itemId, Integer level, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("itemId", itemId);
        map.put("level", level);

        // mybatis-pagehelper 原理是拦截sql
        PageHelper.startPage(page, pageSize);
        List<ItemCommentVO> commentsVO = itemsMapperCustom.queryItemComments(map);
        for (ItemCommentVO comment: commentsVO) {
            comment.setNickname(DesensitizationUtil.commonDisplay(comment.getNickname()));
        }

        return setterPagedGrid(commentsVO, page);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult querySearchItems(String keywords, String sort, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("keywords", keywords);
        map.put("sort", sort);

        // mybatis-pagehelper 原理是拦截sql
        PageHelper.startPage(page, pageSize);
        List<SearchItemsVO> list = itemsMapperCustom.searchItems(map);
        return setterPagedGrid(list, page);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult querySearchItemsByThirdCat(Integer catId, String sort, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("catId", catId);
        map.put("sort", sort);
        // mybatis-pagehelper 原理是拦截sql
        PageHelper.startPage(page, pageSize);
        List<SearchItemsVO> list = itemsMapperCustom.searchItemsByThirdCat(map);
        return setterPagedGrid(list, page);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ShopCartItemsVO> queryItemsBySpecIds(String specId) {
        String ids[] = specId.split(",");
        List<String> list = new ArrayList<>();
        Collections.addAll(list, ids);
        return itemsMapperCustom.queryItemsBySpecIds(list);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ItemsSpec queryItemsSpecById(String specId) {
        return itemsSpecMapper.selectByPrimaryKey(specId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ItemsImg queryItemsImgById(String itemId) {
        ItemsImg itemsImg = new ItemsImg();
        itemsImg.setItemId(itemId);
        itemsImg.setIsMain(YesOrNo.YES.type);
        return itemsImgMapper.selectOne(itemsImg);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void decreaseItemSpecStock(String specId, int buyCounts) {
        // 在方法上添加关键字 synchronized  不推荐使用，集群下无用，性能低下
        // 锁数据库： 不推荐，导致数据库性能低下
        // 分布式锁：zookeeper redis

        // lockUtil.getLock(); 加锁
        //1.查询库存
//        int stock = 2;
        //2.判断库存，是否能够减少到0以下
//        if (stock - buyCounts < 0) {
            //提示用户库存不够
//        }
        // lockeUtil.unLock(); 解锁
        int result = itemsMapperCustom.decreaseItemSpecStock(specId, buyCounts);
        if (result != 1) {
            throw new RuntimeException("订单创建失败，原因：库存不足");
        }
    }

    public Integer getCommentCounts(String itemId, Integer level) {
        ItemsComments condition = new ItemsComments();
        condition.setItemId(itemId);
        if (level != null) {
            condition.setCommentLevel(level);
        }
        return itemsCommentsMapper.selectCount(condition);
    }

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
