package cn.e3mall.service.impl;

import cn.e3mall.common.pojo.DataGridResult;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.service.ItemService;
import cn.e3mall.service.JedisDao;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import java.util.Date;
import java.util.List;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

/**
 * @Description: 商品管理Service
 * @Author: Likh
 * @CreateDate: 2018/3/13 23:37
 */
@Service
public class ItemServiceImpl implements ItemService{
    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private TbItemDescMapper itemDescMapper;
    @Autowired
    private JedisDao jedisDao;
    // 注入jmstemplate消息发送模版对象
    @Autowired
    private JmsTemplate jmsTemplate;
    // 注入消息发送目的地
    @Autowired
    private ActiveMQTopic activeMQTopic;

    // 注入商品详情缓存唯一标识
    @Value("${ITEM_DETAIL}")
    private String ITEM_DETAIL;

    // 注入商品详情缓存过期时间
    @Value("${ITEM_DETAIL_EXPIRE_TIME}")
    private Integer ITEM_DETAIL_EXPIRE_TIME;
    /**
     * @Author:      Likh
     * @Date:        2018/3/13 23:38
     * @Description: 根据商品id查询商品信息,先查缓存，若缓存没有，去数据库查询；并把数据库的数据添加到redis缓存中
     * @Param:       [id]
     * @return:      cn.e3mall.pojo.TbItem
     */
    @Override
    public TbItem findItemById(long id) {
        try {
            String s = jedisDao.get(ITEM_DETAIL + "BASE:" + id);
            if (StringUtils.isNotBlank(s)) {
                TbItem tbItem = JsonUtils.jsonToPojo(s, TbItem.class);
                return tbItem;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        TbItem item = itemMapper.selectByPrimaryKey(id);
        //放入缓存
        jedisDao.set(ITEM_DETAIL+"BASE:"+id,JsonUtils.objectToJson(item));
        //设置过期时间
        jedisDao.expire(ITEM_DETAIL+"BASE:"+id,ITEM_DETAIL_EXPIRE_TIME);
        return item;
    }
    /**
     * @Author:      Likh
     * @Date:        2018/3/14 10:50
     * @Description: 分页查询商品列表
     * @Param:       [page, rows]
     * @return:      cn.e3mall.common.pojo.DataGridResult
     */
    @Override
    public DataGridResult findItemList(int page, int rows) {
        PageHelper.startPage(page,rows);
        TbItemExample example=new TbItemExample();
        List<TbItem> list = itemMapper.selectByExample(example);
        PageInfo<TbItem> pageInfo = new PageInfo<>(list);
        long total = pageInfo.getTotal();
        DataGridResult result = new DataGridResult();
        result.setRows(list);
        result.setTotal(total);
        return result;
    }

    /**
     * @Author:      Likh
     * @Date:        2018/3/14 15:36
     * @Description: 添加商品并发送消息给activeMQ
     * @Param:       [item, desc]
     * @return:      cn.e3mall.common.pojo.E3Result
     */
    @Override
    public E3Result addItem(TbItem item, String desc) {
        long itemId = IDUtils.genItemId();
        item.setId(itemId);
        //商品状态 1上架   2下架   3删除
        item.setStatus((byte)1);
        Date date = new Date();
        item.setCreated(date);
        item.setUpdated(date);
        itemMapper.insert(item);
        TbItemDesc itemDesc = new TbItemDesc();
        itemDesc.setItemId(itemId);
        itemDesc.setItemDesc(desc);
        itemDesc.setCreated(date);
        itemDesc.setUpdated(date);
        itemDescMapper.insert(itemDesc);
        //发送消息
        jmsTemplate.send(activeMQTopic, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {

                return session.createTextMessage(itemId+"");
            }
        });
        return E3Result.ok();
    }

    /**
     * @Author:      Likh
     * @Date:        2018/3/13 23:38
     * @Description: 根据商品id查询商品描述信息
     * @Param:       [id]
     * @return:      cn.e3mall.pojo.TbItemDesc
     */
    @Override
    public TbItemDesc findItemDescById(long id) {
        String s = jedisDao.get(ITEM_DETAIL + "DESC:" + id);
        if (StringUtils.isNotBlank(s)){
            TbItemDesc desc = JsonUtils.jsonToPojo(s, TbItemDesc.class);
            return desc;
        }
        TbItemDesc tbItemDesc = itemDescMapper.selectByPrimaryKey(id);
        //放入缓存并设置过期时间
        jedisDao.set(ITEM_DETAIL+"DESC:"+id,JsonUtils.objectToJson(tbItemDesc));
        jedisDao.expire(ITEM_DETAIL + ":DESC:" + id,
                ITEM_DETAIL_EXPIRE_TIME);
        return tbItemDesc;
    }
}
