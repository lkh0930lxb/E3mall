package cn.e3mall.order.interceptor;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.user.service.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description:
 * @Author: Likh
 * @CreateDate: 2018/3/23 14:07
 */
public class OrderInterceptor implements HandlerInterceptor {
    //cookie存储session用户身份信息唯一标识常量
    @Value("${E3_TOKEN}")
    private String E3_TOKEN;

    //注入单点登录地址
    @Value("${SSO_URL}")
    private String SSO_URL;

    //注入单点登录服务对象
    @Autowired
    private UserService userService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String token = CookieUtils.getCookieValue(request, E3_TOKEN, true);
        if (StringUtils.isBlank(token)){
            String url = request.getRequestURL().toString();
            response.sendRedirect(SSO_URL+"?url="+url);
            return false;
        }
        E3Result result = userService.findUserByToken(token);
        if (result.getStatus()!=200){
            String url = request.getRequestURL().toString();
            response.sendRedirect(SSO_URL+"?url="+url);
            return false;
        }
        request.setAttribute("user",result.getData());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
