package com.pplt.guard.personal.checker;

import com.jty.util.ToastHelper;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;

/**
 * 检查输入。
 */
public class InputChecker {

	/**
	 * 检查输入：如果没输入内容，用toast显示hint。
	 * 
	 * @param context
	 *            context.
	 * @param editText
	 *            输入框。
	 * @return 是否已输入内容。
	 */
	public static boolean check(Context context, EditText editText) {
		String text = editText.getText().toString();
		if (TextUtils.isEmpty(text)) {
			CharSequence hint = editText.getHint();
			if (hint != null) {
				ToastHelper.toast(context, hint);
			}

			return false;
		}

		return true;
	}
}
