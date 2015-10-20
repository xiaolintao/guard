package com.pplt.guard.comm.api;

import android.content.Context;

import com.android.volley.Response;
import com.jty.util.volley.VolleyHelper;

/**
 * 通信：账号。
 */
public class AccountAPI extends BaseAPI {

	/**
	 * 登录.
	 * 
	 * @param context
	 *            context.
	 * @param account
	 *            账号。
	 * @param type
	 *            账号类型：0 - email 1 - phone.
	 * @param password
	 *            密码。
	 * @param listener
	 *            volley response listener.
	 */
	public static void login(Context context, String account, String type,
			String password, Response.Listener<String> listener) {
		RequestParams params = new RequestParams();
		params.put("accountNumber", account);
		params.put("type", type);
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
}
