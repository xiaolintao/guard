package com.pplt.ui;

import android.content.Context;
import android.content.res.Resources.Theme;
import android.util.TypedValue;

/**
 * Theme helper.
 * 
 */
public class ThemeHelper {

	// ---------------------------------------------------- Constructor
	private ThemeHelper() {
	}

	// ---------------------------------------------------- Public methods
	/**
	 * 获取属性值。
	 * 
	 * @param context
	 *            context.
	 * @param resId
	 *            属性的resource id.
	 * @return 属性值。
	 */
	public static int getAttribute(Context context, int resId) {
		Theme theme = context.getTheme();

		TypedValue typedValue = new TypedValue();
		theme.resolveAttribute(resId, typedValue, true);

		return typedValue.data;
	}

}
