package com.pplt.guard.daemon;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * Daemon service.
 */
public class DaemonService extends Service {

	// ---------------------------------------------------- Constants
	private final static String TAG = "DaemonService";

	// ---------------------------------------------------- Binder
	public class DaemonBinder extends Binder {
		public DaemonService getService() {
			return DaemonService.this;
		}
	}

	// ---------------------------------------------------- Private data
	private final IBinder mBinder = new DaemonBinder(); // binder

	private PollingDaemon mPollingDaemon; // 轮询

	// ---------------------------------------------------- Override methods
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreate");

		start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");

		stop();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	// ---------------------------------------------------- Public methods
	/**
	 * 下载新版本。
	 */
	public void downNewVersion() {
		if (mPollingDaemon != null) {
			mPollingDaemon.downloadNewVersion();
		}
	}

	// ---------------------------------------------------- Private methods
	/**
	 * start.
	 */
	private void start() {
		// 轮询
		mPollingDaemon = new PollingDaemon(this);
		mPollingDaemon.start();
	}

	/**
	 * stop.
	 */
	private void stop() {
		// 轮询
		if (mPollingDaemon != null) {
			mPollingDaemon.stop();
			mPollingDaemon = null;
		}
	}
}
