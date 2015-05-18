package com.jty.util;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class ImeHeper {
	/**
	 * Constructor.
	 */
	private ImeHeper() {
	}
	
	/**
	 * 显示输入法。
	 * @param context context。
	 */
	public static void showIme(final Context context) {
    	Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

				if (imm != null) {
					imm.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
				}
			}

		}, 1000);
    }

	/**
	 * 隐藏输入法。
	 * @param context context。
	 * @param view view。
	 */
	public static void hideIme(Context context, View view) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
}
