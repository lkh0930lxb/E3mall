package cn.e3mall.controller;

import cn.e3mall.common.utils.FastDFSClient;
import cn.e3mall.common.utils.JsonUtils;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description: 图片上传controller
 * @Author: Likh
 * @CreateDate: 2018/3/14 13:53
 */
@Controller
public class PictureController {


    private String imageServerUrl="http://192.168.25.133/";

    /**
     * @Author:      Likh
     * @Date:        2018/3/14 14:00
     * @Description: 图片上传
     * @Param:       [uploadFile]
     * @return:      java.lang.String
     */
    @RequestMapping(value = "/pic/upload",produces = MediaType.TEXT_PLAIN_VALUE+";charset=utf-8")
    @ResponseBody
    public String uploadFile(MultipartFile uploadFile){
        try {
            String originalFilename = uploadFile.getOriginalFilename();
            String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:conf/client.conf");
            String url = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);
            url=imageServerUrl+url;
            Map result = new HashMap<>();
            result.put("error",0);
            result.put("url",url);
            return JsonUtils.objectToJson(result);
        } catch (Exception e) {
            Map result = new HashMap<>();
            result.put("error",1);
            result.put("message","上传文件失败："+e.getMessage());
            return JsonUtils.objectToJson(result);
        }
    }
}
