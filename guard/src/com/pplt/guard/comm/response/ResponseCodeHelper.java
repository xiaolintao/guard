package com.pplt.guard.comm.response;

import android.content.Context;
import android.text.TextUtils;

import com.hipalsports.enums.ResponseCode;
import com.pplt.guard.R;

/**
 * Response code helper.
 */
public class ResponseCodeHelper {

	// ---------------------------------------------------- Constructor
	private ResponseCodeHelper() {
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
		String reasonPhrase = ResponseCode.reasonPhrase(code);
		if (!TextUtils.isEmpty(reasonPhrase)) {
			return reasonPhrase;
		}

		CharSequence unknown = context.getText(R.string.error_unkonwn);
		return unknown + " :" + code;
	}
}
