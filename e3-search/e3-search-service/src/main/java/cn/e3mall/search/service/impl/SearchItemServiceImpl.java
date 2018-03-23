package cn.e3mall.search.service.impl;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.search.dao.SearchItemDao;
import cn.e3mall.search.mapper.SearchItemMapper;
import cn.e3mall.search.pojo.ResultModel;
import cn.e3mall.search.pojo.SearchItem;
import cn.e3mall.search.service.SearchItemService;
import java.util.List;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: Likh
 * @CreateDate: 2018/3/15 17:32
 */
@Service
public class SearchItemServiceImpl implements SearchItemService {

    @Autowired
    private SearchItemMapper searchItemMapper;

    @Autowired
    private SolrServer solrServer;

    @Autowired
    private SearchItemDao searchItemDao;

    /**
     * @Author:      Likh
     * @Date:        2018/3/15 17:34
     * @Description: 查询索引库索引域字段一一对应数据,然后把数据导入索引库
     * @Param:       [] 
     * @return:      cn.e3mall.common.pojo.E3Result
     */
    @Override
    public E3Result getItemList() {
        try {
            List<SearchItem> itemList = searchItemMapper.getItemList();
            for (SearchItem searchItem : itemList) {
                SolrInputDocument document = new SolrInputDocument();
                document.addField("id",searchItem.getId());
                document.addField("item_title",searchItem.getTitle());
                document.addField("item_sell_point", searchItem.getSell_point());
                document.addField("item_price", searchItem.getPrice());
                document.addField("item_image", searchItem.getImage());
                document.addField("item_category_name",searchItem.getCategory_name());
                document.addField("item_desc", searchItem.getItem_desc());
                solrServer.add(document);
            }
            solrServer.commit();
            return E3Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return E3Result.build(500,"商品导入失败");
        }
    }

    /**
     * @Author:      Likh
     * @Date:        2018/3/16 13:50
     * @Description: 根据搜索条件，查询索引库
     * @Param:       [q, page, rows]
     * @return:      cn.e3mall.search.pojo.ResultModel
     */
    @Override
    public ResultModel findItemIndex(String queryString, Integer page, Integer rows) {
        SolrQuery solrQuery = new SolrQuery();
        if (queryString != null && !queryString.equals("")) {
            solrQuery.setQuery(queryString);
        }else {
            solrQuery.setQuery("*:*");
        }
        //分页
        int start=(page-1)*rows;
        solrQuery.setRows(rows);
        solrQuery.setStart(start);
        //开启高亮
        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("item_title");
        solrQuery.setHighlightSimplePre("<font color='red'>");
        solrQuery.setHighlightSimplePost("</font>");
        //设置复制域
        solrQuery.set("df","item_keywords");
        //调用Dao查询索引库
        ResultModel resultModel = searchItemDao.findItemIndex(solrQuery);
        Integer recordCount = resultModel.getRecordCount();
        Integer pages = recordCount/rows;
        if (recordCount%rows>0){
            pages++;
        }
        resultModel.setCurPage(page);
        resultModel.setTotalPages(pages);
        return resultModel;
    }
}
