package cn.e3mall.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

/**
 * @Description:
 * @Author: Likh
 * @CreateDate: 2018/3/20 11:49
 */
@RestController
public class FMController {
    //注入spring提高freemarker整合对象
    @Autowired
    private FreeMarkerConfig freeMarkerConfig;


}
