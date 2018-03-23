package cn.e3mall.search.dao.impl;

import cn.e3mall.search.dao.SearchItemDao;
import cn.e3mall.search.pojo.ResultModel;
import cn.e3mall.search.pojo.SearchItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @Description: 根据查询条件，查询索引库
 * @Author: Likh
 * @CreateDate: 2018/3/16 11:46
 */
@Repository
public class SearchItemDaoImpl implements SearchItemDao{
    @Autowired
    private SolrServer solrServer;
    @Override
    public ResultModel findItemIndex(SolrQuery solrQuery) {
        ResultModel resultModel = new ResultModel();
        //创建商品搜索对象集合
        List<SearchItem> itemList=new ArrayList<>();
        try {
            QueryResponse response = solrServer.query(solrQuery);
            //获取结果文档集合对象集合
            SolrDocumentList results = response.getResults();
            //获取总记录数
            long numFound = results.getNumFound();
            //把总记录数设置分页包装类对象
            resultModel.setRecordCount((int) numFound);
            for (SolrDocument docunment : results) {
                SearchItem item = new SearchItem();
                //id
                String id = (String) docunment.get("id");
                item.setId(Long.parseLong(id));
                //标题
                String item_title = (String) docunment.get("item_title");
                //获取高亮
                Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
                Map<String, List<String>> map = highlighting.get(id);
                List<String> list = map.get("item_title");
                //判断高亮是否存在
                if (list != null && list.size()>0) {
                    item_title = list.get(0);
                }
                item.setTitle(item_title);
                //sell_point
                String item_sell_point = (String) docunment.get("item_sell_point");
                item.setSell_point(item_sell_point);

                //price
                Long item_price = (Long) docunment.get("item_price");
                item.setPrice(item_price);

                //image
                String item_image = (String) docunment.get("item_image");
                item.setImage(item_image);

                //item_category_name
                String item_category_name = (String) docunment.get("item_category_name");
                item.setCategory_name(item_category_name);

                //item_category_name
                String item_desc = (String) docunment.get("item_desc");
                item.setItem_desc(item_desc);
                itemList.add(item);
            }
            resultModel.setItemList(itemList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultModel;
    }
}
