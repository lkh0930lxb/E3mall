package cn.e3mall.portal.controller;

import cn.e3mall.common.pojo.AdItem;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.content.service.ContentService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description: 页面跳转
 * @Author: Likh
 * @CreateDate: 2018/3/14 16:27
 */
@Controller
public class PageController {

    @Autowired
    private ContentService contentService;

    //注入大广告位唯一标识
    @Value("${BIG_AD_CATEGORY_ID}")
    private Long BIG_AD_CATEGORY_ID;

    @RequestMapping("index")
    public String showIndex(Model model) {
        List<AdItem> contentList = contentService.findContentList(BIG_AD_CATEGORY_ID);
        String adJson = JsonUtils.objectToJson(contentList);
        model.addAttribute("ad1",adJson);
        return "index";
    }
}
