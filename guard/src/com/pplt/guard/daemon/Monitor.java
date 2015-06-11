package com.pplt.guard.daemon;

import android.content.Context;
import android.content.Intent;
import android.os.FileObserver;
import android.util.Log;

import com.jty.util.FileHelper;
import com.pplt.guard.Global;
import com.pplt.guard.entity.GuardFile;
import com.pplt.guard.entity.GuardFileDataHelper;

/**
 * 监控：pplt文件.
 */
public class Monitor {

	// ---------------------------------------------------- Constants
	final static String TAG = "Monitor";

	// ---------------------------------------------------- Private data
	private Context mContext;

	private FileMonitor mFileMonitor;

	// ------------------------------------------ Constructor & Setting
	/**
	 * Constructor.
	 * 
	 * @param context
	 *            context.
	 */
	public Monitor(Context context) {
		mContext = context;
	}

	// ------------------------------------------ Public methods
	/**
	 * start thread.
	 */
	public void start() {
		Log.d(TAG, "start.");

		if (mFileMonitor != null) {
			return;
		}

		// String root = Environment.getExternalStorageDirectory().getPath();
		String root = Global.getRoot() + "/file";
		mFileMonitor = new FileMonitor(root, FileMonitor.MASK_CREATE_DELETE);
		mFileMonitor.setOnEventListener(new FileMonitor.OnEventListener() {

			@Override
			public void onEvent(int event, String path) {
				dealPPLT(event, path);
			}
		});
		mFileMonitor.startWatching();
	}

	/**
	 * stop thread.
	 */
	public void stop() {
		Log.d(TAG, "stop.");

		if (mFileMonitor != null) {
			mFileMonitor.stopWatching();
			mFileMonitor = null;
		}
	}

	// ------------------------------------------ Private methods
	/**
	 * 处理pplt文件。
	 */
	private void dealPPLT(int event, String path) {
		switch (event) {
		case FileObserver.CREATE:
		case FileObserver.MOVED_TO:
			add(path);
			break;
		case FileObserver.DELETE:
		case FileObserver.MOVED_FROM:
			GuardFileDataHelper.delete(path);
			break;
		}

		// 发送broadcast
		sendBroadcast();
	}

	/**
	 * pplt文件的信息=>database
	 * 
	 * @param path
	 *            文件路径。
	 */
	private void add(String path) {
		GuardFile file = new GuardFile();

		String name = FileHelper.getFilename(path);
		file.setFileName(name);
		file.setFilePath(path);
		file.setTimestamp(System.currentTimeMillis());

		GuardFileDataHelper.insert(file);
	}

	/**
	 * 发送broadcast.
	 */
	private void sendBroadcast() {
		Intent intent = new Intent();
		intent.setAction(Global.ACTION_PPLT_FILE);

		mContext.sendBroadcast(intent);
	}
}
