package cn.e3mall.cart.service;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.pojo.TbItem;
import java.util.List;

public interface CartService {
    //添加购物车
    E3Result addCart(long userId,long itemId,int num);

    /**
     * 需求:合并cookie购物车到redis购物车数据
     * @param id
     * @param cartList
     * @return E3Result
     */
    E3Result mergeCart(Long id, List<TbItem> cartList);

    /**
     * 需求:根据用户id,查询此用户购物车数据
     * @param userId
     * @return List<TbItem>
     */
    List<TbItem> findRedisCart(Long userId);

    /**
     * 需求:根据商品id删除此用户购物车商品
     * @param userId
     * @param itemId
     * @return E3mallResult
     * 业务:
     * 1,先删除此商品排序id
     * 2,再删除购物车商品
     */
    E3Result deleteCart(Long userId,Long itemId);

    /**
     * 需求:修改redis购物车商品数量,价格随之改变
     * @param userId
     * @param itemId
     * @param num
     * @return E3mallResult
     */
    E3Result updateRedisCart(Long userId,Long itemId,Integer num);
}
