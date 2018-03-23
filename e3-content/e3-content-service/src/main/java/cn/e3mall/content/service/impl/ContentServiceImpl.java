package cn.e3mall.content.service.impl;

import cn.e3mall.common.pojo.AdItem;
import cn.e3mall.common.pojo.DataGridResult;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.content.jedis.JedisDao;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Description: 根据categoryId查询该分类内容列表
 * @Author: Likh
 * @CreateDate: 2018/3/14 20:52
 */
@Service
public class ContentServiceImpl implements ContentService{

    @Autowired
    private TbContentMapper contentMapper;

    @Autowired
    private JedisDao jedisDao;

    //注入广告图片宽
    @Value("${WIDTH}")
    private Integer WIDTH;
    @Value("${WIDTHB}")
    private Integer WIDTHB;
    //注入广告图片高
    @Value("${HEIGHT}")
    private Integer HEIGHT;
    @Value("${HEIGHTB}")
    private Integer HEIGHTB;

    @Value("${INDEX_CACHE}")
    private String INDEX_CACHE;

    /**
     * @Author:      Likh
     * @Date:        2018/3/14 20:54
     * @Description: 根据categoryId查询该分类内容列表
     * @Param:       [categoryId, rows, page]
     * @return:      cn.e3mall.common.pojo.DataGridResult
     */
    @Override
    public DataGridResult findCategoryListByCategoryId(Long categoryId,Integer page,Integer rows) {
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        List<TbContent> contentList = contentMapper.selectByExample(example);
        PageHelper.startPage(page,rows);
        PageInfo<TbContent> contentPageInfo = new PageInfo<>(contentList);
        DataGridResult dataGridResult = new DataGridResult();
        dataGridResult.setTotal(contentPageInfo.getTotal());
        dataGridResult.setRows(contentList);
        return dataGridResult;
    }
    
    /**
     * @Author:      Likh
     * @Date:        2018/3/14 21:11 
     * @Description: 保存广告内容
     * @Param:       [content] 
     * @return:      cn.e3mall.common.pojo.E3Result
     * 添加,修改,删除广告数据时候,删除数据库数据,导致和广告缓存数据不同步.
     * 同步缓存:--- 添加,修改,删除广告数据时候,同时删除广告缓存数据.下次查询广告数据库,先查询缓存,发现缓存中已经
     * 没有数据,再次查询数据库新的数据,同时添加到缓存中.达到缓存同步的目的.
     */
    @Override
    public E3Result saveContent(TbContent content) {
        //先删除缓存
        jedisDao.hdel(INDEX_CACHE, content.getCategoryId()+"");
        Date date = new Date();
        content.setCreated(date);
        content.setUpdated(date);
        contentMapper.insert(content);
        return E3Result.ok();
    }
    /**
     * @Author:      Likh
     * @Date:        2018/3/14 23:01
     * @Description: //根据categoryId查询该分类内容列表(大广告位维护)
     * @Param:       [categoryId]
     * @return:      java.util.List<cn.e3mall.common.pojo.AdItem>
     * 流程:
     * 1,首页,其他广告数据获取先查询缓存
     * 2,如果缓存中有数据,直接返回,不再查询数据库
     * 3,如果缓存中没有数据,再次查询数据库,同时把数据放入缓存.当下一个人访问,查询就是缓存.
     */
    @Override
    public List<AdItem> findContentList(Long categoryId) {
        try {
            //查询缓存
            String adJson = jedisDao.hget(INDEX_CACHE, categoryId + "");
            //判断缓存是否存在
            if (StringUtils.isNotBlank(adJson)){
                List<AdItem> adList = JsonUtils.jsonToList(adJson, AdItem.class);
                return adList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<AdItem> adList =new ArrayList<>();
        TbContentExample contentExample = new TbContentExample();
        TbContentExample.Criteria criteria = contentExample.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        List<TbContent> list = contentMapper.selectByExample(contentExample);
        for (TbContent tbContent : list) {
            AdItem adItem = new AdItem();
            adItem.setAlt(tbContent.getSubTitle());
            adItem.setSrc(tbContent.getPic());
            adItem.setSrcB(tbContent.getPic2());
            adItem.setHref(tbContent.getUrl());

            //广告图片宽,高
            adItem.setWidth(WIDTH);
            adItem.setWidthB(WIDTHB);
            adItem.setHeight(HEIGHT);
            adItem.setHeightB(HEIGHTB);

            adList.add(adItem);
        }
        //不存在缓存，添加
        jedisDao.hset(INDEX_CACHE, categoryId+"", JsonUtils.objectToJson(adList));
        return adList;
    }
}
