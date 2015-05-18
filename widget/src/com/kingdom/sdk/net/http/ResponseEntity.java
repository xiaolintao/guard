package com.kingdom.sdk.net.http;

/**
 * 网络响应实体类
 * @author Administrator
 *
 */
public class ResponseEntity{

	/**
	 * 网络响应状态
	 * 0：网络响应成功
	 * 1：网络响应失败
	 */
	private int status ;
	/**
	 * 请求协议号
	 */
	private int command;
	/**
	 * 请求地址
	 */
	private String url;
	/**
	 * 网络响应内容
	 */
	private String content;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getUrl() {
		return url;
	}

	public ResponseEntity setUrl(String url) {
		this.url = url;
		return this;
	}

	public int getCommand() {
		return command;
	}

	public void setCommand(int command) {
		this.command = command;
	}

	public String getContentAsString() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
