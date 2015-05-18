package com.pplt.guard.comm;

import java.util.ArrayList;
import java.util.List;


/**
 * HTTP URLs.
 */
public class HttpUrls extends HttpUrlBase {
	// ---------------------------------------------------- Private data
	// IP - 内网
	// private final static String IP = "http://" + "120.132.58.49"; // IP
	// private final static String BASE_URL = IP + "/stock/index.php";

	// IP - 外网
	private final static String IP = "http://" + "www.richba.com"; // IP
	private final static String BASE_URL = IP + "/index.php/home";

	// Module: user
	private final static String USER_URL = BASE_URL + "/user";
	public final static String URL_CHEKC_NAME = USER_URL + "/checkName"; // 检测用户名
	public final static String URL_CHECK_PHONE = USER_URL + "/checkPhone"; // 检测手机号码
	public final static String URL_VEFIFY_PHONE = USER_URL + "/verifyPhone"; // 获取手机验证码
	public final static String URL_VEFIFY_CODE = USER_URL + "/verifyCode"; // 获取图片验证码
	public final static String URL_LOGIN = USER_URL + "/doLogin"; // 登录
	public final static String URL_LOGOUT = USER_URL + "/doLogout"; // 登出
	public final static String URL_REGISTER = USER_URL + "/doRegister"; // 注册
	public final static String URL_USER_INFO = USER_URL + "/getUinfo"; // 获取登陆用户信息
	public final static String URL_USER_TAG = USER_URL + "/getUserTag"; // 获取用户标签
	public final static String URL_USER_PROFILE = USER_URL + "/doProfile"; // 完善用户资料

	// Module: password
	public final static String URL_RETRIEVE_PWD_VERIFY = USER_URL
			+ "/doVerifyPhone"; // 找回密码验证手机号
	public final static String URL_RETRIEVE_PWD_FIND = USER_URL
			+ "/doFindPasswd"; // 找回密码
	public final static String URL_CHANGE_PWD = USER_URL + "/changePasswd"; // 修改密码

	// Module: search
	private final static String SEARCH_URL = BASE_URL + "/search";
	private final static String URL_SEARCH = SEARCH_URL + "/sug"; // 联想词搜索

	// Module: stock
	private final static String STOCK_URL = BASE_URL + "/stock";
	private final static String URL_W8 = STOCK_URL + "/updownw8"; // 八周涨跌
	private final static String URL_TREND10 = STOCK_URL + "/trendd10"; // 10天走势

	// ---------------------------------------------------- Constructor
	private HttpUrls() {
	}

	// ---------------------------------------------------- user
	/**
	 * 检测用户名。
	 * 
	 * @param name
	 *            用户名。
	 * @return URL。
	 */
	public static String checkName(String name) {
		return URL_CHEKC_NAME + "?name=" + name;
	}

	/**
	 * 检测手机号码。
	 * 
	 * @param phoneNum
	 *            手机号码。
	 * @return URL。
	 */
	public static String checkPhone(String phoneNum) {
		return URL_CHECK_PHONE + "?phone_num=" + phoneNum;
	}

	// ---------------------------------------------------- search
	/**
	 * 联想词搜索.
	 * 
	 * @param key
	 *            关键字.
	 * @param page
	 *            页码.
	 * @param count
	 *            每页数量.
	 * @return URL。
	 */
	public static String search(String key, int page, int count) {
		List<String> params = new ArrayList<String>();

		addStringParam(params, "key", key);
		addIntParam(params, "page", page);
		addIntParam(params, "count", count);
		addIntParam(params, "client_type", 2); // 2 - android

		return URL_SEARCH + "?" + getParams(params.toArray(new String[] {}));
	}

	// ---------------------------------------------------- 股票
	/**
	 * 获取八周涨跌。
	 * 
	 * @param code
	 *            股票代码。
	 * @return URL。
	 */
	public static String getW8(String code) {
		return URL_W8 + "?code=" + code;
	}

	/**
	 * 获取10天走势。
	 * 
	 * @param code
	 *            股票代码。
	 * @return URL。
	 */
	public static String getTrend10(String code) {
		return URL_TREND10 + "?code=" + code;
	}
}
