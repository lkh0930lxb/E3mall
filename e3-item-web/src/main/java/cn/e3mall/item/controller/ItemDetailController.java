package cn.e3mall.item.controller;

import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 商品详情页面
 * @Author: Likh
 * @CreateDate: 2018/3/20 10:18
 */
@RestController
public class ItemDetailController {

    @Autowired
    private ItemService itemService;

    @RequestMapping("{itemId}")
    public String itemDetail(@PathVariable long itemId, Model model){
        //查询商品信息
        TbItem item = itemService.findItemById(itemId);

        //查询商品描述信息
        TbItemDesc desc =itemService.findItemDescById(itemId);

        model.addAttribute("item",item);
        model.addAttribute("itemDesc",desc);
        return "item";
    }
}
