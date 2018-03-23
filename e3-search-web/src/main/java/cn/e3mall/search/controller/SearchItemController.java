package cn.e3mall.search.controller;

import cn.e3mall.search.pojo.ResultModel;
import cn.e3mall.search.service.SearchItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description: 根据搜索内容查询索引库
 * @Author: Likh
 * @CreateDate: 2018/3/16 14:11
 */
@Controller
public class SearchItemController {

    @Autowired
    private SearchItemService searchItemService;

    @RequestMapping("search")
    public String findItemIndex(@RequestParam(defaultValue="q")String q,
                                @RequestParam(defaultValue = "1") Integer page,
                                @RequestParam(defaultValue = "30") Integer rows, Model model){
        //解决乱码问题
        try {
            q = new String(q.getBytes("ISO8859-1"), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        ResultModel rm = searchItemService.findItemIndex(q, page, rows);
        //回显查询条件
        model.addAttribute("query",q);
        //回显页码数
        model.addAttribute("totalPages",rm.getTotalPages());
        //回显总记录
        model.addAttribute("itemList", rm.getItemList());
        //回显当前页
        model.addAttribute("page", page);
        return "search";
    }
}
