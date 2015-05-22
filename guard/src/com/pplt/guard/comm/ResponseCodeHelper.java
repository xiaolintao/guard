package com.pplt.guard.comm;

import android.content.Context;

/**
 * Response code helper.
 */
public class ResponseCodeHelper {

	// ---------------------------------------------------- Constants
	/** 错误码对应的提示 */
	private static int[] CODE = new int[] {
	};

	/** 错误码 */
	public final static int CODE_ACCOUNT_ERROR = 10007; // 账号资料不完善

	/** HTTP status对应的提示 */
	private final static int[] STATUS = new int[] {
	};

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
		for (int i = 0; i < CODE.length / 2; i++) {
			if (code == CODE[2 * i]) {
				return context.getText(CODE[2 * i + 1]).toString();
			}
		}

		return "" + code;
	}

	/**
	 * 获取HTTP status提示。
	 * 
	 * @param context
	 *            context.
	 * @param status
	 *            状态。
	 * @return 提示。
	 */
	public static String getStatus(Context context, int status) {
		for (int i = 0; i < STATUS.length / 2; i++) {
			if (status == STATUS[2 * i]) {
				return context.getText(STATUS[2 * i + 1]).toString();
			}
		}

		return "" + status;
	}
}
