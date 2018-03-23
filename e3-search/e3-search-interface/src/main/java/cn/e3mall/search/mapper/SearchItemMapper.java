package cn.e3mall.search.mapper;

import cn.e3mall.search.pojo.SearchItem;
import java.util.List;

public interface SearchItemMapper {
    /**
     * 需求:查询索引库索引域字段一一对应数据,然后把数据导入索引库
     */
    List<SearchItem> getItemList();

    /**
     * 需求:根据id查询数据库,同步索引库
     * @param itemId
     * @return SearchItem
     */
    SearchItem findSearchItemToSolrIndexWithItemId(Long itemId);
}
