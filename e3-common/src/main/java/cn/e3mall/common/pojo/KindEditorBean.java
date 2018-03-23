package cn.e3mall.common.pojo;

import java.io.Serializable;
/**
 * 
 * Title KindEditorBean
 * Description 文件上传返回值对象
 * @author Administrator
 * @date 2017年9月14日
 */
public class KindEditorBean implements Serializable{
	
	//成功时:
	/*//成功时
	{
	        "error" : 0,
	        "url" : "http://www.example.com/path/to/file.ext",
	        "message":null;
	}
	//失败时:
	{
        "error" : 0,
        "url" :null,
        "message":"上传失败";
	}*/
	private int error;
	private String url;
	private String message;
	public int getError() {
		return error;
	}
	public void setError(int error) {
		this.error = error;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
	
}
