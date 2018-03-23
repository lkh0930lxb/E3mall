package cn.e3mall.controller;

import cn.e3mall.common.pojo.TreeNode;
import cn.e3mall.service.ItemCatService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 商品类目controller
 * @Author: Likh
 * @CreateDate: 2018/3/14 11:50
 */
@RestController
public class ItemCatController {
    @Autowired
    private ItemCatService itemCatService;

    @RequestMapping("/item/cat/list")
    public List<TreeNode> findCatList(@RequestParam(value = "id",defaultValue = "0")long parentId){
        List<TreeNode> list = itemCatService.findCatList(parentId);
        return list;
    }
}
