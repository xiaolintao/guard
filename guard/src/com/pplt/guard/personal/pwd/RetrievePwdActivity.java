package com.pplt.guard.personal.pwd;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;

import com.jty.util.FragmentHelper;
import com.kingdom.sdk.ioc.InjectUtil;
import com.kingdom.sdk.ioc.annotation.InjectView;
import com.pplt.guard.BaseActivity;
import com.pplt.guard.R;
import com.pplt.ui.TitleBar;

/**
 * 找回密码。
 */
public class RetrievePwdActivity extends BaseActivity {

	// ---------------------------------------------------- Constants
	/** Action */
	private static final String ACTION_RETRIEVE_PWD_STEP = "com.jty.stock.retrievepwd";

	/** Extra */
	private static final String EXTRA_STEP = "step";
	private static final String EXTRA_PHONE = "phone";

	// ---------------------------------------------------- Private data
	@InjectView(id = R.id.title_bar)
	private TitleBar mTitleBar;

	private BroadcastReceiver mReceiver; // 广播receiver

	// ---------------------------------------------------- Override methods
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_retrieve_pwd_view);

		// IOC
		InjectUtil.inject(this);

		// step1
		toStep1();

		// broadcast receiver
		registerBroadcastReceiver();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// broadcast receiver
		unregisterNoteBroadcastReceiver();
	}

	/**
	 * 发送广播.
	 * 
	 * @param context
	 *            context.
	 * @param step
	 *            步骤.
	 * @param phone
	 *            手机号码.
	 */
	public static void sendBroadcast(Context context, int step, String phone) {
		Intent intent = new Intent();
		intent.setAction(ACTION_RETRIEVE_PWD_STEP);

		intent.putExtra(EXTRA_STEP, step);
		if (!TextUtils.isEmpty(phone)) {
			intent.putExtra(EXTRA_PHONE, phone);
		}

		if (context != null) {
			context.sendBroadcast(intent);
		}
	}

	// ---------------------------------------------------- Private methods
	/**
	 * 步骤切换。
	 * 
	 * @param step
	 *            步骤。
	 * @param intent
	 *            broadcast数据。
	 */
	private void switchTo(int step, Intent intent) {
		switch (step) {
		case 1:
			toStep1();
			break;
		case 2:
			String phone = intent.getStringExtra(EXTRA_PHONE);
			toStep2(phone);
			break;
		case 3:
			toStep3();
			break;
		default:
			break;
		}
	}

	/**
	 * switch to step1.
	 */
	private void toStep1() {
		RetrievePwdStep1Fragment fragment = new RetrievePwdStep1Fragment();

		FragmentHelper.add(getSupportFragmentManager(), fragment,
				R.id.content_panel, false);
	}

	/**
	 * switch to step2.
	 * 
	 * @param phone
	 *            手机号码。
	 */
	private void toStep2(String phone) {
		RetrievePwdStep2Fragment fragment = new RetrievePwdStep2Fragment();
		fragment.setPhone(phone);

		FragmentHelper.add(getSupportFragmentManager(), fragment,
				R.id.content_panel, false);
	}

	/**
	 * switch to step3.
	 */
	private void toStep3() {
		RetrievePwdStep3Fragment fragment = new RetrievePwdStep3Fragment();

		FragmentHelper.add(getSupportFragmentManager(), fragment,
				R.id.content_panel, false);
	}

	// ---------------------------------------------------- Private methods
	/**
	 * register broadcast receiver.
	 */
	private void registerBroadcastReceiver() {
		mReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				if (!intent.hasExtra(EXTRA_STEP)) {
					return;
				}

				int step = intent.getIntExtra(EXTRA_STEP, 1);
				switchTo(step, intent);
			}
		};

		IntentFilter intentFilter = new IntentFilter(ACTION_RETRIEVE_PWD_STEP);
		registerReceiver(mReceiver, intentFilter);
	}

	/**
	 * unregister note broadcast receiver.
	 */
	private void unregisterNoteBroadcastReceiver() {
		if (mReceiver != null) {
			unregisterReceiver(mReceiver);
			mReceiver = null;
		}
	}
}
