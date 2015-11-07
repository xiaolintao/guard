package com.pplt.guard.comm.api;

import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response;
import com.hipalsports.enums.UserEnum;
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
	 * @param bindPlatform
	 *            第三方平台名称。
	 * @param bindId
	 *            第三方账号id.
	 * @param nickName
	 *            昵称。
	 * @param gender
	 * @see GenderEnum。
	 * @param avatar
	 *            头像URL.
	 * @param listener
	 *            volley response listener.
	 */
	public static void thirdLogin(Context context, String bindPlatform,
			String bindId, String nickName, int gender, String avatar,
			Response.Listener<String> listener) {
		RequestParams params = new RequestParams();
		params.put("bindPlatform", bindPlatform);
		params.put("bindId", bindId);
		params.put("nickName", nickName);
		params.put("gender", gender);
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
	 * 修改密码。
	 * 
	 * @param context
	 *            context.
	 * @param account
	 *            账号。
	 * @param oldPassword
	 *            验证码。
	 * @param newPassword
	 *            新密码。
	 * @param listener
	 *            volley response listener.
	 */
	public static void changePassword(Context context, String account,
			String oldPassword, String newPassword,
			Response.Listener<String> listener) {
		RequestParams params = new RequestParams();
		params.put("accountNumber", account);
		params.put("oldPassword", oldPassword);
		params.put("newPassword", newPassword);
		params.put("type", getAccountType(account));

		VolleyHelper.post(context, BASE_URL + "users/changePassword",
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

	/**
	 * 更新用户信息：此接口不能更新phone&email&password。
	 * 
	 * @param conext
	 *            context.
	 * @param userId
	 *            用户id。
	 * @param params
	 *            需更新的字段：key - 字段名 value - 字段值。
	 * @param listener
	 *            volley response listener.
	 */
	public static void update(Context context, int userId,
			Map<String, Object> params, Response.Listener<String> listener) {
		RequestParams requestParams = new RequestParams();
		params.put("userId", userId);

		String json = JSON.toJSONString(params);
		params.put("params", json);

		VolleyHelper.post(context, BASE_URL + "users/update",
				requestParams.toString(), listener);
	}

	/**
	 * 更新账号。
	 * 
	 * @param context
	 *            context.
	 * @param userId
	 *            用户id.
	 * @param account
	 *            账号。
	 * @param captcha
	 *            验证码。
	 * @param listener
	 *            volley response listener.
	 */
	public static void updateAccountNumber(Context context, int userId,
			String account, String captcha, Response.Listener<String> listener) {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("accountNumber", account);
		params.put("captcha", captcha);
		params.put("type", getAccountType(account));

		VolleyHelper.post(context, BASE_URL + "users/updateAccountNumber",
				params.toString(), listener);
	}

	/**
	 * 查询。
	 * 
	 * @param context
	 *            context.
	 * @param key
	 *            查询条件：匹配phone/email/nickname。
	 * @param offset
	 *            offset.
	 * @param length
	 *            length.
	 * @param listener
	 *            volley response listener.
	 */
	public static void search(Context context, String key, int offset,
			int length, Response.Listener<String> listener) {
		RequestParams params = new RequestParams();
		params.put("key", key);
		params.put("offset", offset);
		params.put("length", length);

		VolleyHelper.post(context, BASE_URL + "users/search",
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
			return UserEnum.UNKNOWN.value();
		}

		// email
		if (FormatHelper.isEmail(account)) {
			return UserEnum.EMAIL.value();
		}

		// phone
		if (FormatHelper.isPhone(account)) {
			return UserEnum.PHONE.value();
		}

		return UserEnum.UNKNOWN.value();
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
