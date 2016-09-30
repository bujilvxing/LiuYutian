package com.zjns.LiuYutian.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class LiuYutianResult {
	
	private LiuYutianResult() {
		
	}

	public static String getResult(ErrorCode errorCode, JsonNode data, String errorMsg) {
		
		String errorWithDefault = null;
		if(errorMsg != null) {
			errorWithDefault = errorMsg;
		} else {
			if(errorCode != ErrorCode.OK) {
				errorWithDefault = errorCode.getMsg();
			} else {
				errorWithDefault = null;
			}
		}
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put("timestamp", System.currentTimeMillis());
		node.put("code", errorCode.getCode());
		if(errorWithDefault != null) {
			node.put("error", errorWithDefault);
		}
		if(data != null)
			node.set("result", data);
		String content = "";
		try {
			content = mapper.writeValueAsString(node);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return content;
	}
	
	public static String ok(JsonNode data, String errorMsg) {
		return getResult(ErrorCode.OK, data, errorMsg);
	}
	
	public static String notFound(String errorMsg) {
		return getResult(ErrorCode.NOT_FOUND, null, errorMsg);
	}
	
	public static String unprocessable(ErrorCode errorCode, JsonNode data, String errorMsg) {
		return getResult(errorCode, data, errorMsg);
	}
	
	public static String forbidden(JsonNode data, String errorMsg) {
		return getResult(ErrorCode.FORBIDDEN, data, errorMsg);
	}
	
	public static String unprocessableWithMsg(String errorMsg) {
		return getResult(ErrorCode.INVALID_ARGUMENTS, null, errorMsg);
	}
	
	public static String conflict(String errorMsg) {
		return getResult(ErrorCode.UNKNOWN, null, errorMsg);
	}
	
	public static String serverException(String errorMsg) {
		return getResult(ErrorCode.ServerException, null, errorMsg);
	}
}
