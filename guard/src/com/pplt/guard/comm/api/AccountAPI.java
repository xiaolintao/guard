package com.pplt.guard.comm.api;

import java.util.Locale;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Response;
import com.jty.util.FormatHelper;
import com.jty.util.volley.VolleyHelper;

/**
 * 通信：账号。
 */
public class AccountAPI extends BaseAPI {

	// ---------------------------------------------------- Public methods
	/**
	 * 注册。
	 * 
	 * @param context
	 *            context.
	 * @param account
	 *            账号。
	 * @param password
	 *            密码。
	 * @param captcha
	 *            验证码。
	 * @param nickName
	 *            昵称。
	 * @param listener
	 *            volley response listener.
	 */
	public static void register(Context context, String account,
			String password, String captcha, String nickName,
			Response.Listener<String> listener) {

		RequestParams params = new RequestParams();
		params.put("accountNumber", account);
		params.put("type", getAccountType(account));
		params.put("password", password);
		params.put("captcha", captcha);
		params.put("nickName", nickName);

		VolleyHelper.post(context, BASE_URL + "users/register",
				params.toString(), listener);
	}

	/**
	 * 登录.
	 * 
	 * @param context
	 *            context.
	 * @param account
	 *            账号。
	 * @param password
	 *            密码。
	 * @param listener
	 *            volley response listener.
	 */
	public static void login(Context context, String account, String password,
			Response.Listener<String> listener) {
		RequestParams params = new RequestParams();
		params.put("accountNumber", account);
		params.put("type", getAccountType(account));
		params.put("password", password);

		VolleyHelper.post(context, BASE_URL + "users/login", params.toString(),
				listener);
	}

	/**
	 * 第三方登录。
	 * 
	 * @param context
	 *            context.
	 * @param bindId
	 *            第三方的id.
	 * @param nickName
	 *            昵称。
	 * @param gender
	 *            性别：m - 男 w - 女。
	 * @param avatar
	 *            头像URL.
	 * @param listener
	 *            volley response listener.
	 */
	public static void thirdLogin(Context context, String bindId,
			String nickName, String gender, String avatar,
			Response.Listener<String> listener) {
		RequestParams params = new RequestParams();
		params.put("bindId", bindId);
		params.put("nickName", nickName);
		params.put("logoUrl", avatar);

		VolleyHelper.post(context, BASE_URL + "users/bindAccount",
				params.toString(), listener);
	}

	/**
	 * 验证账号。
	 * 
	 * @param context
	 *            context.
	 * @param account
	 *            账号。
	 * @param captcha
	 *            验证码。
	 * @param listener
	 *            volley response listener.
	 */
	public static void validateAccountNumber(Context context, String account,
			String captcha, Response.Listener<String> listener) {
		RequestParams params = new RequestParams();
		params.put("accountNumber", account);
		params.put("captcha", captcha);
		params.put("type", getAccountType(account));

		VolleyHelper.post(context, BASE_URL + "users/validateAccountNumber",
				params.toString(), listener);
	}

	/**
	 * 重设密码。
	 * 
	 * @param context
	 *            context.
	 * @param account
	 *            账号。
	 * @param captcha
	 *            验证码。
	 * @param password
	 *            新密码。
	 * @param listener
	 *            volley response listener.
	 */
	public static void resetPassword(Context context, String account,
			String captcha, String password, Response.Listener<String> listener) {
		RequestParams params = new RequestParams();
		params.put("accountNumber", account);
		params.put("captcha", captcha);
		params.put("password", password);
		params.put("type", getAccountType(account));

		VolleyHelper.post(context, BASE_URL + "users/resetPassword",
				params.toString(), listener);
	}

	/**
	 * 获取验证码。
	 * 
	 * @param conext
	 *            context.
	 * @param account
	 *            账号。
	 * @param purpose
	 * @see PurposeEnum。
	 * @param listener
	 *            volley response listener.
	 */
	public static void getCaptcha(Context context, String account, int purpose,
			Response.Listener<String> listener) {
		RequestParams params = new RequestParams();
		params.put("accountNumber", account);
		params.put("type", getAccountType(account));
		params.put("purpose", purpose);
		params.put("locale", getLocale());

		VolleyHelper.post(context, BASE_URL + "users/getCaptcha",
				params.toString(), listener);
	}

	// ---------------------------------------------------- Private methods
	/**
	 * 获取账号类型。
	 * 
	 * @param account
	 *            账号。
	 * @return 0 : email 1 : phone -1 : 其它。
	 */
	private static int getAccountType(String account) {
		if (TextUtils.isEmpty(account)) {
			return -1;
		}

		// email
		if (FormatHelper.isEmail(account)) {
			return 0;
		}

		// phone
		if (FormatHelper.isPhone(account)) {
			return 1;
		}

		return -1;
	}

	/**
	 * 获取locale : language +"_" + country.
	 * 
	 * @return locale.
	 */
	private static String getLocale() {
		String language = Locale.getDefault().getLanguage();
		String country = Locale.getDefault().getCountry();

		return language + "_" + country;
	}
}
