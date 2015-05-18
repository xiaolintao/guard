package com.jty.util;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.os.Build;
import android.view.WindowManager;

/**
 * Helper of system.
 */
public class SystemHelper {
	/**
	 * 获取屏幕宽度。
	 * 
	 * @param wm
	 *            window manager.
	 * @return 宽度。
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static int getDisplayWidth(WindowManager wm) {

		if (Build.VERSION.SDK_INT >= 13) {
			Point size = new Point();
			wm.getDefaultDisplay().getSize(size);
			return size.x;
		} else {
			return wm.getDefaultDisplay().getWidth();
		}
	}
}
