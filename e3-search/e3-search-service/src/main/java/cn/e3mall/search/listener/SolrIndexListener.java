package cn.e3mall.search.listener;

import cn.e3mall.search.mapper.SearchItemMapper;
import cn.e3mall.search.pojo.SearchItem;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: activeMQ监听器   同步索引库
 * @Author: Likh
 * @CreateDate: 2018/3/18 17:04
 */
public class SolrIndexListener implements MessageListener{

    @Autowired
    private SearchItemMapper searchItemMapper;

    @Autowired
    private SolrServer solrServer;

    @Override
    public void onMessage(Message message) {
        Long itemId =null;
        try {
            if (message instanceof TextMessage) {
                TextMessage tm = (TextMessage) message;
                itemId= Long.parseLong(tm.getText());
                SearchItem searchItem = searchItemMapper.findSearchItemToSolrIndexWithItemId(itemId);
                SolrInputDocument document = new SolrInputDocument();
                document.setField("id",searchItem.getId());
                document.setField("item_title",searchItem.getTitle());
                document.setField("item_sell_point",searchItem.getSell_point());
                document.setField("item_price",searchItem.getPrice());
                document.setField("item_desc",searchItem.getItem_desc());
                document.setField("item_image",searchItem.getImage());
                document.setField("item_category_name",searchItem.getCategory_name());
                solrServer.add(document);
                solrServer.commit();
            }
            } catch(Exception e){
                e.printStackTrace();
        }
    }
}
