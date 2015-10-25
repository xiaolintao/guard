package com.pplt.guard.personal;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jty.util.FormatHelper;
import com.jty.util.ToastHelper;
import com.kingdom.sdk.ioc.InjectUtil;
import com.kingdom.sdk.ioc.annotation.InjectView;
import com.kingdom.sdk.net.http.ResponseEntity;
import com.pplt.guard.BaseActivity;
import com.pplt.guard.R;
import com.pplt.ui.TitleBar;

/**
 * 注册。
 */
public class RegisterActivity extends BaseActivity {

	// ---------------------------------------------------- Private data
	@InjectView(id = R.id.title_bar)
	private TitleBar mTitleBar;

	@InjectView(id = R.id.et_account)
	private EditText mAccountEt; // 手机

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
		// 检查账号
		if (!checkAccount()) {
			return;
		}
	}

	/**
	 * 检查账号。
	 * 
	 * @return 账号格式是否正确。
	 */
	private boolean checkAccount() {
		// 账号
		String account = mAccountEt.getText().toString();
		if (TextUtils.isEmpty(account)) {
			ToastHelper.toast(this, R.string.personal_login_hint_input_account);
			return false;
		}
		if (!FormatHelper.isEmail(account) && !FormatHelper.isPhone(account)) {
			ToastHelper.toast(this,
					R.string.personal_login_hint_input_right_account);
			return false;
		}

		return true;
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
		// 账号
		if (!checkAccount()) {
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
		String df = getText(R.string.personal_hint_time_left)
				.toString();
		String hint = String.format(df, mCount);
		mVerifyCodeTv.setText(hint);
	}

	private void stopTimer() {
		// 启用按钮
		mVerifyCodeTv.setEnabled(true);
		mVerifyCodeTv.setText(R.string.personal_get_verifycode);

		// 取消timer
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
	}
}
