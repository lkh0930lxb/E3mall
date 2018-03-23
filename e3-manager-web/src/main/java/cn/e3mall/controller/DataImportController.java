package cn.e3mall.controller;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.search.service.SearchItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: Likh
 * @CreateDate: 2018/3/15 17:53
 */
@RestController
public class DataImportController {
    @Autowired
    private SearchItemService searchItemService;

    @RequestMapping("/index/item/import")
    public E3Result dataImport(){
        E3Result result = searchItemService.getItemList();
        return result;
    }
}
