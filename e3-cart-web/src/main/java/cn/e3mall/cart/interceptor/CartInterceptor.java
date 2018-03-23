package cn.e3mall.cart.interceptor;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.user.service.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description: 拦截器
 * @Author: Likh
 * @CreateDate: 2018/3/21 16:41
 */
public class CartInterceptor implements HandlerInterceptor{

    @Autowired
    private UserService userService;

    @Value("${E3_TOKEN}")
    private String E3_TOKEN;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //判断用户是否登录
        String token = CookieUtils.getCookieValue(httpServletRequest, E3_TOKEN);
        if (StringUtils.isNotBlank(token)){
            E3Result result = userService.findUserByToken(token);
            if (result.getStatus()==200){
                TbUser user= (TbUser) result.getData();
                httpServletRequest.setAttribute("user",user);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
