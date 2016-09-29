package com.zjns.LiuYutian.utils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

import com.fasterxml.jackson.databind.JsonNode;

public class LogUtils {

	public static void info(Class<?> cls,String title, String info) {

        Logger log = LoggerFactory.getLogger(cls);
        log.info("---------CoreApi Debug Start---------");
        log.info(title);
        log.info(info);
        log.info("---------CoreApi Debug End---------");

    }

    public static void info(Class<?> cls, String info) {

        Logger log = LoggerFactory.getLogger(cls);
        log.info("---------CoreApi Debug Start---------");
        log.info(info);
        log.info("---------CoreApi Debug End---------");

    }

    public static void info(Class<?> cls, HttpHeaders headers, HttpServletRequest request, JsonNode body) {
        String header = getHeader(headers);
        String uri = getUrl(request);
        String post = getPost(body);
        Logger log = LoggerFactory.getLogger(cls);
        log.info("---------CoreApi Debug Start---------");
        log.info(header);
        log.info(uri);
        log.info(post);
        log.info("---------CoreApi Debug End---------");

    }

    /**
     * 取得http header信息
     * @param headers
     * @return header信息
     */
    private static String getHeader(HttpHeaders headers) {
        StringBuffer sbHead = new StringBuffer(10);
        sbHead.append("---Header---");
        sbHead.append("\r\n");
        for (Entry<String, List<String>> entry : headers.entrySet()) {
            String k = entry.getKey();
            List<String> v = entry.getValue();
            sbHead.append("------");
            sbHead.append(k);
            sbHead.append(":");
            String value = "";
            for (String e : v) {
                value = value + e;
            }
            sbHead.append(value);
            sbHead.append("\r\n");
        }
        return sbHead.toString();
    }

    /**
     * 取得http的uri
     * @param request http请求参数
     * @return uri
     */
    private static String getUrl(HttpServletRequest request) {
        return "---URI---" + "\r\n" + request.getRequestURI();
    }

    private static String getPost(JsonNode body) {
        if (body == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        sb.append("---Header---");
        sb.append("\r\n");
        for (Iterator<Map.Entry<String, JsonNode>> it = body.fields(); it.hasNext(); ) {
            Map.Entry<String, JsonNode> n = it.next();
            sb.append(n.getKey());
            sb.append(":");
            sb.append(n.getValue().asText());
            sb.append("\r\n");
        }

        return "---POST---" + "\r\n" + (body == null ? "" : sb.toString());
    }
}
