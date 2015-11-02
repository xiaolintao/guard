package com.pplt.guard.personal;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.hipalsports.entity.UserInfo;
import com.hipalsports.enums.PurposeEnum;
import com.jty.util.ToastHelper;
import com.kingdom.sdk.ioc.InjectUtil;
import com.kingdom.sdk.ioc.annotation.InjectView;
import com.pplt.guard.Global;
import com.pplt.guard.Jump;
import com.pplt.guard.R;
import com.pplt.guard.comm.api.AccountAPI;
import com.pplt.guard.comm.response.ResponseCodeHelper;
import com.pplt.guard.comm.response.ResponseParser;
import com.pplt.guard.personal.checker.AccountChecker;
import com.pplt.guard.personal.checker.InputChecker;
import com.pplt.ui.TitleBar;

/**
 * 注册。
 */
public class RegisterActivity extends VerifyCodeActivity {

	// ---------------------------------------------------- Private data
	@InjectView(id = R.id.title_bar)
	private TitleBar mTitleBar;

	@InjectView(id = R.id.et_pwd)
	private EditText mPwdEt; // 密码

	@InjectView(id = R.id.et_nickname)
	private EditText mNicknameEt; // 昵称

	@InjectView(id = R.id.et_verify_code)
	private EditText mVerifyCodeEt; // 验证码：输入

	@InjectView(id = R.id.tv_register, click = "register")
	private TextView mRegisterTv; // 注册按钮

	// ---------------------------------------------------- Override methods
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_register_view);

		// IOC
		InjectUtil.inject(this);

		// initial views
		initViews();

		// test : 账号&密码
		mAccountEt.setText("369024496@qq.com");
		mAccountEt.setSelection(mAccountEt.getText().length());
		mAccountEt.requestFocus();
		mPwdEt.setText("112");
	}

	// ---------------------------------------------------- Private methods
	/**
	 * initial view.
	 */
	private void initViews() {
	}

	// ---------------------------------------------------- Private methods
	/**
	 * TODO: 获取验证码。
	 */
	public void getVerifyCode() {
		doGetVerifyCode(PurposeEnum.REGISTER.value());
	}

	/**
	 * 注册。
	 */
	public void register() {
		// 检查输入
		if (!checkInput()) {
			return;
		}

		forbidClick(mRegisterTv, 3);

		// listener
		Response.Listener<String> listener = new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				if (isFinishing() || response == null) {
					return;
				}

				dealRegister(response);
			}
		};

		// request
		String account = mAccountEt.getText().toString();
		String pwd = mPwdEt.getText().toString();
		String verifyCode = mVerifyCodeEt.getText().toString();
		String nickName = mNicknameEt.getText().toString();
		AccountAPI.register(this, account, pwd, verifyCode, nickName, listener);
	}

	/**
	 * 处理register响应。
	 * 
	 * @param response
	 *            响应。
	 */
	private void dealRegister(String response) {
		int code = ResponseParser.parseCode(response);

		// success
		if (code == 0) {
			ToastHelper.toast(this, R.string.personal_register_successed);

			UserInfo user = ResponseParser.parse(response, UserInfo.class);
			Global.setUser(user);

			Jump.toMain(this);
			Jump.sendLoginBroadcast(this);
			finish();
			return;
		}

		// fail
		String msg = ResponseCodeHelper.getHint(this, code);
		ToastHelper.toast(this, msg);
	}

	/**
	 * 检查输入。
	 * 
	 * @return 输入是否完整。
	 */
	private boolean checkInput() {
		// 账号
		if (!AccountChecker.check(this, mAccountEt)) {
			return false;
		}

		// 验证码
		if (!InputChecker.check(this, mVerifyCodeEt)) {
			return false;
		}

		// 密码
		if (!InputChecker.check(this, mPwdEt)) {
			return false;
		}

		// 昵称
		if (!InputChecker.check(this, mNicknameEt)) {
			return false;
		}

		return true;
	}
}
