package com.pplt.guard.personal;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.jty.util.FormatHelper;
import com.jty.util.ToastHelper;
import com.kingdom.sdk.ioc.annotation.InjectView;
import com.pplt.guard.BaseActivity;
import com.pplt.guard.R;
import com.pplt.guard.comm.api.AccountAPI;
import com.pplt.guard.comm.response.ResponseCodeHelper;
import com.pplt.guard.comm.response.ResponseParser;

/**
 * 账号。
 */
public class VerifyCodeActivity extends BaseActivity {

	// ---------------------------------------------------- Constants
	/** 计时count */
	private final static int TIME_COUNT_PHONE = 3 * 60; // 手机
	private final static int TIME_COUNT_EMAIL = 10 * 60; // email

	// ---------------------------------------------------- Protected data
	@InjectView(id = R.id.et_account)
	protected EditText mAccountEt; // 账号：手机&邮箱

	@InjectView(id = R.id.tv_verify_code, click = "getVerifyCode")
	protected TextView mVerifyCodeTv; // 验证码：申请下发

	// ---------------------------------------------------- Private data
	private Timer mTimer; // 计时timer
	private int mCount; // 计时count

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			showTimeLeft();
		}
	};

	// ---------------------------------------------------- Protected methods
	/**
	 * 检查账号。
	 * 
	 * @return 账号格式是否正确。
	 */
	protected boolean checkAccount() {
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

	// ---------------------------------------------------- Private methods
	/**
	 * 获取验证码。
	 */
	protected void doGetVerifyCode(int purpose) {
		// 检查账号
		if (!checkAccount()) {
			return;
		}

		// 禁止点击
		forbidClick(mVerifyCodeTv, 3);

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
		AccountAPI.getCaptcha(this, account, purpose, listener);
	}

	/**
	 * 处理响应：获取验证码。
	 * 
	 * @param response
	 *            响应包。
	 */
	private void dealGetVerifyCode(String response) {
		// check result
		int code = ResponseParser.parseCode(response);

		// success
		if (code == 0) {
			hintVerifyCode();
			startCountTimer();
			return;
		}

		// fail
		String hint = ResponseCodeHelper.getHint(this, code);
		ToastHelper.toast(this, hint);
	}

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
	private void startCountTimer() {
		stopCountTimer();

		// 禁用按钮
		mVerifyCodeTv.setEnabled(false);

		// 设置计数值
		mCount = getTimeCount();

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

	/**
	 * 获取计时count.
	 * 
	 * @return count.
	 */
	private int  getTimeCount() {
		String account = mAccountEt.getText().toString();

		// email
		if (FormatHelper.isEmail(account)) {
			return TIME_COUNT_EMAIL;
		}

		// phone
		if (FormatHelper.isPhone(account)) {
			return TIME_COUNT_PHONE;
		}

		return TIME_COUNT_PHONE;
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
	 * 禁止点击。
	 * 
	 * @param view
	 *            view.
	 * @param seconds
	 *            禁止时间：单位- 秒。
	 */
	protected void forbidClick(final View view, int seconds) {
		view.setEnabled(false);

		view.postDelayed(new Runnable() {

			@Override
			public void run() {
				view.setEnabled(true);
			}
		}, seconds * 1000);

	}
}
