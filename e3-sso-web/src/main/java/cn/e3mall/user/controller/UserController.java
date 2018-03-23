package cn.e3mall.user.controller;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.user.service.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: Likh
 * @CreateDate: 2018/3/20 17:55
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Value("${E3_TOKEN}")
    private String E3_TOKEN;

    @Value("${E3_TOKEN_EXPIRE_TIME}")
    private Integer E3_TOKEN_EXPIRE_TIME;

    @RequestMapping(value = "/user/check/{param}/{type}",method = RequestMethod.POST)
    public E3Result dataCheck(@PathVariable String param,@PathVariable Integer type){
        E3Result e3Result = userService.dataCheck(param, type);
        return e3Result;
    }
    /**
     * @Author:      Likh
     * @Date:        2018/3/21 11:51
     * @Description: 用户注册
     * @Param:       [user]
     * @return:      cn.e3mall.common.pojo.E3Result
     */
    @RequestMapping(value = "/user/register",method = RequestMethod.POST)
    public E3Result register(TbUser user){
        E3Result result = userService.register(user);
        return result;
    }
    /**
     * @Author:      Likh
     * @Date:        2018/3/22 9:38
     * @Description: 用户登录
     * @Param:       [username, password, request, response]
     * @return:      cn.e3mall.common.pojo.E3Result
     */
    @RequestMapping(value = "/user/login",method = RequestMethod.POST)
    public E3Result login(String username,String password,HttpServletRequest request, HttpServletResponse response){
        E3Result result = userService.login(username, password);
        //把token放入cookie中
        CookieUtils.setCookie(request,response,E3_TOKEN,result.getData().toString(),E3_TOKEN_EXPIRE_TIME,true);
        return result;
    }

    @RequestMapping(value = "/user/token/{token}",method = RequestMethod.POST)
    public Object findUserByToken(@PathVariable String token,String callback){
        E3Result result = userService.findUserByToken(token);
        if (StringUtils.isNotBlank(callback)){
            MappingJacksonValue value = new MappingJacksonValue(result);
            value.setJsonpFunction(callback);
            return value;
        }
        return result;
    }
}