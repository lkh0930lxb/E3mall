package cn.e3mall.item.listener;

import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

/**
 * 需求:接受消息商品id,同步更新静态页面
 * @author Administrator
 * 流程:
 * 1,接受商品id消息
 * 2,根据商品id查询模版文件需要的数据
 * 3,生成HTML文件
 * 三要素:
 * 1,模版ftl
 * 2,数据
 * 3,代码
 *
 */
public class FMListener implements MessageListener {

    @Autowired
    private ItemService itemService;

    @Autowired
    private FreeMarkerConfig freeMarkerConfig;

    @Value(("${STATIC_SERVER_URL}"))
    private String STATIC_SERVER_URL;
    @Override
    public void onMessage(Message message) {
        Long itemId=null;
        if (message instanceof TextMessage){
            TextMessage tm= (TextMessage) message;
            try {
                itemId= Long.parseLong(tm.getText());
                //延时1s
                Thread.sleep(1000);
                //获取配置对象
                Configuration configuration = freeMarkerConfig.getConfiguration();
                Template template = configuration.getTemplate("item.ftl");
                //准备生成模板数据
                Map<String, Object> map = new HashMap<>();
                TbItem tbItem = itemService.findItemById(itemId);
                TbItemDesc itemDesc = itemService.findItemDescById(itemId);
                map.put("item",tbItem);
                map.put("itemDesc",itemDesc);
                //创建writer
                FileWriter writer = new FileWriter(new File(STATIC_SERVER_URL + itemId + ".html"));
                template.process(map,writer);
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
