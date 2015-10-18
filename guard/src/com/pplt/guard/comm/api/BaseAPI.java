package com.pplt.guard.comm.api;

/**
 * 通信：URL。
 */
public class BaseAPI {

	// ---------------------------------------------------- Constants
	/** 0 : 测试环境 1 : 生产环境 */
	static int environment = 0;

	// ---------------------------------------------------- Public data
	/** BASE URL */
	public final static String BASE_URL = getDefaultBaseUrl();

	// ---------------------------------------------------- Private methods
	/**
	 * 获取BASE URL.
	 */
	private static String getDefaultBaseUrl() {
		return "http://test2.hipalsports.com/HiPalServices/";
	}
}
