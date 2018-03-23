package cn.e3mall.search.service;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.search.pojo.ResultModel;

public interface SearchItemService {
    /**
     * 需求:查询索引库索引域字段一一对应数据,然后把数据导入索引库
     */
    public E3Result getItemList();

    //根据搜索条件，查询索引库
    ResultModel findItemIndex(String queryString,Integer page,Integer rows);
}
