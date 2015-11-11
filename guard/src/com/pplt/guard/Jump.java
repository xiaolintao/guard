package com.pplt.guard;

import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.hipalsports.entity.FriendDetail;
import com.pplt.guard.chat.EMChatActivity;
import com.pplt.guard.personal.LoginActivity;

/**
 * Activity跳转。
 */
public class Jump {

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
	 * 跳转：主界面.
	 * 
	 * @param context
	 *            context.
	 */
	public static void toMain(Context context) {
		Intent intent = new Intent(context, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		context.startActivity(intent);
	}

	// ---------------------------------------------------- Broadcast
	/**
	 * 发送广播：登录。
	 * 
	 * @param context
	 *            context.
	 */
	public static void sendLoginBroadcast(Context context) {
		Intent intent = new Intent();
		intent.setAction(Global.ACTION_LOGIN);
		context.sendBroadcast(intent);
	}

	/**
	 * 发送广播：退出登录。
	 * 
	 * @param context
	 *            context.
	 */
	public static void sendLogoutBroadcast(Context context) {
		Intent intent = new Intent();
		intent.setAction(Global.ACTION_LOGOUT);
		context.sendBroadcast(intent);
	}

	// ---------------------------------------------------- Fragment
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

	// ---------------------------------------------------- Chat
	/**
	 * 跳转：聊天。
	 * 
	 * @param friend
	 *            好友信息。
	 */
	public static void toChat(Context context, FriendDetail friend) {
		Intent intent = new Intent(context, EMChatActivity.class);

		if (friend != null) {
			String data = JSON.toJSONString(friend);
			intent.putExtra(Global.EXTRA_FRIEND_DETAIL, data);
		}

		context.startActivity(intent);
	}
}
