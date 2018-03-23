package cn.e3mall.controller;

import cn.e3mall.common.pojo.DataGridResult;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: Likh
 * @CreateDate: 2018/3/14 21:01
 */
@RestController
public class ContentController {
    @Autowired
    private ContentService contentService;

    /**
     * @Author:      Likh
     * @Date:        2018/3/14 21:09
     * @Description: 根据categoryId查询该分类内容列表
     * @Param:       [categoryId, page, rows]
     * @return:      cn.e3mall.common.pojo.DataGridResult
     */
    @RequestMapping("/content/query/list")
    public DataGridResult findCategoryListByCategoryId(Long categoryId,
                                                       @RequestParam(defaultValue = "1") Integer page,
                                                       @RequestParam(defaultValue = "20") Integer rows){
        DataGridResult result = contentService.findCategoryListByCategoryId(categoryId, page, rows);
        return result;
    }

    @RequestMapping("/content/save")
    public E3Result saveContent(TbContent content){
        E3Result e3Result = contentService.saveContent(content);
        return e3Result;
    }
}
