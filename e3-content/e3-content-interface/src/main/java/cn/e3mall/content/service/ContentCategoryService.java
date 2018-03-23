package cn.e3mall.content.service;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.TreeNode;
import java.util.List;

public interface ContentCategoryService {
    //根据id查询节点列表
    List<TreeNode> getContentCategoryList(long parentId);

    //添加节点
    E3Result createNode(long parentId,String name);
}
