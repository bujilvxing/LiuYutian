package com.zjns.LiuYutian.controller;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zjns.LiuYutian.model.ImageItem;
import com.zjns.LiuYutian.utils.LogUtils;
import com.zjns.LiuYutian.utils.LiuYutianResult;


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
        return String.format("avt_%d_%d.jpg", userId, date.getTime());
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
    private static ImageItem getImageFromCallBack(ObjectNode ret) {
        ImageItem imageItem = new ImageItem();
        // 如果没有url,返回空对象
        if (ret.get("key") == null)
            return null;
        imageItem.setKey(ret.get("key").asText());

        if (ret.get("w") != null && ret.get("w").canConvertToInt())
            imageItem.setW(ret.get("w").asInt());
        if (ret.get("h") != null && ret.get("h").canConvertToInt())
            imageItem.setH(ret.get("h").asInt());
        if (ret.get("size") != null && ret.get("size").canConvertToInt())
            imageItem.setSize(ret.get("size").asInt());
        if (ret.get("bucket") != null)
            imageItem.setBucket(ret.get("bucket").asText());
        if (ret.get(UPLOAD_CAPTION) != null)
            imageItem.setCaption(ret.get(UPLOAD_CAPTION).asText());
        return imageItem;
    }
    
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
    
    /**
     * 上传回调
     * @param request http请求参数
     * @return 
     */
    public String getCallback(HttpServletRequest request, Map<String, String[]> fav) {
    	ObjectMapper mapper = new ObjectMapper();
        ObjectNode ret = mapper.createObjectNode();
        String scenario = null;
        String url = null;
        String userId = null;
        String id = null;
        String title = "";
        for (Map.Entry<String, String[]> entry : fav.entrySet()) {
            String key = entry.getKey();
            String[] value = entry.getValue();
            if (key.equals(UPLOAD_URL))
                url = value[0];
            if (key.equals(UPLOAD_UID))
                userId = value[0];
            if (key.equals("id"))
                id = value[0];
            if (key.equals(UPLOAD_CAPTION))
                title = value[0];
            if (key.equals(UPLOAD_SCENARIO)) {
                scenario = value[0];
                LogUtils.info(LiuYutianController.class, "Test Upload CallBack.Scenario:" + scenario, key + "&&" + value[0]);
            }
            ret.put(key, value[0]);
        }

        if (scenario != null && scenario.equals("album")) {
            LogUtils.info(LiuYutianController.class, "Test UPLOAD_CAPTION:" + title);
            ImageItem imageItem = getImageFromCallBack(ret);
            if (imageItem == null)
                return LiuYutianResult.serverException("Can't get image key!");
//            UserUgcAPI.addUserAlbum(Long.valueOf(userId), imageItem, id);
        } else {
            LogUtils.info(LiuYutianController.class, "Start." + userId.toString() + url);
//            FinagleUtils$.MODULE$.updateUserAvatar(userId, url);
            LogUtils.info(LiuYutianController.class, "End." + userId.toString() + url);
        }
        //UserAPI.resetAvater(Long.valueOf(userId), url);
        ret.put("success", true);

        return LiuYutianResult.ok(ret, null);
    }

	/**
	 * 获取资源上传凭证
	 * @param scenario 
	 * @param request http请求参数
	 * @return 上传凭证
	 */
	public String putPolicy(String scenario, HttpServletRequest request) {
        
        String userId = request.getHeader("UserId");
        String picName = getPicName(Integer.parseInt(request.getHeader("UserId")));
        String secretKey = "pg_PpiOEf00OaukEeOZR3YUaM-0dTWIMjOxgFFnG";
        String accessKey = "Vt4wTx0tZFlI9ScYqP7UV3gjO0YEqKOEzeY6_oKP";
        String caption = request.getParameter("caption") == null ? "" : request.getParameter("caption");
        String scope, callbackUrl;
//        if (scenario.equals("portrait") || scenario.equals("album")) {
//            scope = "";//qiniu.get("taoziAvaterScope").toString();
//            // 回调的url
//            String hostname = "127.0.0.1";
//            String url = routes.MiscCtrl.getCallback().url();
//            callbackUrl = new StringBuilder().append("http://").append(hostname).append(url).toString();
//            LogUtils.info(MiscCtrl.class, "Test Upload CallBack.callbackUrl:" + callbackUrl);
//        } else
//            return new TaoziResBuilder().setCode(ErrorCode.INVALID_ARGUMENT)
//                    .setMessage(TaoziSceneText.instance().text(SceneID.INVALID_UPLOAD_SCENE))
//                    .build();
//
//        //取得上传策略
//        ObjectNode policy = getPutPolicyInfo(scope, picName, callbackUrl, Integer.valueOf(userId), scenario, caption);
//        // UrlBase64编码
//        String encodedPutPolicy = Base64.encodeBase64URLSafeString(policy.toString().trim().getBytes());
//        encodedPutPolicy = Utils.base64Padding(encodedPutPolicy);
//        // 构造密钥并UrlBase64编码
//        String encodedSign = Base64.encodeBase64URLSafeString(Utils.hmac_sha1(encodedPutPolicy, secretKey));
//        encodedSign = Utils.base64Padding(encodedSign);
//
//        ObjectNode ret = Json.newObject();
//        ret.put("uploadToken", accessKey + ":" + encodedSign + ":" + encodedPutPolicy);
//        ret.put("key", picName);
//
//        return new TaoziResBuilder().setBody(ret).build();
        return null;
    }

	
	@RequestMapping(value = "/app/qiniutest", method=RequestMethod.POST, produces = "application/json;charset=utf-8")
	public @ResponseBody String qiniutest(HttpServletRequest request, @RequestBody JsonNode body) {
       ObjectMapper mapper = new ObjectMapper();
       ObjectNode node = mapper.createObjectNode();
       node.put("code", 200);
       node.put("msg", "success");
       node.set("result", body);
       return node.toString();
    }
}
