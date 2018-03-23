package cn.e3mall.content.service.impl;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.TreeNode;
import cn.e3mall.content.service.ContentCategoryService;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: 展示内容分类
 * @Author: Likh
 * @CreateDate: 2018/3/14 16:37
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
    @Autowired
    private TbContentCategoryMapper contentCategoryMapper;
    
    /**
     * @Author:      Likh
     * @Date:        2018/3/14 16:50
     * @Description: 
     * @Param:       [parentId] 
     * @return:      java.util.List<cn.e3mall.common.pojo.TreeNode>
     */
    @Override
    public List<TreeNode> getContentCategoryList(long parentId) {
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
        ArrayList<TreeNode> treeNodes = new ArrayList<TreeNode>();
        for (int i = 0; i < list.size(); i++) {
            TbContentCategory contentCategory = list.get(i);
            TreeNode treeNode = new TreeNode();
            treeNode.setId(contentCategory.getId());
            treeNode.setText(contentCategory.getName());
            treeNode.setState(contentCategory.getIsParent()?"closed":"open");
            treeNodes.add(treeNode);
        }
        return treeNodes;
    }
    /**
     * @Author:      Likh
     * @Date:        2018/3/14 17:58
     * @Description:
     * @Param:       [parentId, name]
     * @return:      cn.e3mall.common.pojo.E3Result
     */
    @Override
    public E3Result createNode(long parentId, String name) {
        TbContentCategory category = new TbContentCategory();
        category.setId(parentId);
        category.setName(name);
        //状态  1正常，2删除
        category.setStatus(1);
        category.setSortOrder(1);
        //该类目是否是父节点，父节点为1，true;子节点为0，false(新建节点一定是子节点)
        category.setIsParent(false);
        Date date = new Date();
        category.setCreated(date);
        category.setUpdated(date);
        contentCategoryMapper.insert(category);
        //如果添加的节点为子节点，修改isParent的值
        TbContentCategory tbContentCategory = contentCategoryMapper.selectByPrimaryKey(parentId);
        if (!tbContentCategory.getIsParent()) {
            tbContentCategory.setIsParent(true);
            contentCategoryMapper.updateByPrimaryKey(tbContentCategory);
        }
        return E3Result.ok(category);
    }
}
