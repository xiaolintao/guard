package com.pplt.guard;

import android.app.Application;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;

import com.kingdom.sdk.db.DBHelper;
import com.kingdom.sdk.ioc.IocContainer;
import com.pplt.guard.daemon.DaemonService;
import com.pplt.guard.entity.GuardFile;

public class TApplication extends Application {

	// ---------------------------------------------------- Private data
	final static String TAG = "Application";

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
		Intent service = new Intent(this, DaemonService.class);
		startService(service);
	}

	// ---------------------------------------------------- Private methods
	/**
	 * initial database.
	 */
	private void initDB() {
		// tables
		Class<?>[] list = new Class<?>[] { GuardFile.class };
		DBHelper.addClass(list);

		// single instance
		DBHelper.setDatabaseName(Global.DB_FILENAME);
		DBHelper.createInstance(this);
	}
}
