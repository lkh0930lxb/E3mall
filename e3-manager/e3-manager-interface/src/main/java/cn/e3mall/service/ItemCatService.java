package cn.e3mall.service;

import cn.e3mall.common.pojo.TreeNode;
import java.util.List;

/**
 * @Description: 商品类目
 * @Author: Likh
 * @CreateDate: 2018/3/14 11:38
 */
public interface ItemCatService {
    //根据id查询节点列表
    List<TreeNode> findCatList(long parentId);
}
