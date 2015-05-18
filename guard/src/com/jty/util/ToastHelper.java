package com.jty.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast.
 */
public class ToastHelper {

	public static void toast(Context context, CharSequence text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	public static void toast(Context context, int resId) {
		Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
	}

}
