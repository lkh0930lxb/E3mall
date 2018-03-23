package cn.e3mall.service;

import cn.e3mall.common.pojo.DataGridResult;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;

public interface ItemService {
    //根据商品id查询商品信息
    TbItem findItemById(long id);

    //分页查询商品列表

    DataGridResult findItemList(int page,int rows);

    E3Result addItem(TbItem item, String desc);

    ////根据商品id查询商品描述信息
    TbItemDesc findItemDescById(long id);
}
