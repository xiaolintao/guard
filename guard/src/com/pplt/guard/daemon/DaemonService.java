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

	private Daemon mDaemon; // daemon

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
		if (mDaemon != null) {
			mDaemon.downloadNewVersion();
		}
	}

	// ---------------------------------------------------- Private methods
	/**
	 * start.
	 */
	private void start() {
		// daemon
		mDaemon = new Daemon(this);
		mDaemon.start();

	}

	/**
	 * stop.
	 */
	private void stop() {
		// daemon
		if (mDaemon != null) {
			mDaemon.stop();
			mDaemon = null;
		}
	}
}
