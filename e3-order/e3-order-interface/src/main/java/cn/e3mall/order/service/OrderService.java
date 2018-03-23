package cn.e3mall.order.service;

import cn.e3mall.order.pojo.OrderInfo;

public interface OrderService {

    /**
     * 需求:提交订单,保存订单数据
     * 参数:OrderInfo
     * 返回值:订单号
     */
    public String createOrder(OrderInfo orderInfo);
}
