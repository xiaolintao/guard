package com.pplt.guard.personal.pwd;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.hipalsports.enums.PurposeEnum;
import com.jty.util.ToastHelper;
import com.kingdom.sdk.ioc.InjectUtil;
import com.kingdom.sdk.ioc.annotation.InjectView;
import com.pplt.guard.Jump;
import com.pplt.guard.R;
import com.pplt.guard.comm.api.AccountAPI;
import com.pplt.guard.comm.response.ResponseCodeHelper;
import com.pplt.guard.comm.response.ResponseParser;
import com.pplt.guard.personal.VerifyCodeActivity;
import com.pplt.guard.personal.checker.AccountChecker;
import com.pplt.guard.personal.checker.InputChecker;
import com.pplt.ui.TitleBar;

/**
 * 找回密码。
 */
public class RetrievePwdActivity extends VerifyCodeActivity {

	// ---------------------------------------------------- Private data
	@InjectView(id = R.id.title_bar)
	private TitleBar mTitleBar;

	@InjectView(id = R.id.et_verify_code)
	private EditText mVerifyCodeEt; // 验证码：输入

	@InjectView(id = R.id.tv_validate_account_number, click = "validateAccountNumber")
	private TextView mValidateAccountNumberTv; // 第1步："下一步"按钮

	@InjectView(id = R.id.et_pwd)
	private EditText mPwdEt; // 密码

	@InjectView(id = R.id.tv_reset_password, click = "resetPwd")
	private TextView mResetPasswordTv; // 第2步："确定修改"按钮

	// ---------------------------------------------------- Override methods
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_retrieve_pwd_view);

		// IOC
		InjectUtil.inject(this);

		// test : 账号&密码
		mAccountEt.setText("369024496@qq.com");
		mAccountEt.setSelection(mAccountEt.getText().length());
		mAccountEt.requestFocus();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	// ---------------------------------------------------- Private methods
	/**
	 * TODO: 获取验证码。
	 */
	public void getVerifyCode() {
		doGetVerifyCode(PurposeEnum.RESET_PWD.value());
	}

	/**
	 * 验证账号。
	 */
	public void validateAccountNumber() {
		// 检查输入
		if (!checkValidateAccountNumber()) {
			return;
		}

		forbidClick(mValidateAccountNumberTv, 3);

		// listener
		Response.Listener<String> listener = new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				if (isFinishing() || response == null) {
					return;
				}

				dealValidateAccountNumber(response);
			}
		};

		// request
		String account = mAccountEt.getText().toString();
		String verifyCode = mVerifyCodeEt.getText().toString();
		AccountAPI.validateAccountNumber(this, account, verifyCode, listener);
	}

	/**
	 * 处理响应：验证账号。
	 */
	private void dealValidateAccountNumber(String response) {
		int code = ResponseParser.parseCode(response);

		// success
		if (code == 0) {
			toStep2();
			return;
		}

		// fail
		String msg = ResponseCodeHelper.getHint(this, code);
		ToastHelper.toast(this, msg);
	}

	/**
	 * 重设密码。
	 */
	public void resetPwd() {
		// 检查输入
		if (!checkResetPwd()) {
			return;
		}

		// listener
		Response.Listener<String> listener = new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				if (isFinishing() || response == null) {
					return;
				}

				dealResetPwd(response);
			}
		};

		// request
		String account = mAccountEt.getText().toString();
		String pwd = mPwdEt.getText().toString();
		String verifyCode = mVerifyCodeEt.getText().toString();
		AccountAPI.resetPassword(this, account, verifyCode, pwd, listener);
	}

	/**
	 * 处理响应：重设密码。
	 * 
	 * @param response
	 *            响应包。
	 */
	private void dealResetPwd(String response) {
		int code = ResponseParser.parseCode(response);

		// success
		if (code == 0) {
			ToastHelper.toast(this, R.string.personal_retrieve_pwd_changed);
			toLogin();
			return;
		}

		// fail
		String hint = ResponseCodeHelper.getHint(this, code);
		ToastHelper.toast(this, hint);
	}

	/**
	 * 跳转：登录。
	 */
	private void toLogin() {
		mResetPasswordTv.postDelayed(new Runnable() {
			@Override
			public void run() {
				Jump.toLogin(RetrievePwdActivity.this);
				finish();
			}
		}, 3000);
	}

	// ---------------------------------------------------- Input
	/**
	 * 检查输入
	 */
	private boolean checkValidateAccountNumber() {
		// 账号
		if (!AccountChecker.check(this, mAccountEt)) {
			return false;
		}

		// 验证码
		if (!InputChecker.check(this, mVerifyCodeEt)) {
			return false;
		}

		return true;
	}

	/**
	 * 检查输入。
	 * 
	 * @return
	 */
	private boolean checkResetPwd() {
		// 密码
		if (!InputChecker.check(this, mPwdEt)) {
			return false;
		}

		return true;
	}

	// ---------------------------------------------------- Switch
	/**
	 * to : 第2步。
	 */
	private void toStep2() {
		setVisibility(R.id.step1_panel, View.GONE);
		setVisibility(R.id.step2_panel, View.VISIBLE);
	}

	/**
	 * 设置visibility.
	 * 
	 * @param resId
	 *            view resource id.
	 * @param visibility
	 *            visibility.
	 */
	private void setVisibility(int resId, int visibility) {
		View view = findViewById(resId);
		if (view != null) {
			view.setVisibility(visibility);
		}
	}
}
