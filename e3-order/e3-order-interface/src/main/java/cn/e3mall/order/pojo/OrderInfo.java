package cn.e3mall.order.pojo;

import cn.e3mall.pojo.TbOrder;
import cn.e3mall.pojo.TbOrderItem;
import cn.e3mall.pojo.TbOrderShipping;
import java.io.Serializable;
import java.util.List;

/**
 * @Description: 表单数据
 * @Author: Likh
 * @CreateDate: 2018/3/23 15:17
 */
public class OrderInfo implements Serializable{
    private List<TbOrderItem> orderItems;
    private TbOrder orders;
    private TbOrderShipping orderShipping;

    public List<TbOrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<TbOrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public TbOrder getOrders() {
        return orders;
    }

    public void setOrders(TbOrder orders) {
        this.orders = orders;
    }

    public TbOrderShipping getOrderShipping() {
        return orderShipping;
    }

    public void setOrderShipping(TbOrderShipping orderShipping) {
        this.orderShipping = orderShipping;
    }
}
