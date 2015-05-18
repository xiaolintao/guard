package com.pplt.guard.daemon;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.jty.util.VersionHelper;
import com.pplt.guard.Global;
import com.pplt.guard.R;
import com.pplt.ui.NotificationHelper;

/**
 * 轮询thread.
 */
public class PollingThread implements Runnable {

	// ---------------------------------------------------- Constants
	final static String TAG = "PollingThread";
	final static int INTERVAL = 3 * 1000;

	// ---------------------------------------------------- Private data
	private Context mContext;

	private ExecutorService mExecutor = Executors.newSingleThreadExecutor(); // executor

	private boolean mDownload = false;

	// ------------------------------------------ Constructor & Setting
	/**
	 * Constructor.
	 * 
	 * @param context
	 *            context.
	 */
	public PollingThread(Context context) {
		mContext = context;
	}

	/**
	 * start thread.
	 */
	public void start() {
		Log.d(TAG, "start.");

		mExecutor = Executors.newSingleThreadExecutor();
		mExecutor.execute(new Thread(this));
	}

	/**
	 * stop thread.
	 */
	public void stop() {
		Log.d(TAG, "stop.");

		mExecutor.shutdownNow();
	}

	/**
	 * 下载新版本。
	 */
	public synchronized void downloadNewVersion() {
		mDownload = true;

		notifyAll();
	}

	// ---------------------------------------------------- Runnable
	@Override
	public void run() {

		while (true) {
			try {
				Log.d(TAG, "begin...");

				// 检测新版本
				checkNewVersion();

				// 下载APK
				if (mDownload) {
					downloadApk();
					mDownload = false;
				}

				Thread.sleep(INTERVAL);
			} catch (InterruptedException e) {
				Log.d(TAG, "exit.");
				break;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// ---------------------------------------------------- Private methods
	/**
	 * 检测新版本。
	 */
	private void checkNewVersion() {
		// check
		String apkPath = VersionHelper.checkNewVersion(mContext);
		if (apkPath == null) {
			return;
		}

		// version code
		int apkVersionCode = VersionHelper.getApkVersionCode(mContext, apkPath);
		if (apkVersionCode <= Global.getNewVersionCode()) {
			return;
		}
		Global.setNewVersionCode(apkVersionCode);

		// 发送broadcast
		sendBroadcast();
	}

	/**
	 * 下载APK。
	 */
	private void downloadApk() {
		// check
		String apkPath = VersionHelper.checkNewVersion(mContext);
		if (apkPath == null) {
			return;
		}

		// 下载APK
		downloadApk(apkPath);
	}

	/**
	 * 下载APK.
	 */
	private void downloadApk(String apkPath) {
		CharSequence title = mContext.getText(R.string.version_downloading);
		for (int i = 0; i < 30; i++) {
			NotificationHelper.send(mContext, title, String.valueOf(i * 3)
					+ "%");

			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		PendingIntent pendingIntent = NotificationHelper
				.getInstallPendingIntent(mContext, apkPath);
		title = mContext.getText(R.string.version_downloaded);
		CharSequence text = mContext.getText(R.string.version_install);
		NotificationHelper.send(mContext, title, text, pendingIntent);
	}

	/**
	 * 发送broadcast: 新版本.
	 * 
	 */
	private void sendBroadcast() {
		Intent intent = new Intent();
		intent.setAction(Global.ACTION_NEW_VERSION);

		mContext.sendBroadcast(intent);
	}
}
