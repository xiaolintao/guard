package com.pplt.ui;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import com.pplt.guard.MainActivity;
import com.pplt.guard.R;

/**
 * 通知：helper.
 */
public class NotificationHelper {

	// ---------------------------------------------------- Constructor
	private NotificationHelper() {
	}

	// ---------------------------------------------------- Public methods
	/**
	 * 发送通知。
	 * 
	 * @param context
	 *            context.
	 * @param title
	 *            标题。
	 * @param text
	 *            内容。
	 * @param pendingIntent
	 *            pending intent.
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static void send(Context context, CharSequence title,
			CharSequence text, PendingIntent pendingIntent) {
		NotificationManager nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Notification notification = null;
		if (Build.VERSION.SDK_INT >= 11) {
			Notification.Builder builder = new Notification.Builder(context)
			.setContentIntent(pendingIntent)
			.setSmallIcon(R.drawable.ic_launcher)
			.setContentTitle(title).setContentText(text);
			if (Build.VERSION.SDK_INT >= 16) {
				notification = builder.build();
			} else {
				notification = builder.getNotification();
			}

		} else {
			notification = new Notification();
			notification.icon = R.drawable.ic_launcher;
			notification
			.setLatestEventInfo(context, title, text, pendingIntent);
		}

		nm.notify(0, notification);
	}

	/**
	 * 发送通知。
	 * 
	 * @param context
	 *            context.
	 * @param title
	 *            标题。
	 * @param text
	 *            内容。
	 */
	public static void send(Context context, CharSequence title,
			CharSequence text) {
		PendingIntent pendingIntent = getLaunchPendingIntent(context);

		send(context, title, text, pendingIntent);
	}

	/**
	 * 获取安装APK的pending intent.
	 * 
	 * @param context
	 *            context.
	 * @param apkPath
	 *            APK的路径。
	 * @return pending intent.
	 */
	public static PendingIntent getInstallPendingIntent(Context context,
			String apkPath) {
		File updateFile = new File(apkPath);
		Uri uri = Uri.fromFile(updateFile);

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(uri, "application/vnd.android.package-archive");

		return PendingIntent.getActivity(context, 0, intent, 0);
	}

	// ---------------------------------------------------- Private methods
	private static PendingIntent getLaunchPendingIntent(Context context) {
		Intent intent = new Intent(context, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);

		return pendingIntent;
	}
}
