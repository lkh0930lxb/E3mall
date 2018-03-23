package cn.e3mall.service.impl;

import cn.e3mall.common.pojo.TreeNode;
import cn.e3mall.mapper.TbItemCatMapper;
import cn.e3mall.pojo.TbItemCat;
import cn.e3mall.pojo.TbItemCatExample;
import cn.e3mall.service.ItemCatService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: 商品类目Service
 * @Author: Likh
 * @CreateDate: 2018/3/14 11:42
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private TbItemCatMapper itemCatMapper;

    /**
     * @Author:      Likh
     * @Date:        2018/3/14 11:42
     * @Description:
     * @Param:       [parentId]
     * @return:      java.util.List<cn.e3mall.common.pojo.TreeNode>
     */
    @Override
    public List<TreeNode> findCatList(long parentId) {
        TbItemCatExample example = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbItemCat> list = itemCatMapper.selectByExample(example);
        ArrayList<TreeNode> treeNodes = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            TbItemCat tbItemCat = list.get(i);
            TreeNode treeNode = new TreeNode();
            treeNode.setId(tbItemCat.getId());
            treeNode.setText(tbItemCat.getName());
            treeNode.setState(tbItemCat.getIsParent()?"closed":"open");
            treeNodes.add(treeNode);
        }
        return treeNodes;
    }
}
