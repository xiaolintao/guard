package com.jty.util.volley;

import android.content.Context;

import com.pplt.guard.R;

/**
 * volley响应码。
 */
public class VolleyCodeHelper {

	// ---------------------------------------------------- Constants
	/** 错误码 */
	private final static int[] codes = { 
		VolleyHelper.RESULT_UNKNOWN_ERROR,	R.string.error_unkonwn,
		VolleyHelper.RESULT_NET_ERROR,			R.string.volley_net_error,
		VolleyHelper.RESULT_SERVER_ERROR,		R.string.volley_server_error,
		VolleyHelper.RESULT_AUTH_ERROR,			R.string.volley_auth_error, 
		VolleyHelper.RESULT_TIMEOUT,				R.string.volley_timeout };

	// ---------------------------------------------------- Constructor
	private VolleyCodeHelper() {
	}

	// ---------------------------------------------------- Public methods
	/**
	 * 获取错误提示。
	 * 
	 * @param context
	 *            context.
	 * @param code
	 *            错误码。
	 * @return 错误提示。
	 */
	public static String getHint(Context context, int code) {
		for (int i = 0; i < codes.length / 2; i++) {
			if (code == codes[2 * i]) {
				return context.getText(codes[2 * i + 1]).toString();
			}
		}

		CharSequence unknown = context.getText(R.string.error_unkonwn);
		return unknown + " :" + code;
	}
}
