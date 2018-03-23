package cn.e3mall.order.service.impl;

import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.mapper.TbOrderItemMapper;
import cn.e3mall.mapper.TbOrderMapper;
import cn.e3mall.mapper.TbOrderShippingMapper;
import cn.e3mall.order.pojo.OrderInfo;
import cn.e3mall.order.service.OrderService;
import cn.e3mall.pojo.TbOrder;
import cn.e3mall.pojo.TbOrderItem;
import cn.e3mall.pojo.TbOrderShipping;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: Likh
 * @CreateDate: 2018/3/23 14:15
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private TbOrderMapper orderMapper;
    @Autowired
    private TbOrderItemMapper orderItemMapper;
    @Autowired
    private TbOrderShippingMapper orderShippingMapper;
    @Autowired

    @Override
    public String createOrder(OrderInfo orderInfo) {
        //获取订单对象
        TbOrder orders = orderInfo.getOrders();
        //生成订单id，毫秒值＋uuid
        long orderId = IDUtils.genItemId();
        //订单id
        orders.setOrderId(orderId+"");
        //邮费
        orders.setPostFee("0");
        //状态:1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
        orders.setStatus(1);
        //订单创建时间
        Date date = new Date();
        orders.setCreateTime(date);
        orders.setUpdateTime(date);
        //保存订单数据
        orderMapper.insert(orders);
        //保存订单明细
        //一个订单有多个订单明细
        //获取订单明细集合数据
        List<TbOrderItem> orderItems = orderInfo.getOrderItems();
        //循环订单明细，保存单个对象
        for (TbOrderItem orderItem : orderItems) {
            orderItem.setId(UUID.randomUUID().toString());
            //设置外键
            orderItem.setOrderId(orderId+"");
            orderItemMapper.insert(orderItem);
        }
        //保存收货人地址
        //获取收货人地址
        TbOrderShipping orderShipping = orderInfo.getOrderShipping();
        orderShipping.setOrderId(orderId+"");
        orderShipping.setCreated(date);
        orderShipping.setUpdated(date);
        orderShippingMapper.insert(orderShipping);
        //返回订单号
        return orderId+"";
    }
}
