package cn.e3mall.order.controller;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.order.pojo.OrderInfo;
import cn.e3mall.order.service.OrderService;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: Likh
 * @CreateDate: 2018/3/23 14:16
 */
@RestController
public class OrderController {
    @Value("${COOKIE_CART}")
    private String COOKIE_CART;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    /**
     * 需求:跳转到结算页面
     * 请求:http://localhost:8092/order/order-cart.html
     * 参数:request,response
     * 返回值:order-cart.jsp
     */
    @RequestMapping("/order/order-cart")
    public String orderCart(HttpServletRequest request, HttpServletResponse response){
        TbUser user= (TbUser) request.getAttribute("user");
            List<TbItem> cartList = this.getCookieCartList(request);
        if (!cartList.isEmpty()) {
            E3Result e3Result = cartService.mergeCart(user.getId(), cartList);
            CookieUtils.setCookie(request,response,COOKIE_CART,"",0,true);
        }
        //1,加载收货人地址(默认地址)
        //2,加载支付方式 (默认支付方式)--货到付款
        //3,加载购物清单 (redis购物车查询)
        // 调用远程购物车服务方法,查询redis购物车清单
        cartList = cartService.findRedisCart(user.getId());
        request.setAttribute("cartList",cartList);
        return "order-cart";
    }

    /**
     * 需求:提交订单,保存订单数据
     * 请求:/order/create.html
     * 参数:OrderInfo
     * 返回值:success.jsp
     */
    @RequestMapping("order/create")
    public String createOrder(OrderInfo orderInfo, Model model){
        //获取订单号
        String orderId = orderService.createOrder(orderInfo);
        //回显订单号
        model.addAttribute("orderId",orderId);
        //回显支付金额
        model.addAttribute("payment",orderInfo.getOrders().getPayment());
        //回显预计到达时间，3天
        DateTime dateTime = new DateTime();
        DateTime days = dateTime.plusDays(3);
        model.addAttribute("date",days.toString("yyyy-MM-dd"));
        return "success";
    }

    /**
     * 需求:获取cookie中购物车列表数据
     * @param request
     * @return
     */
    private List<TbItem> getCookieCartList(HttpServletRequest request) {
        String json = CookieUtils.getCookieValue(request, COOKIE_CART, true);
        if (StringUtils.isNotBlank(json)){
            List<TbItem> cartList = JsonUtils.jsonToList(json, TbItem.class);
            return cartList;
        }else {
            return new ArrayList<TbItem>();
        }
    }
}
