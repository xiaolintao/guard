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
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jty.util.VersionHelper;
import com.kingdom.sdk.ioc.InjectUtil;
import com.kingdom.sdk.ioc.annotation.InjectView;
import com.pplt.guard.Global;
import com.pplt.guard.Jump;
import com.pplt.guard.R;
import com.pplt.guard.SuperScript;
import com.pplt.guard.daemon.DaemonService;
import com.pplt.guard.daemon.DaemonService.DaemonBinder;
import com.pplt.guard.personal.pwd.ChangePwdActivity;
import com.pplt.ui.DlgHelper;
import com.pplt.ui.PreferenceItem;

/**
 * 系统设置。
 */
public class SystemSettingFragment extends Fragment {

	// ---------------------------------------------------- Private data
	@InjectView(id = R.id.item_change_pwd, click = "changePwd")
	private View mChangePwdPi; // 修改密码

	@InjectView(id = R.id.item_feedback, click = "")
	private View mFeedback; // 意见反馈

	@InjectView(id = R.id.item_version, click = "version")
	private View mVersion; // 版本

	@InjectView(id = R.id.tv_logout, click = "logout")
	private View mLogout; // 退出登录

	private DaemonService mDaemonService;

	// ---------------------------------------------------- Override methods
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.personal_setting_system, container,
				false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// IOC
		InjectUtil.injectFragment(this, view);

		// initial views
		initViews();

		// bind service
		bindDaemonService();
	}

	@Override
	public void onResume() {
		super.onResume();

		// 显示角标.
		showSuperScript();
	}

	@Override
	public void onDestroy() {
		// unbind service
		unbindDaemonService();

		super.onDestroy();
	}

	// ---------------------------------------------------- Private methods
	/**
	 * initial views.
	 */
	private void initViews() {
	}

	/**
	 * 显示角标.
	 */
	private void showSuperScript() {
		// 关于：是否有新版本
		String text = SuperScript.hintAbout() ? "new" : null;
		SuperScript.showLabelSub(getView(), R.id.item_version, text);

		// 关于：当前版本号
		String version = VersionHelper.getAppVersionName(getActivity());
		String format = getText(R.string.version).toString();
		setItemText(R.id.item_version, String.format(format, version));
	}

	// ---------------------------------------------------- Private methods
	/**
	 * 修改密码。
	 */
	public void changePwd() {
		Intent intent = new Intent(getActivity(), ChangePwdActivity.class);

		startActivity(intent);
	}

	/**
	 * 退出登录。
	 */
	public void logout() {
		// 用户信息
		Global.resetUser();

		// 跳转：主界面
		Jump.toMain(getActivity());
		Jump.sendLogoutBroadcast(getActivity());
	}

	// ---------------------------------------------------- Private methods
	/**
	 * 连接daemon service。
	 */
	private void bindDaemonService() {
		Intent intent = new Intent("com.jty.stock.daemon.DaemonService");
		getActivity().bindService(intent, mServiceConnection,
				Context.BIND_AUTO_CREATE);
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
			getActivity().unbindService(mServiceConnection);

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
		String apkPath = VersionHelper.checkNewVersion(getActivity());
		if (apkPath == null) {
			DlgHelper.showAlertDialog(getActivity(),
					R.string.about_item_version,
					R.string.about_hint_version_newest);
			return;
		}

		// hint message
		String apkVersion = VersionHelper.getApkVersionName(getActivity(),
				apkPath);
		String format = getText(R.string.about_hint_download_newversion).toString();
		String message = String.format(format, apkVersion);


		Builder builder = new AlertDialog.Builder(getActivity());
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

	// ---------------------------------------------------- Private methods
	void setItemText(int resId, int textResId) {
		View view = getView().findViewById(resId);
		if (view != null && view instanceof PreferenceItem) {
			((PreferenceItem) view).setItemText(textResId);
		}
	}

	void setItemText(int resId, String text) {
		View view = getView().findViewById(resId);
		if (view != null && view instanceof PreferenceItem) {
			((PreferenceItem) view).setItemText(text);
		}
	}
}
