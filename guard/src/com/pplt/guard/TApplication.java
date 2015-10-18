package com.pplt.guard;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.DisplayMetrics;
import android.util.Log;

import com.jty.util.ToastHelper;
import com.kingdom.sdk.db.DBHelper;
import com.kingdom.sdk.ioc.IocContainer;
import com.pplt.guard.daemon.DaemonService;
import com.pplt.guard.entity.Contact;

public class TApplication extends Application {

	// ---------------------------------------------------- Private data
	final static String TAG = "Application";

	private BroadcastReceiver mReceiver; // 广播receiver

	// ---------------------------------------------------- Override methods
	@Override
	public void onCreate() {
		super.onCreate();

		// Global
		Global.init(this);
		Global.load();

		// Density
		DisplayMetrics dm = getResources().getDisplayMetrics();
		Log.d(TAG, "DisplayMetrics=" + dm);

		// IOC的初始化
		IocContainer.getShare().initApplication(this);

		// 图片缓存
		// BitmapCache.init(this);

		// database
		initDB();

		// daemon service
		startDaemon();

		// broadcast receiver
		registerBroadcastReceiver();
	}

	@Override
	public void onTerminate() {
		// daemon service
		stopDaemon();

		// broadcast receiver
		unregisterBroadcastReceiver();

		super.onTerminate();
	}


	// ---------------------------------------------------- Private methods
	/**
	 * initial database.
	 */
	private void initDB() {
		// tables
		Class<?>[] list = new Class<?>[] { Contact.class };
		DBHelper.addClass(list);

		// single instance
		DBHelper.setDatabaseName(Global.DB_FILENAME);
		DBHelper.createInstance(this);
	}

	// 启动daemon服务
	private void startDaemon() {
		Intent service = new Intent(this, DaemonService.class);
		startService(service);
	}

	// 停止daemon服务
	private void stopDaemon() {
		Intent service = new Intent(this, DaemonService.class);
		stopService(service);
	}

	/**
	 * register broadcast receiver.
	 */
	private void registerBroadcastReceiver() {
		if (mReceiver != null) {
			return;
		}

		mReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				ToastHelper.toast(getApplicationContext(), "网络异常");
			}
		};

		IntentFilter filter = new IntentFilter();
		filter.addAction(Global.ACTION_VOLLEY_ABNORMAL); // volley异常
		registerReceiver(mReceiver, filter);
	}

	/**
	 * unregister broadcast receiver.
	 */
	private void unregisterBroadcastReceiver() {
		if (mReceiver != null) {
			unregisterReceiver(mReceiver);
			mReceiver = null;
		}
	}
}
