package cn.e3mall.cart.controller;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.service.ItemService;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description: 购物车系统
 * @Author: Likh
 * @CreateDate: 2018/3/22 14:10
 */
@Controller
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private ItemService itemService;

    //注入cookie购物车唯一标识
    @Value("${COOKIE_CART}")
    private String COOKIE_CART;

    //注入cookie购物车过期时间:7 days
    @Value("${COOKIE_CART_EXPIRE_TIME}")
    private Integer COOKIE_CART_EXPIRE_TIME;

    /**
     * 需求:添加购物车
     * 请求:http://localhost:8090/cart/add/150797615489847.html?num=7
     * 参数:Long itemId,Integer num
     * 返回值:cartSuccess.jsp
     * 业务:
     * 判断用户是否登录
     * 1,登录   (操作redis购物车)
     * > 判断redis购物车中是否有相同的商品
     * > 有,商品数量相加
     * > 否则,直接添加即可
     * 2,未登录 (cookie购物车)
     * > 获取cookie购物车列表
     * > 判断cookie中是否有相同商品
     * > 有,商品数量相加
     * > 没有,直接添加即可
     *
     */
    @RequestMapping("/cart/add/{itemId}")
    public String addCart(@PathVariable Long itemId,HttpServletRequest request,HttpServletResponse response,
                            @RequestParam(defaultValue = "1") int num){
        //判断用户是否登录
        TbUser user= (TbUser) request.getAttribute("user");
        if (user!=null && !user.equals("")){
            //用户此时处于登录状态
            E3Result e3Result = cartService.addCart(user.getId(), itemId, num);
            return "cartSuccess";
        }
        //未登录 添加cookie购物车
        //定义判断cookie购物车是否有相同商品标识
        boolean flag=false;
        List<TbItem> cartList=this.getCookieCartList(request);
        for (TbItem tbItem : cartList) {
            //如果商品id和待添加的商品id相同,说明此商品和购物车商品相同
            if (tbItem.getId()==itemId.longValue()){
                tbItem.setNum(tbItem.getNum()+num);
                flag=true;
                break;
            }
        }
        if (!flag){
            TbItem item = itemService.findItemById(itemId);
            item.setNum(num);
            cartList.add(item);
        }
        //把cartList购物车列表写回cookie购物车
        CookieUtils.setCookie(request,response,COOKIE_CART, JsonUtils.objectToJson(cartList),COOKIE_CART_EXPIRE_TIME,true);
        return "cartSuccess";
    }

    /**
     * 需求:跳转到结算页面
     * 请求:/cart/cart.html
     * 参数:request,response
     * 返回值:cart.jsp
     * 业务:
     * 1,登录  (查询redis购物车)
     *   > 判断cookie购物车是否有值
     *   > 有,合并cookie购物车到redis购物车
     *   > 合并完毕后,清空cookie购物车
     *   > 查询redis购物车,进行列表展示
     * 2,未登录 (查询cookie购物车)
     *   > 查询cookie购物车数据即可
     */
    @RequestMapping("/cart/cart")
    public String showCart(HttpServletResponse response,HttpServletRequest request){
        TbUser user= (TbUser) request.getAttribute("user");
        //获取cookie中购车车列表
        List<TbItem> cartList = this.getCookieCartList(request);
        if (user != null) {
            //判断cookie购物车列表中是否有数据
            if (!cartList.isEmpty()){
                //合并购物车
                E3Result e3Result = cartService.mergeCart(user.getId(), cartList);
                //将cookie中的数据清空
                CookieUtils.setCookie(request,response,COOKIE_CART,"",0,true);
            }
            cartList = cartService.findRedisCart(user.getId());
        }
        //页面回显
        request.setAttribute("cartList",cartList);

        return "cart";
    }

    /**
     * 需求:删除购物车结算页面商品
     * 请求:http://localhost:8090/cart/delete/150788642650019.html
     * 参数:Long itemId
     * 返回值: redirect:/cart/cart.html
     * 业务:
     * 1,登录 (删除的redis购物车)
     * 2,未登录 (删除cookie购物车)
     */
    @RequestMapping("/cart/delete/${itemId}")
    public String deleteCart(@PathVariable Long itemId,HttpServletRequest request,HttpServletResponse response){
        TbUser user= (TbUser) request.getAttribute("user");
        if (user!=null){
            //删除redis购物车
            cartService.deleteCart(user.getId(), itemId);
            return "redirect:/cart/cart.html";
        }
         //未登录,删除cookie购物车
        //获取cookie购物车列表
        List<TbItem> cartList = this.getCookieCartList(request);
        for (TbItem tbItem : cartList) {
            if (tbItem.getId()==itemId.longValue()){
                cartList.remove(tbItem);
                break;
            }
        }
        //把购物车列表写回到cookie购物车
        CookieUtils.setCookie(request, response, COOKIE_CART, JsonUtils.objectToJson(cartList),COOKIE_CART_EXPIRE_TIME, true);

        return "redirect:/cart/cart.html";
    }

    /**
     * 需求:更新购物车数量,价格随之而改变
     * 请求:/cart/update/num/{itemId}/{num}.html
     * 参数:Long itemId,Integer num
     * 返回值:json格式E3mallResult
     * 业务:
     * 1,登录 (更新redis购物车)
     * 2,未登录 (更新cookie购物车)
     */
    @RequestMapping("/cart/update/num/{itemId}/{num}")
    public E3Result updateRedisCart(@PathVariable Long itemId,
                                    @PathVariable Integer num,
                                    HttpServletRequest request,HttpServletResponse response){
        TbUser user= (TbUser) request.getAttribute("user");
        if (user != null) {
            E3Result e3Result = cartService.updateRedisCart(user.getId(), itemId, num);
            return e3Result;
        }else {
            List<TbItem> itemList = this.getCookieCartList(request);
            for (TbItem tbItem : itemList) {
                tbItem.setNum(num);
                break;
            }
        }
        return E3Result.ok();
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
