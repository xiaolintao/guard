package com.hipalsports.enums;

/**
 * 响应码。
 */
public enum ResponseCode {
	
	// parameter
	PARAMS_ERROR(100100, "输入参数有误"),

	// 账号
	ACCOUNT_EXISTS(100200, "账号已存在"),
	ACCOUNT_NOT_EXISTS(100201, "账号不存在"),
	
	// 验证码
	CAPTCHA_ERROR(100210, "验证码有误"),
	CAPTCHA_EXPIRED(100211, "验证码已过期"),
	SEND_CAPTCHA_EMAIL_FAIL(100212, "发送验证码到邮箱时出错"),
	SEND_CAPTCHA_PHONE_FAIL(100213, "发送验证码到手机时出错"),
	
	// 密码
	PASSWORD_ERROR(100220, "密码有误"),
	UPDATE_PASSWORD_FAIL(100221, "重设密码时出错"),
	
	// common
	UNKNOWN(-1, "未知错误"),
	OK(0,"");

	private final int value;
	private final String reasonPhrase;

	private ResponseCode(int value, String reasonPhrase) {
		this.value = value;
		this.reasonPhrase = reasonPhrase;
	}

	public int value() {
		return this.value;
	}
	
	public String reasonPhrase() {
		return this.reasonPhrase;
	}
	
	/**
	 * 获取响应码的说明。
	 * @param value 响应码。
	 * @return  说明。
	 */
	public static String reasonPhrase(int value) {
		ResponseCode[] responseCodes = ResponseCode.values();
		
		if (responseCodes != null) {
			for (ResponseCode responseCode : responseCodes) {
				if (responseCode.value == value) {
					return responseCode.reasonPhrase;
				}
			}
		}
		
		return UNKNOWN.reasonPhrase;
	}
}
