package com.zyc.zdh.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author zzw
 */
@Data
public class ResultInfo implements Serializable {
	private static final long serialVersionUID = 3807874205300200606L;
	// 返回的数据
	private Object data;
	/* 状态码
		20000: success;
		50008: Illegal token;
		50012: Other clients logged in;
		50014: Token expired;

	 */
	private int code;
	// response消息
	private String message;
	// 用于身份识别的token
	private String token;

	public enum Code {
		Success(20000),
		IllegalToken(50008),
		OtherClientsLoggedIn(50012),
		TokenExpired(50014);
		private final int value;
		Code(int value) {
			this.value = value;
		}
		public int getValue() {
			return this.value;
		}
	}
}
