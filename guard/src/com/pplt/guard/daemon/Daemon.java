package com.pplt.guard.daemon;

import android.content.Context;

/**
 * Daemon。
 * 
 * 1. 检查&下载新版本。
 */
public class Daemon {

	// ---------------------------------------------------- Private data
	private Context mContext; // context
	private PollingThread mThread; // 检查&下载新版本。

	// ---------------------------------------------------- Public methods
	/**
	 * Constructor.
	 * 
	 * @param context
	 *            context.
	 */
	public Daemon(Context context) {
		mContext = context;
	}

	/**
	 * start.
	 */
	public void start() {
		// start线程
		startThread();
	}

	/**
	 * stop.
	 */
	public void stop() {
		// stop线程
		stopThread();
	}

	/**
	 * 下载新版本。
	 */
	public void downloadNewVersion() {
		if (mThread != null) {
			mThread.downloadNewVersion();
		}
	}

	// ---------------------------------------------------- Private methods
	/**
	 * 启动线程。
	 */
	private void startThread() {
		if (mThread == null) {
			mThread = new PollingThread(mContext);
			mThread.start();
		}
	}

	/**
	 * 停止线程。
	 */
	private void stopThread() {
		if (mThread != null) {
			mThread.stop();
			mThread = null;
		}
	}
}
