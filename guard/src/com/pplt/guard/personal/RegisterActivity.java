package com.pplt.guard.personal;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jty.util.ToastHelper;
import com.kingdom.sdk.ioc.InjectUtil;
import com.kingdom.sdk.ioc.annotation.InjectView;
import com.kingdom.sdk.net.http.ResponseEntity;
import com.pplt.guard.BaseActivity;
import com.pplt.guard.Global;
import com.pplt.guard.R;
import com.pplt.ui.TitleBar;

/**
 * 注册。
 */
public class RegisterActivity extends BaseActivity {

	// ---------------------------------------------------- Constants
	private final static int REQUEST_CODE_PROFILE = 101; // 完善用户资料

	// ---------------------------------------------------- Private data
	@InjectView(id = R.id.title_bar)
	private TitleBar mTitleBar;

	@InjectView(id = R.id.et_phone)
	private EditText mPhoneEt; // 手机号码

	@InjectView(id = R.id.et_pwd)
	private EditText mPwdEt; // 密码

	@InjectView(id = R.id.et_verify_code)
	private EditText mVerifyCodeEt; // 验证码：输入

	@InjectView(id = R.id.tv_verify_code)
	private TextView mVerifyCodeTv; // 验证码：申请下发

	@InjectView(id = R.id.tv_register)
	private TextView mRegisterTv; // 注册按钮

	private Timer mTimer;
	private int mCount;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			showTimeLeft();
		}
	};

	// ---------------------------------------------------- Override methods
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_register_view);

		// IOC
		InjectUtil.inject(this);

		// initial views
		initViews();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_PROFILE) {
			if (resultCode == RESULT_OK) {
				setResult(RESULT_OK);
				finish();
			}
		}
	}

	// ---------------------------------------------------- Private methods
	/**
	 * initial view.
	 */
	private void initViews() {
		// 验证码
		mVerifyCodeTv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				getVerifyCode();
			}
		});

		// register
		mRegisterTv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!checkInput()) {
					return;
				}

				register();
			}
		});
	}

	// ---------------------------------------------------- Private methods
	/**
	 * 获取手机验证码。
	 */
	private void getVerifyCode() {
		// 手机号码
		String phone = mPhoneEt.getText().toString();
		if (TextUtils.isEmpty(phone)) {
			ToastHelper.toast(this, R.string.personal_login_hint_input_account);
			return;
		}
	}

	/**
	 * 处理响应。
	 * 
	 * @param response
	 *            响应。
	 */
	void dealGetVerifyCode(ResponseEntity response) {
	}

	/**
	 * 检查输入。
	 * 
	 * @return 输入是否完整。
	 */
	private boolean checkInput() {
		// 手机号码
		String phone = mPhoneEt.getText().toString();
		if (TextUtils.isEmpty(phone)) {
			ToastHelper.toast(this, R.string.personal_login_hint_input_account);
			return false;
		}

		// 密码
		String pwd = mPwdEt.getText().toString();
		if (TextUtils.isEmpty(pwd)) {
			ToastHelper.toast(this, R.string.personal_login_hint_input_pwd);
			return false;
		}

		// 验证码
		String verifyCode = mVerifyCodeEt.getText().toString();
		if (TextUtils.isEmpty(verifyCode)) {
			ToastHelper.toast(this,
					R.string.personal_login_hint_input_verifycode);
			return false;
		}

		return true;
	}

	/**
	 * 注册。
	 */
	private void register() {
		// parameters
		// String phone = mPhoneEt.getText().toString();
		// String pwd = mPwdEt.getText().toString();
		// String verifyCode = mVerifyCodeEt.getText().toString();
	}

	/**
	 * 处理register响应。
	 * 
	 * @param response
	 *            响应。
	 */
	void dealRegister(ResponseEntity response) {

	}

	/**
	 * 完善资料。
	 * 
	 * @param uid
	 *            用户id.
	 */
	void profile(int uid) {
		String phone = mPhoneEt.getText().toString();

		Intent intent = new Intent(this, ProfileActivity.class);
		intent.putExtra(Global.EXTRA_UID, uid);
		intent.putExtra(Global.EXTRA_PHONE, phone);

		startActivityForResult(intent, REQUEST_CODE_PROFILE);
	}

	// ---------------------------------------------------- timer
	void startTimer() {
		stopTimer();

		// 禁用按钮
		mVerifyCodeTv.setEnabled(false);

		// 设置计数值
		mCount = 30;

		// 启动timer
		mTimer = new Timer(getPackageName(), false);
		mTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				mHandler.sendEmptyMessage(0);
			}
		}, 1000, 1000);
	}

	private void showTimeLeft() {
		mCount--;

		// 到时
		if (mCount == 0) {
			stopTimer();
			return;
		}

		// 未到时
		String df = getText(R.string.personal_register_hint_time_left)
				.toString();
		String hint = String.format(df, mCount);
		mVerifyCodeTv.setText(hint);
	}

	private void stopTimer() {
		// 启用按钮
		mVerifyCodeTv.setEnabled(true);
		mVerifyCodeTv.setText(R.string.personal_register_get_verifycode);

		// 取消timer
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
	}
}
