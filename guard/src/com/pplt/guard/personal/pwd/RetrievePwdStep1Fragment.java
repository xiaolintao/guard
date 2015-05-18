package com.pplt.guard.personal.pwd;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.jty.util.FileHelper;
import com.jty.util.ToastHelper;
import com.kingdom.sdk.ioc.InjectUtil;
import com.kingdom.sdk.ioc.annotation.InjectView;
import com.kingdom.sdk.net.http.HttpUtils;
import com.kingdom.sdk.net.http.IHttpResponeListener;
import com.kingdom.sdk.net.http.ResponseEntity;
import com.pplt.guard.comm.HttpParameters;
import com.pplt.guard.comm.HttpUrls;
import com.pplt.guard.comm.ResponseCodeHelper;
import com.pplt.guard.comm.ResponseParser;
import com.pplt.guard.R;

/**
 * 找回密码：第一步。
 */
public class RetrievePwdStep1Fragment extends Fragment {

	// ---------------------------------------------------- Private data
	@InjectView(id = R.id.et_phone)
	private EditText mPhoneEt; // 手机号码

	@InjectView(id = R.id.et_verify_code)
	private EditText mVerifyCodeEt; // 验证码：输入

	@InjectView(id = R.id.tv_verify_code)
	private TextView mVerifyCodeTv; // 验证码：申请下发

	@InjectView(id = R.id.tv_next)
	private TextView mNextTv; // "下一步"按钮

	@InjectView(id = R.id.tv_hint)
	private TextView mHintTv; // 提示

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.personal_retrieve_pwd_step1_view,
				container, false);

		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// IOC
		InjectUtil.injectFragment(this, view);

		// initial vies
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

		// "下一步"
		mNextTv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!checkInput()) {
					return;
				}

				next();
			}
		});

		// hint
		String text = FileHelper.readAssertFile(getActivity(),
				"RetrievePwdStep1.txt", "utf-8");
		mHintTv.setText(text);
	}

	// ---------------------------------------------------- Private methods
	/**
	 * 获取手机验证码。
	 */
	private void getVerifyCode() {
		// 手机号码
		String phone = mPhoneEt.getText().toString();
		if (TextUtils.isEmpty(phone)) {
			ToastHelper.toast(getActivity(),
					R.string.personal_login_hint_input_phone);
			return;
		}

		// send
		String params = HttpParameters.getVerifyPhone(phone,
				HttpParameters.CODE_TYPE_FORGET_PWD);
		IHttpResponeListener listener = new IHttpResponeListener() {

			@Override
			public void onHttpRespone(ResponseEntity response) {
				dealGetVerifyCode(response);
			}
		};
		HttpUtils.HttpPost(HttpUrls.URL_VEFIFY_PHONE, params, listener);
	}

	/**
	 * 处理响应。
	 * 
	 * @param response
	 *            响应。
	 */
	private void dealGetVerifyCode(ResponseEntity response) {
		int code = ResponseParser.parseCode(response);

		// success
		if (code == 0) {
			ToastHelper.toast(getActivity(),
					R.string.personal_register_hint_verifycode_sended);
			startTimer();
			return;
		}

		// fail
		String msg = ResponseCodeHelper.getHint(getActivity(), code);
		ToastHelper.toast(getActivity(), msg);
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
			ToastHelper.toast(getActivity(),
					R.string.personal_login_hint_input_phone);
			return false;
		}

		// 验证码
		String verifyCode = mVerifyCodeEt.getText().toString();
		if (TextUtils.isEmpty(verifyCode)) {
			ToastHelper.toast(getActivity(),
					R.string.personal_login_hint_input_verifycode);
			return false;
		}

		return true;
	}

	/**
	 * "下一步"。
	 */
	private void next() {
		// parameters
		String phone = mPhoneEt.getText().toString();
		String verifyCode = mVerifyCodeEt.getText().toString();

		// send
		String params = HttpParameters.retrievePwdVerify(phone, verifyCode);
		IHttpResponeListener listener = new IHttpResponeListener() {

			@Override
			public void onHttpRespone(ResponseEntity response) {
				dealNext(response);
			}
		};
		HttpUtils.HttpPost(HttpUrls.URL_RETRIEVE_PWD_VERIFY, params, listener);
	}

	/**
	 * 处理响应。
	 * 
	 * @param response
	 *            响应。
	 */
	private void dealNext(ResponseEntity response) {
		int code = ResponseParser.parseCode(response);

		// success
		if (code == 0) {
			String phone = mPhoneEt.getText().toString();
			RetrievePwdActivity.sendBroadcast(getActivity(), 2, phone);
			return;
		}

		// fail
		String msg = ResponseCodeHelper.getHint(getActivity(), code);
		ToastHelper.toast(getActivity(), msg);
	}

	// ---------------------------------------------------- timer
	private void startTimer() {
		stopTimer();

		// 禁用按钮
		mVerifyCodeTv.setEnabled(false);

		// 设置计数值
		mCount = 30;

		// 启动timer
		mTimer = new Timer(getActivity().getPackageName(), false);
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
