package com.pplt.guard.personal.setting;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.jty.util.VersionHelper;
import com.kingdom.sdk.ioc.InjectUtil;
import com.kingdom.sdk.ioc.annotation.InjectView;
import com.pplt.guard.BaseActivity;
import com.pplt.guard.SuperScript;
import com.pplt.guard.daemon.DaemonService;
import com.pplt.guard.daemon.DaemonService.DaemonBinder;
import com.pplt.guard.R;
import com.pplt.ui.DlgHelper;
import com.pplt.ui.PreferenceItem;

/**
 * 关于。
 */
public class About extends BaseActivity {

	// ---------------------------------------------------- Private data
	@InjectView(id = R.id.item_version, click = "version")
	private PreferenceItem mItemVersion; // 版本

	@InjectView(id = R.id.item_disclaimer, click = "disclaimer")
	private PreferenceItem mDisclaimer; // 免责

	private DaemonService mDaemonService;

	// ---------------------------------------------------- Override methods
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_view);

		// IOC
		InjectUtil.inject(this);

		// bind service
		bindDaemonService();
	}

	@Override
	protected void onResume() {
		super.onResume();

		// 显示角标
		showSuperScript();
	}

	@Override
	protected void onDestroy() {
		// unbind service
		unbindDaemonService();

		super.onDestroy();
	}

	// ---------------------------------------------------- Private methods
	/**
	 * 显示角标.
	 */
	private void showSuperScript() {
		// 是否有新版本
		String hint = SuperScript.hintVersion() ? "new" : null;
		SuperScript.showLabel(mItemVersion, hint);

		// 当前版本号
		String version = VersionHelper.getAppVersionName(this);
		String format = getText(R.string.version).toString();
		mItemVersion.setItemText(String.format(format, version));
	}

	// ---------------------------------------------------- Private methods
	/**
	 * 连接daemon service。
	 */
	private void bindDaemonService() {
		Intent intent = new Intent("com.jty.stock.daemon.DaemonService");
		bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
	}

	private ServiceConnection mServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mDaemonService = ((DaemonBinder) service).getService();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mDaemonService = null;
		}
	};

	/**
	 * 断开daemon service。
	 */
	private void unbindDaemonService() {
		if (mServiceConnection != null) {
			unbindService(mServiceConnection);

			mServiceConnection = null;
			mDaemonService = null;
		}
	}

	// ---------------------------------------------------- Private methods
	/**
	 * 版本。
	 */
	public void version() {
		SuperScript.versionClicked();
		showSuperScript();

		// check
		String apkPath = VersionHelper.checkNewVersion(this);
		if (apkPath == null) {
			DlgHelper.showAlertDialog(this, R.string.about_item_version,
					R.string.about_hint_version_newest);
			return;
		}

		CharSequence message = getText(R.string.about_hint_download_newversion);
		Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.about_item_version).setMessage(message);
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (mDaemonService != null) {
					mDaemonService.downNewVersion();
				}
			}
		});
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		builder.create().show();
	}

	/**
	 * 免责。
	 */
	public void disclaimer() {
		Intent intent = new Intent(this, Disclaimer.class);

		startActivity(intent);
	}
}
