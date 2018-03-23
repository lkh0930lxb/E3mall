package cn.e3mall.user.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 页面跳转
 * @Author: Likh
 * @CreateDate: 2018/3/20 17:32
 */
@RestController
public class PageController {
    /**
     * @Author:      Likh
     * @Date:        2018/3/20 17:34
     * @Description: 登录页面
     * @Param:       []
     * @return:      java.lang.String
     */
    @RequestMapping("/page/login")
    public String showLogin(String redirectUrl, Model model){
        model.addAttribute("redirect",redirectUrl);
        return "login";
    }

    /**
     * @Author:      Likh
     * @Date:        2018/3/20 17:34
     * @Description: 注册页面
     * @Param:       []
     * @return:      java.lang.String
     */
    @RequestMapping("/page/register")
    public String showRegister(){
        return "register";
    }


}
