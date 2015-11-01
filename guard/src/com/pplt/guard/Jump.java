package com.pplt.guard;

import android.content.Context;
import android.content.Intent;

import com.pplt.guard.personal.LoginActivity;

/**
 * Activity跳转。
 */
public class Jump {

	/**
	 * 跳转：主界面.
	 * 
	 * @param context
	 *            context.
	 */
	public static void toMain(Context context) {
		Intent intent = new Intent(context, MainActivity.class);

		context.startActivity(intent);
	}

	// ---------------------------------------------------- Account
	/**
	 * 跳转：登录。
	 * 
	 * @param context
	 *            context.
	 */
	public static void toLogin(Context context) {
		Intent intent = new Intent(context, LoginActivity.class);

		context.startActivity(intent);
	}

	/**
	 * 退出登录。
	 * 
	 * 1. MainActivity=>clear top.
	 * 
	 * 2. 给MainActivity发退出广播。
	 * 
	 * @param context
	 *            context.
	 */
	public static void logout(Context context) {
		// clear top
		Intent intentClearTop = new Intent(context, MainActivity.class);
		intentClearTop.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intentClearTop);

		// broadcast
		Intent intentLogout = new Intent();
		intentLogout.setAction(Global.ACTION_LOGOUT);
		context.sendBroadcast(intentLogout);
	}

	// ---------------------------------------------------- Contact

	// ---------------------------------------------------- Public methods
	/**
	 * 跳转：fragment.
	 * 
	 * @param titleResId
	 *            标题resource id.
	 * @param fname
	 *            fragment名称。
	 */
	public static void toFragment(Context context, int titleResId,
			String fname) {
		String title = context.getResources().getText(titleResId).toString();

		WrapperActivity.start(context, title, fname);
	}
}
