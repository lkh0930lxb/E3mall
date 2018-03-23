package cn.e3mall.controller;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.TreeNode;
import cn.e3mall.content.service.ContentCategoryService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: Likh
 * @CreateDate: 2018/3/14 17:07
 */
@RestController
public class ContentCategoryController {
    @Autowired
    private ContentCategoryService contentCategoryService;
    /**
     * @Author:      Likh
     * @Date:        2018/3/14 17:54
     * @Description: 根据id查询节点列表
     * @Param:       [parentId]
     * @return:      java.util.List<cn.e3mall.common.pojo.TreeNode>
     */
    @RequestMapping("/content/category/list")
    public List<TreeNode> getContentCategoryList(@RequestParam(value = "id",defaultValue = "0")long parentId){
        List<TreeNode> contentCategoryList = contentCategoryService.getContentCategoryList(parentId);
        return contentCategoryList;
    }

    @RequestMapping("content/category/create")
    public E3Result createNode(long parentId,String name){
        E3Result result = contentCategoryService.createNode(parentId, name);
        return result;
    }
}
