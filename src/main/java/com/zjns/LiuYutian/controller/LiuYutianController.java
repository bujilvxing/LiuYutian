package com.zjns.LiuYutian.controller;

import java.io.IOException;
import java.util.Date;
//import java.util.Map;

import javax.servlet.http.HttpServletRequest;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Recorder;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.persistent.FileRecorder;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.zjns.LiuYutian.utils.ErrorCode;
import com.zjns.LiuYutian.utils.LiuYutianResult;
//import com.zjns.LiuYutian.model.ImageItem;
//import com.zjns.LiuYutian.utils.LogUtils;
//import com.zjns.LiuYutian.utils.LiuYutianResult;


@Controller
public class LiuYutianController {

	/**
     * 取得文件名
     *
     * @param userId
     * @return
     */
    public String getPicName(long userId) {
        Date date = new Date();
        return String.format("xljb_%d_%d.jpg", userId, date.getTime());
    }
    
    public static String UPLOAD_URL = "url";
    public static String UPLOAD_URL_SMALL = "urlSmall";
    public static String UPLOAD_UID = "userId";
    public static String UPLOAD_SCENARIO = "scenario";
    public static String UPLOAD_CAPTION = "caption";
    
    /**
     * 取得回调的图像的值
     * @param ret 回调时的参数
     * @return 图像实体
     */
//    private static ImageItem getImageFromCallBack(ObjectNode ret) {
//        ImageItem imageItem = new ImageItem();
//        // 如果没有url,返回空对象
//        if (ret.get("key") == null)
//            return null;
//        imageItem.setKey(ret.get("key").asText());
//
//        if (ret.get("w") != null && ret.get("w").canConvertToInt())
//            imageItem.setW(ret.get("w").asInt());
//        if (ret.get("h") != null && ret.get("h").canConvertToInt())
//            imageItem.setH(ret.get("h").asInt());
//        if (ret.get("size") != null && ret.get("size").canConvertToInt())
//            imageItem.setSize(ret.get("size").asInt());
//        if (ret.get("bucket") != null)
//            imageItem.setBucket(ret.get("bucket").asText());
//        if (ret.get(UPLOAD_CAPTION) != null)
//            imageItem.setCaption(ret.get(UPLOAD_CAPTION).asText());
//        return imageItem;
//    }
    
//    /**
//     * 添加一张用户上传的图片
//     *
//     * @param userId
//     * @param imageItem
//     * @throws AizouException
//     */
//    public static void addUserAlbum(Long userId, ImageItem imageItem, String id) {
//        Datastore dsUser = MorphiaFactory.datastore();
//
//        Album entity = new Album();
//        entity.setId(new ObjectId(id));
//        entity.setcTime(System.currentTimeMillis());
//        entity.setImage(imageItem);
//        entity.setUserId(userId);
//        entity.setTaoziEna(true);
//        dsUser.save(entity);
//    }
	
	//设置好账号的ACCESS_KEY和SECRET_KEY
	private final static String secretKey = "pg_PpiOEf00OaukEeOZR3YUaM-0dTWIMjOxgFFnG";
	private final static String accessKey = "Vt4wTx0tZFlI9ScYqP7UV3gjO0YEqKOEzeY6_oKP";
	// 要上传的空间
	private final static String bucketname = "qiniu-bujilvxing";
	
	//密钥配置
	Auth auth = Auth.create(secretKey, accessKey);
	
	//创建上传对象
    UploadManager uploadManager = new UploadManager();
	
	/**
	 * 简单上传，使用默认策略，只需要设置上传的空间名就可以了
	 * @return
	 */
	public String getUpToken(){
		return auth.uploadToken(bucketname);
	}
	
	/**
	 * 覆盖上传
	 * @return
	 */
	public String getUpToken(String fileName){
		return auth.uploadToken(bucketname, fileName);
	}
	
	/**
	 * 简单上传
	 * @throws IOException
	 */
	public void upload(String fileName, String filePath) throws IOException {
		try {
			//调用put方法上传
			Response res = uploadManager.put(filePath, fileName, getUpToken());
			//打印返回的信息
			System.out.println("bodyString = " + res.bodyString());
			System.out.println("url = " + res.url());
			System.out.println("isNetworkBroken = " + res.isNetworkBroken());
			System.out.println("address = " + res.address);
			System.out.println("duration = " + res.duration);
			System.out.println("error = " + res.error);
			System.out.println("reqId = " + res.reqId);
			System.out.println("statusCode = " + res.statusCode);
			System.out.println("xlog = " + res.xlog);
			System.out.println("xvia = " + res.xvia);
			System.out.println("contentType = " + res.contentType());
			System.out.println("hashCode = " + res.hashCode());
			System.out.println("toString = " + res.toString());
			System.out.println("Cancelled = " + res.Cancelled);
			System.out.println("InvalidFile = " + res.InvalidFile);
			System.out.println("InvalidArgument = " + res.InvalidArgument);
			System.out.println("NetworkError = " + res.NetworkError);
			System.out.println("isJson = " + res.isJson());
			System.out.println("isServerError = " + res.isServerError());
			System.out.println("needRetry = " + res.needRetry());
			System.out.println("needSwitchServer = " + res.needSwitchServer());
	    } catch (QiniuException e) {
	        Response r = e.response;
	        // 请求失败时打印的异常的信息
	        System.out.println(r.toString());
	        try {
	            //响应的文本信息
	            System.out.println(r.bodyString());
	        } catch (QiniuException e1) {
	            //ignore
	        }
	    }       
	}
	
	/**
	 * 断点续传
	 * @param fileName 文件名
	 * @param filePath 文件上传的本地路径
	 * @param recordPath 设置断点记录文件保存在指定文件夹
	 * @throws IOException IO异常
	 */
	public void upload(String fileName, String filePath, String recordPath) throws IOException {
	    
	    //实例化recorder对象
	    Recorder recorder = new FileRecorder(recordPath);
	    //实例化上传对象，并且传入一个recorder对象
	    UploadManager uploadManager = new UploadManager(recorder);

	    try {
	    	//调用put方法上传
	    	Response res = uploadManager.put(filePath, fileName, getUpToken());
	    	//打印返回的信息
	    	System.out.println(res.bodyString()); 
	    } catch (QiniuException e) {
	        Response r = e.response;
	        // 请求失败时打印的异常的信息
	        System.out.println(r.toString());
	        try {
	            //响应的文本信息
	            System.out.println(r.bodyString());
	        } catch (QiniuException e1) {
	            //ignore
	        }
	    }       
	}
	
	//设置callbackUrl以及callbackBody，七牛将文件名和文件大小回调给业务服务器
	public String getUpToken(String fileName, long expires){
	    return auth.uploadToken(bucketname, fileName, 3600,new StringMap()
	          .put("callbackUrl","http://your.domain.com/callback")
	          .put("callbackBody", "filename=$(fname)&filesize=$(fsize)"));
	}
	
	// 上传并转码

	public void download(String key){
		//调用privateDownloadUrl方法生成下载链接,第二个参数可以设置Token的过期时间
		// 设置公开访问的话，这个URL就可以直接访问了
		String URL = String.format("http://oe7hx2tam.bkt.clouddn.com/%s", key);
		// 设置私有空间的话，必须通过下面的调用获取QueryString，也就是令牌token和过期时间e
		String downloadRUL = auth.privateDownloadUrl(URL,3600);
		System.out.println(downloadRUL);
	}
	
	@RequestMapping(value = "/app/qiniutest", method=RequestMethod.POST, produces = "application/json;charset=utf-8")
	public @ResponseBody String qiniutest(HttpServletRequest request, @RequestBody JsonNode body) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		//上传到七牛后保存的文件名
		String fileName = getPicName(Integer.parseInt(request.getHeader("UserId")));
		System.out.println(fileName);
		//上传文件的本地路径
		String filePath = null;// 例如："C:\\Users\\xiaozhi\\Desktop\\可达性分析算法判定对象是否可回收.jpg";
		if(body.has("filePath")) {
			JsonNode filePathObj = body.get("filePath"); 
			filePath = filePathObj == null ? "" : filePathObj.asText();
		} else {
			return LiuYutianResult.unprocessable(ErrorCode.INVALID_ARGUMENTS, body, "文件路径不合法");
		}
		
		try {
			upload(fileName, filePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return LiuYutianResult.unprocessable(ErrorCode.ServerException, body, "上传失败");
		}
		download(fileName);
		return LiuYutianResult.ok(body, null);
    }
}
