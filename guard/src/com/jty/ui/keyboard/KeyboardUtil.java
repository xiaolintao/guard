package com.jty.ui.keyboard;

import java.lang.reflect.Method;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class KeyboardUtil {
	public static void showSoftKeyPad(View view, Context context) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		view.requestFocus();
		imm.showSoftInput(view, InputMethodManager.RESULT_SHOWN);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
				InputMethodManager.HIDE_IMPLICIT_ONLY);
	}
	public static void hideSoftKeypad(View view, Context context) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
	
	public static void forbidSoftKeypad(EditText editText, Context context) {
		int currentVersion = android.os.Build.VERSION.SDK_INT;
		String methodName = null;
		if (currentVersion >= 16) {
			// 4.2
			methodName = "setShowSoftInputOnFocus";
		} else if (currentVersion >= 14) {
			// 4.0
			methodName = "setSoftInputShownOnFocus";
		}

		if (methodName == null) {
			editText.setInputType(InputType.TYPE_NULL);
		} else {
			Class<EditText> cls = EditText.class;
			Method setShowSoftInputOnFocus;
			try {
				setShowSoftInputOnFocus = cls.getMethod(methodName,
						boolean.class);
				setShowSoftInputOnFocus.setAccessible(true);
				setShowSoftInputOnFocus.invoke(editText, false);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
