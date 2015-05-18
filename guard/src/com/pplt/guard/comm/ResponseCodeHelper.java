package com.pplt.guard.comm;

import android.content.Context;

import com.kingdom.sdk.net.http.HttpUtils;
import com.pplt.guard.R;

/**
 * Response code helper.
 */
public class ResponseCodeHelper {

	// ---------------------------------------------------- Constants
	/** 错误码对应的提示 */
	private static int[] CODE = new int[] {
		// system
		10001, R.string.error_system,
		10002, R.string.error_service_unavailable,
		10003, R.string.error_ip_limit,
		10004, R.string.error_permisson_denied,
		10005, R.string.error_param,
		10006, R.string.error_login,
		10007, R.string.error_account,
		// user
		20001, R.string.error_phone_num,
		20002, R.string.error_illegal_user_name,
		20003, R.string.error_password,
		20004, R.string.error_verify_code,
		20005, R.string.error_send_sms_too_quick,
		20006, R.string.error_ip_limit,
		20007, R.string.error_send_sms,
		20008, R.string.error_sms_code_expire,
		20009, R.string.error_sms_code,
		20010, R.string.error_request_data,
		20011, R.string.error_insert_data,
		20012, R.string.error_empty_user_name,
		20013, R.string.error_registered_user_name,
		20014, R.string.error_illegal_user_name,
		20016, R.string.error_system_user_name,
		20017, R.string.error_phone_code,
		20018, R.string.error_phone_num,
		20019, R.string.error_bad_user_name,
		// login
		20100, R.string.error_user_name,
		20101, R.string.error_password_login,
		20102, R.string.error_verify_code_login,
		20103, R.string.error_empty_name_or_password,
		20104, R.string.error_user_not_exists,
		20105, R.string.error_user_locked,
		// 帖子
		20200, R.string.error_have_fav,
		20201, R.string.error_have_no_fav,
		20202, R.string.error_have_recom,
		20203, R.string.error_postid,
		20204, R.string.error_post_content_too_long,
		20205, R.string.error_common_to_long,
		20206, R.string.error_post_content_empty,
		20207, R.string.error_comment_empty,
		20208, R.string.error_at_user_too_many,
		20209, R.string.error_at_theme_too_mang,
		20210, R.string.error_transmit_content_too_long,
		// 关注
		20300, R.string.error_user_cannot_follow,
		20301, R.string.error_have_followed,
		20302, R.string.error_have_not_followed,
		// 资讯管理模块
		20400, R.string.error_no_data_change,
		// 搜索模块
		20500, R.string.error_param_empty
	};

	/** 错误码 */
	public final static int CODE_ACCOUNT_ERROR = 10007; // 账号资料不完善

	/** HTTP status对应的提示 */
	private final static int[] STATUS = new int[] {
		HttpUtils.RESULT_NET_ERROR,     R.string.status_net_error,
		HttpUtils.RESULT_SERVER_ERROR,  R.string.status_server_error,
		HttpUtils.RESULT_TIMEOUT,       R.string.status_time_out
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

		return context.getText(R.string.error_unknown).toString() + code;
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

		return context.getText(R.string.error_unknown).toString() + status;
	}
}
