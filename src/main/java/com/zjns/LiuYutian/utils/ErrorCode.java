package com.zjns.LiuYutian.utils;

public enum ErrorCode {
	OK(0, "Normal"), INVALID_ARGUMENTS(100, "Invalid arguments"), FORBIDDEN(403, "Forbidden"), NOT_FOUND(404, "Resource not found"), ServerException(500, "Server Exception"), ALIPAY_REFUND(901, "Ali pay refund need manual operation"), UNKNOWN(999, "Unknown error");
	
	public String msg;

	public int code;
	
	private ErrorCode(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	
	public String getMsg() {
		return this.msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public int getCode() {
		return this.code;
	}
	
	public void setCode(int code) {
		this.code = code;
	}
}
