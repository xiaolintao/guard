package com.pplt.guard.personal.pwd;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.hipalsports.entity.UserInfo;
import com.jty.util.FormatHelper;
import com.jty.util.ToastHelper;
import com.kingdom.sdk.ioc.InjectUtil;
import com.kingdom.sdk.ioc.annotation.InjectView;
import com.pplt.guard.BaseActivity;
import com.pplt.guard.Global;
import com.pplt.guard.R;
import com.pplt.guard.comm.api.AccountAPI;
import com.pplt.guard.comm.response.ResponseCodeHelper;
import com.pplt.guard.comm.response.ResponseParser;
import com.pplt.ui.TitleBar;

/**
 * 修改密码。
 */
public class ChangePwdActivity extends BaseActivity {

	// ---------------------------------------------------- Private data
	@InjectView(id = R.id.title_bar)
	private TitleBar mTitleBar;

	@InjectView(id = R.id.et_account)
	private EditText mAccountEt; // 账号：手机&邮箱

	@InjectView(id = R.id.et_pwd)
	private EditText mPwdEt; // 密码

	@InjectView(id = R.id.et_verify_code)
	private EditText mVerifyCodeEt; // 验证码：输入

	@InjectView(id = R.id.tv_verify_code, click = "getVerifyCode")
	private TextView mVerifyCodeTv; // 验证码：申请下发

	@InjectView(id = R.id.tv_change_pwd, click = "changePwd")
	private TextView mChangePwdTv; // 修改密码按钮

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
		setContentView(R.layout.personal_change_pwd_view);

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
		// account
		initAccount();
	}

	/**
	 * initial account.
	 */
	private void initAccount() {
		UserInfo user = Global.getUser();
		if (user == null) {
			return;
		}

		// 手机号码
		String phone = user.getPhone();
		if (!TextUtils.isEmpty(phone)) {
			mAccountEt.setText(phone);
			return;
		}

		// email
		String email = user.getEmail();
		if (!TextUtils.isEmpty(email)) {
			mAccountEt.setText(email);
			return;
		}
	}

	// ---------------------------------------------------- Private methods
	/**
	 * 获取验证码。
	 */
	public void getVerifyCode() {
		// 检查账号
		if (!checkAccount()) {
			return;
		}

		// listener
		Response.Listener<String> listener = new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				if (isFinishing() || response == null) {
					return;
				}

				dealGetVerifyCode(response);
			}
		};

		// request
		String account = mAccountEt.getText().toString();
		AccountAPI.getCaptcha(this, account, listener);
	}

	/**
	 * 处理响应：获取验证码。
	 * 
	 * @param response
	 *            响应包。
	 */
	private void dealGetVerifyCode(String response) {
		// check result
		boolean result = ResponseParser.parseResult(response);

		// success
		if (result) {
			hintVerifyCode();
			startCountTimer(60 * 10);
			return;
		}

		// fail
		int code = ResponseParser.parseCode(response);
		String hint = ResponseCodeHelper.getHint(this, code);
		ToastHelper.toast(this, hint);
	}

	/**
	 * 修改密码。
	 */
	public void changePwd() {
		// 检查输入
		if (!checkInput()) {
			return;
		}

		// listener
		Response.Listener<String> listener = new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				if (isFinishing() || response == null) {
					return;
				}

				dealChangePwd(response);
			}
		};

		// request
		String account = mAccountEt.getText().toString();
		String pwd = mPwdEt.getText().toString();
		String verifyCode = mVerifyCodeEt.getText().toString();
		AccountAPI.rePassword(this, account, verifyCode, pwd, listener);
	}

	/**
	 * 处理响应：修改密码。
	 * 
	 * @param response
	 *            响应包。
	 */
	private void dealChangePwd(String response) {
		// check result
		boolean result = ResponseParser.parseResult(response);

		// success
		if (result) {
			ToastHelper.toast(this, R.string.personal_retrieve_pwd_changed);
			finish();
			return;
		}

		// fail
		int code = ResponseParser.parseCode(response);
		String hint = ResponseCodeHelper.getHint(this, code);
		ToastHelper.toast(this, hint);
	}

	// ---------------------------------------------------- Check input
	/**
	 * 检查账号。
	 * 
	 * @return 账号格式是否正确。
	 */
	private boolean checkAccount() {
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

	// ---------------------------------------------------- timer
	/**
	 * 验证码发送提示。
	 */
	private void hintVerifyCode() {
		String account = mAccountEt.getText().toString();

		// email
		if (FormatHelper.isEmail(account)) {
			ToastHelper.toast(this,
					R.string.personal_hint_verifycode_sended_email);
		}

		// phone
		if (FormatHelper.isPhone(account)) {
			ToastHelper.toast(this,
					R.string.personal_hint_verifycode_sended_phone);
		}
	}

	/**
	 * 启动记时timer.
	 * 
	 * @param count
	 *            时长(单位：秒)。
	 */
	void startCountTimer(int count) {
		stopCountTimer();

		// 禁用按钮
		mVerifyCodeTv.setEnabled(false);

		// 设置计数值
		mCount = count;

		// 启动timer
		mTimer = new Timer(getPackageName(), false);
		mTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				mHandler.sendEmptyMessage(0);
			}
		}, 1000, 1000);
	}

	/**
	 * 显示剩余计时时间。
	 */
	private void showTimeLeft() {
		mCount--;

		// 到时
		if (mCount == 0) {
			stopCountTimer();
			return;
		}

		// 未到时
		String df = getText(R.string.personal_hint_time_left).toString();
		String hint = String.format(df, mCount);
		mVerifyCodeTv.setText(hint);
	}

	/**
	 * 停止计时timer.
	 */
	private void stopCountTimer() {
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
