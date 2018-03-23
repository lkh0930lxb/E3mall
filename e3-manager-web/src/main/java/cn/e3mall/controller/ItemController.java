package cn.e3mall.controller;

import cn.e3mall.common.pojo.DataGridResult;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 商品管理controller
 * @Author: Likh
 * @CreateDate: 2018/3/13 23:05
 */
@RestController
public class ItemController {
    @Autowired
    private ItemService itemService;

    /**
     * @Author:      Likh
     * @Date:        2018/3/14 11:16
     * @Description: 根据商品id查询商品信息
     * @Param:       [itemId]
     * @return:      cn.e3mall.pojo.TbItem
     */
    @RequestMapping("/item/{itemId}")
    public TbItem findItemById(@PathVariable long itemId){
        TbItem item = itemService.findItemById(itemId);
        return item;
    }

    /**
     * @Author:      Likh
     * @Date:        2018/3/14 11:36
     * @Description: 分页查询商品列表
     * @Param:       [page, rows] 
     * @return:      cn.e3mall.common.pojo.DataGridResult
     */
    @RequestMapping("/item/list")
    public DataGridResult findItemList(int page,int rows){
        DataGridResult result = itemService.findItemList(page, rows);
        return result;
    }

    @RequestMapping(value = "/item/save",method = RequestMethod.POST)
    public E3Result addItem(TbItem item,String desc){
        E3Result e3Result = itemService.addItem(item, desc);
        return e3Result;
    }
}
