package cn.e3mall.search.dao;

import cn.e3mall.search.pojo.ResultModel;
import org.apache.solr.client.solrj.SolrQuery;

public interface SearchItemDao {

    //根据查询条件，查询索引库
    ResultModel findItemIndex(SolrQuery solrQuery);
}
