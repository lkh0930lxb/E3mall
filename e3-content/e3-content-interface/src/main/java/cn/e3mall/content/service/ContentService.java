package cn.e3mall.content.service;

import cn.e3mall.common.pojo.AdItem;
import cn.e3mall.common.pojo.DataGridResult;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.pojo.TbContent;
import java.util.List;

public interface ContentService {

    //根据categoryId查询该分类内容列表(分页)
    DataGridResult findCategoryListByCategoryId(Long categoryId,Integer page,Integer rows);

    //保存广告内容
    E3Result saveContent(TbContent content);

    //根据categoryId查询该分类内容列表(大广告位维护)
    List<AdItem> findContentList(Long categoryId);
}
