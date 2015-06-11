package com.pplt.guard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jty.util.FragmentHelper;
import com.jty.util.ToastHelper;
import com.kingdom.sdk.ioc.InjectUtil;
import com.kingdom.sdk.ioc.annotation.InjectView;
import com.pplt.guard.contact.ContactFragment;
import com.pplt.guard.file.GuardFileFragment;
import com.pplt.guard.personal.PersonalFragment;
import com.pplt.ui.TabChange;
import com.pplt.ui.TabChange.OnTabChangedListener;

/**
 * 主页面。
 * 
 */
public class MainActivity extends BaseActivity {

	// ---------------------------------------------------- Constants
	private class Tab {
		public int titleResId; // 文字
		public int iconResId; // 图标
		public String fname; // fragment

		public Tab(int titleResId, int iconResId, String fname) {
			this.titleResId = titleResId;
			this.iconResId = iconResId;
			this.fname = fname;
		}
	}

	private final Tab[] TAB_FRAGMENT = new Tab[] {
			/** 密防 */
			new Tab(R.string.main_tab_gurad, R.drawable.mainbar_stock,
					GuardFileFragment.class.getName()),

			/** 联系人 */
					new Tab(R.string.main_tab_contact, R.drawable.mainbar_focus,
							ContactFragment.class.getName()),

			/** 个人 */
							new Tab(R.string.main_tab_me, R.drawable.mainbar_me,
									PersonalFragment.class.getName()) };

	// ---------------------------------------------------- Private data
	@InjectView(id = R.id.tab_change)
	private TabChange mTabChange;

	private BroadcastReceiver mReceiver; // 广播receiver
	private long firstTime = 0; // "第1次"按回退键的时间

	// ---------------------------------------------------- Override methods
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_view);

		// IOC
		InjectUtil.inject(this);

		// initial
		initViews();

		// broadcast receiver
		registerBroadcastReceiver();
	}

	@Override
	protected void onDestroy() {
		// broadcast receiver
		unregisterBroadcastReceiver();

		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		exit();
	}

	// ---------------------------------------------------- initial
	/**
	 * initial views.
	 */
	private void initViews() {
		LayoutInflater inflater = LayoutInflater.from(this);
		for (Tab tab : TAB_FRAGMENT) {
			View view = inflater.inflate(R.layout.main_tab_item, null);

			// icon
			View iconView = view.findViewById(R.id.tb_item);
			iconView.setBackgroundResource(tab.iconResId);

			// text
			TextView textView = (TextView) view.findViewById(R.id.tv_item);
			textView.setText(tab.titleResId);

			mTabChange.addTab(view);
		}
		mTabChange.setOnTabChangedListener(new OnTabChangedListener() {

			@Override
			public void onTabChanged(int position) {
				showTabFocusAtPosition(position, View.GONE);

				if (position != mTabChange.getCurrentTab()) {
					switchToTab(position);
				}
			}
		});

		// tab
		mTabChange.post(new Runnable() {

			@Override
			public void run() {
				mTabChange.setCurrentTab(0);
			}
		});
		switchToTab(0);
	}

	/**
	 * switch to tab.
	 * 
	 * @param index
	 *            tab index.
	 */
	private void switchToTab(int index) {
		if (index < 0 || index >= TAB_FRAGMENT.length) {
			return;
		}

		Tab tab = TAB_FRAGMENT[index];
		if (tab == null) {
			return;
		}

		if (tab.fname != null) {
			FragmentHelper.add(getSupportFragmentManager(), this, tab.fname,
					R.id.content_panel);

			Global.setMainLastTab(index);
		} else {
			FragmentHelper.removeAll(getSupportFragmentManager());
		}
	}

	// ---------------------------------------------------- broadcast receiver
	/**
	 * register broadcast receiver.
	 */
	private void registerBroadcastReceiver() {
		mReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				// 新版本
				if (intent.getAction().equals(Global.ACTION_NEW_VERSION)) {
					showTabFocus(R.string.main_tab_me);
					SuperScript.triggerNewVersion();
				}
			}
		};

		IntentFilter filter = new IntentFilter();
		filter.addAction(Global.ACTION_NEW_VERSION); // 新版本
		registerReceiver(mReceiver, filter);
	}

	/**
	 * 显示Tab的focus提示。
	 * 
	 * @param titleResId
	 *            Tab标题的resource id.
	 * 
	 */
	private void showTabFocus(int titleResId) {
		int tabIndex = getTabIndex(titleResId);
		if (tabIndex != -1) {
			showTabFocusAtPosition(tabIndex, View.VISIBLE);
		}
	}

	/**
	 * 显示Tab的focus提示。
	 * 
	 * @param positon
	 *            Tab的position.
	 * @param visibility
	 *            View.VISIBLE and so on.
	 */
	private void showTabFocusAtPosition(int positon, int visibility) {
		View view = mTabChange.getTabView(positon);
		if (view == null) {
			return;
		}

		TextView tv = (TextView) view.findViewById(R.id.tv_focus);
		tv.setVisibility(visibility);
	}

	/**
	 * 获取Tab的index。
	 * 
	 * @param titleResId
	 *            Tab文字的resource id.
	 */
	private int getTabIndex(int titleResId) {
		for (int i = 0; i < TAB_FRAGMENT.length; i++) {
			Tab tab = TAB_FRAGMENT[i];
			if (tab.titleResId == titleResId) {
				return i;
			}
		}

		return -1;
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

	// ---------------------------------------------------- exit
	/**
	 * 按两次（回退键）退出。
	 */
	private void exit() {
		long secondTime = System.currentTimeMillis();
		if (secondTime - firstTime > 2000) {
			ToastHelper.toast(this, R.string.press_twice_exit);
			firstTime = secondTime;
		} else {
			finish();
		}
	}
}
