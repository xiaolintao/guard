package com.pplt.chat;

import java.security.MessageDigest;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.os.AsyncTask;

import com.easemob.EMCallBack;
import com.easemob.EMError;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.exceptions.EaseMobException;

/**
 * 环信的helper.
 */
public class EMChatHelper {

	// ---------------------------------------------------- Public methods
	/**
	 * 初始化。
	 * 
	 * @param context
	 *            context.
	 */
	public static void init(Context context) {
		// 防止环信SDK被多次初始化
		int pid = android.os.Process.myPid();
		String processName = getProcessName(context, pid);
		String packageName = context.getPackageName();
		if (processName == null || !processName.equalsIgnoreCase(packageName)) {
			return;
		}

		EMChat.getInstance().init(context);

		// 代码混淆时，需设置成false
		EMChat.getInstance().setDebugMode(true);
	}

	public static String getPwd(int userId) {
		String pwd = "EMCHAT-" + userId;

		byte[] md5 = getMD5(pwd);
		return toHex(md5);
	}

	/**
	 * 注册。
	 * 
	 * @param callback
	 */
	public static void register(final String userId, final String pwd,
			final EMCallBack callback) {
		new AsyncTask<Void, Void, Integer>() {

			@Override
			protected Integer doInBackground(Void... params) {
				try {
					EMChatManager.getInstance().createAccountOnServer(userId,
							pwd);
				} catch (EaseMobException e) {
					int errorCode = e.getErrorCode();
					if (errorCode == EMError.USER_ALREADY_EXISTS) {
						return EMError.NO_ERROR;
					} else {
						return errorCode;
					}
				}

				return EMError.NO_ERROR;
			}

			@Override
			protected void onPostExecute(Integer result) {
				if (callback == null) {
					return;
				}

				if (result == 0) {
					callback.onSuccess();
				} else {
					callback.onError(result, "");
				}
			}

		}.execute();
	}

	// ---------------------------------------------------- Private methods
	/**
	 * 
	 * @param context
	 * @param pID
	 * @return
	 */
	private static String getProcessName(Context context, int pid) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);

		List<RunningAppProcessInfo> list = am.getRunningAppProcesses();
		for (RunningAppProcessInfo info : list) {
			if (info.pid == pid) {
				return info.processName;
			}
		}

		return null;
	}

	/**
	 * 计算MD5。
	 * 
	 * @param data
	 *            数据。
	 * @return MD5。
	 */
	private static byte[] getMD5(String data) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(data.getBytes("UTF-8"));
			return md5.digest();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 将byte数组转换成16进制形式的字符串。
	 * 
	 * @param data
	 *            byte数组.
	 * @return 16进制形式的字符串。
	 */
	private static String toHex(byte[] data) {
		StringBuffer buf = new StringBuffer();

		if (data == null) {
			return null;
		}

		for (byte element : data) {
			buf.append(String.format("%02x", element));
		}

		return buf.toString();
	}
}
