package com.pplt.guard.personal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.jty.util.FileHelper;
import com.jty.weixin.WeiXinHelper;
import com.kingdom.sdk.ioc.InjectUtil;
import com.pplt.guard.Global;
import com.pplt.guard.Jump;
import com.pplt.guard.R;
import com.pplt.guard.SuperScript;
import com.pplt.guard.personal.setting.SystemSettingFragment;
import com.pplt.ui.PreferenceItem;

/**
 * 个人：中心。
 */
public class PersonalCenterFragment extends Fragment implements OnClickListener {

	// ---------------------------------------------------- Private data
	private BroadcastReceiver mReceiver; // 广播receiver

	// ---------------------------------------------------- Override methods
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater
				.inflate(R.layout.personal_center_view, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// IOC
		InjectUtil.injectFragment(this, view);

		// initial views
		initViews();

		// broadcast receiver
		registerBroadcastReceiver();
	}

	@Override
	public void onResume() {
		super.onResume();

		// initial data
		initData();
	}

	@Override
	public void onDestroy() {
		// broadcast receiver
		unregisterBroadcastReceiver();

		super.onDestroy();
	}

	// ---------------------------------------------------- Private methods
	/**
	 * initial views.
	 */
	private void initViews() {
		// click listener
		setOnClickListener();
	}

	/**
	 * initial data.
	 */
	private void initData() {
		// 系统设置
		String text = SuperScript.hintSetting() ? "new" : null;
		SuperScript.showSub(getView(), R.id.item_system_setting, text);
	}

	/**
	 * set click listener.
	 */
	private void setOnClickListener() {
		int[] ids = new int[] { R.id.item_system_setting, R.id.item_weixin };

		for (int id : ids) {
			View view = getView().findViewById(id);
			if (view != null) {
				view.setOnClickListener(this);
			}
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.item_system_setting: // 系统设置
			setting();
			SuperScript.settingClicked();
			break;
		case R.id.item_weixin: // 微信
			weixin();
			break;
		default:
			break;
		}
	}

	/**
	 * 系统设置。
	 */
	private void setting() {
		Jump.toFragment(getActivity(), R.string.personal_item_setting,
				SystemSettingFragment.class.getName());
	}

	/**
	 * 微信。
	 */
	private void weixin() {
		String imagePath = Global.getRoot() + "/test.png";
		if (!FileHelper.isFileExists(imagePath)) {
			return;
		}

		WeiXinHelper helper = new WeiXinHelper(getActivity());
		helper.sendImage(imagePath, 179, 278);
	}

	// ---------------------------------------------------- broadcast receiver
	/**
	 * register broadcast receiver.
	 */
	private void registerBroadcastReceiver() {
		mReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals(Global.ACTION_NEW_VERSION)) {
					SuperScript.showSub(getView(), R.id.item_system_setting,
							"new");
				}
			}
		};

		IntentFilter filter = new IntentFilter();
		filter.addAction(Global.ACTION_NEW_VERSION); // 新版本
		getActivity().registerReceiver(mReceiver, filter);
	}

	/**
	 * unregister broadcast receiver.
	 */
	private void unregisterBroadcastReceiver() {
		if (mReceiver != null) {
			getActivity().unregisterReceiver(mReceiver);
			mReceiver = null;
		}
	}

	// ---------------------------------------------------- Private methods
	/**
	 * 设置item文本。
	 * 
	 * @param resId
	 *            item的resource id.
	 * @param text
	 *            文本。
	 */
	void setItemText(int resId, String text) {
		View view = getView().findViewById(resId);
		if (view != null && view instanceof PreferenceItem) {
			((PreferenceItem) view).setItemText(text);
		}
	}
}
