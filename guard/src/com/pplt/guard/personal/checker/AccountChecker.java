package com.pplt.guard.personal.checker;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;

import com.jty.util.FormatHelper;
import com.jty.util.ToastHelper;
import com.pplt.guard.R;

/**
 * 检查输入：账号。
 * 
 */
public class AccountChecker {

	/**
	 * 检查输入：账号。
	 * 
	 * @param context
	 *            context.
	 * @param accountEt
	 *            输入框。
	 * @return 输入是否正确。
	 */
	public static boolean check(Context context, EditText accountEt) {
		String account = accountEt.getText().toString();

		if (TextUtils.isEmpty(account)) {
			ToastHelper.toast(context,
					R.string.personal_login_hint_input_account);
			return false;
		}
		if (!FormatHelper.isEmail(account) && !FormatHelper.isPhone(account)) {
			ToastHelper.toast(context,
					R.string.personal_login_hint_input_right_account);
			return false;
		}

		return true;
	}
}
