package cn.e3mall.cart.service.impl;

import cn.e3mall.cart.jedis.JedisDao;
import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Description: 购物车系统
 * @Author: Likh
 * @CreateDate: 2018/3/22 11:59
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private JedisDao jedisDao;
    //redis购物车唯一标识
    @Value("${REDIS_CART}")
    private String REDIS_CART;

    //redis购物车有序商品id唯一标识
    @Value("${REDIS_SORT_CART}")
    private String REDIS_SORT_CART;

    /**
     * 需求:添加redis购物车
     * @param userId
     * @param itemId
     * @param num
     * @return E3mallResult
     * 业务:
     * 1,判断redis是否有相同商品
     * 2,有,商品数量相加
     * 3,没有,直接添加即可
     * 4,购物车商品必须有顺序,后添加,先展示(购物车列表)
     */
    @Override
    public E3Result addCart(long userId, long itemId, int num) {
        //判断redis中是否有相同商品
        Boolean hexists = jedisDao.hexists(REDIS_CART + ":" + userId, itemId + "");
        //如果返回结果为true,表示存在相同商品
        if (hexists){
            String itemJson = jedisDao.hget(REDIS_CART + "：" + userId, itemId + "");
            TbItem tbItem = JsonUtils.jsonToPojo(itemJson, TbItem.class);
            tbItem.setNum(tbItem.getNum()+num);
            addCartRedis(userId,tbItem);
        }
        TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
        tbItem.setNum(num);
        addCartRedis(userId,tbItem);
        return E3Result.ok();
    }

    /**
     * 需求:合并cookie购物车到redis购物车数据
     * @param userId
     * @param cartList
     * @return E3mallResult
     * 业务;
     * 1,判断redis购物车是否有相同的商品,有,数量相加
     * 2,否则,直接添加即可
     */
    @Override
    public E3Result mergeCart(Long userId, List<TbItem> cartList) {
        //遍历购物车列表
        for (TbItem tbItem : cartList) {
            this.addCart(userId, tbItem.getId(), tbItem.getNum());
        }
        return E3Result.ok();
    }

    /**
     * 需求:根据用户id,查询此用户购物车数据
     * @param userId
     * @return List<TbItem>
     * 业务:
     * 1,先sorted-set有序集合排序商品id
     * 2,再根据有序id查询redis购物车商品
     */
    @Override
    public List<TbItem> findRedisCart(Long userId) {
        List<TbItem> cartList =new ArrayList<>();
        Set<String> itemIds = jedisDao.zrevrange(REDIS_SORT_CART + ":" + userId, 0L, -1L);
        for (String itemId : itemIds) {
            String itemJson = jedisDao.hget(REDIS_CART + ":" + userId, itemId);
            TbItem tbItem = JsonUtils.jsonToPojo(itemJson, TbItem.class);
            cartList.add(tbItem);
        }

        return cartList;
    }

    /**
     * 需求:根据商品id删除此用户购物车商品
     * @param userId
     * @param itemId
     * @return E3mallResult
     * 业务:
     * 1,先删除此商品排序id
     * 2,再删除购物车商品
     */
    @Override
    public E3Result deleteCart(Long userId, Long itemId) {
        // 1,先删除此商品排序id sorted-set
        jedisDao.zrem(REDIS_SORT_CART+":"+userId,itemId+"");
        //再删除购物车商品
        jedisDao.hdel(REDIS_CART+":"+userId,itemId+"");
        return E3Result.ok();
    }
    /**
     * 需求:修改redis购物车商品数量,价格随之改变
     * @param userId
     * @param itemId
     * @param num
     * @return E3mallResult
     */
    @Override
    public E3Result updateRedisCart(Long userId, Long itemId, Integer num) {
        //获取redis购物车此商品的数据
        String cartJson = jedisDao.hget(REDIS_CART + ":" + userId, itemId + "");
        TbItem tbItem = JsonUtils.jsonToPojo(cartJson, TbItem.class);
        tbItem.setNum(num);
        //把商品数据再次放入redis购物车
        jedisDao.hset(REDIS_SORT_CART+":"+userId,itemId+"",JsonUtils.objectToJson(tbItem));

        return E3Result.ok();
    }


    private void addCartRedis(long userId, TbItem tbItem) {
        //把添加数量后商品放回redis购物车
        jedisDao.hset(REDIS_CART+":"+userId,tbItem.getId()+"",JsonUtils.objectToJson(tbItem));
        //购物车商品必须有顺序,后添加,先展示(购物车列表)
        Long millis = System.currentTimeMillis();
        //将商品id进行排序
        //把购物车商品id添加到sorted-set有序集合中进行排序,默认排序根据时间,从小到大进行排序
        jedisDao.zadd(REDIS_SORT_CART+":"+userId,millis.doubleValue(),tbItem.getId()+"");
    }

}
