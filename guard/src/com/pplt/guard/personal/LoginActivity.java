package com.pplt.guard.personal;

import android.content.Intent;
import android.os.Bundle;
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
import com.pplt.guard.MainActivity;
import com.pplt.guard.R;
import com.pplt.guard.comm.api.AccountAPI;
import com.pplt.guard.comm.response.ResponseCodeHelper;
import com.pplt.guard.comm.response.ResponseParser;
import com.pplt.guard.personal.pwd.RetrievePwdActivity;

/**
 * 登录。
 */
public class LoginActivity extends BaseActivity {

	// ---------------------------------------------------- Constants
	private final static int REQUEST_CODE_REGISTER = 100; // 注册

	// ---------------------------------------------------- Private data
	@InjectView(id = R.id.et_account)
	private EditText mAccountEt; // 账号：手机&邮箱

	@InjectView(id = R.id.et_pwd)
	private EditText mPwdEt; // 密码

	@InjectView(id = R.id.tv_login, click = "login")
	private TextView mLoginTv; // 登录按钮

	@InjectView(id = R.id.tv_register, click = "register")
	private TextView mRegisterTv; // 注册按钮

	@InjectView(id = R.id.tv_forget_pwd, click = "forgetPwd")
	private TextView mForgetPwdTv; // 忘记密码按钮

	private static int mTimes = 0; // 次数

	// ---------------------------------------------------- Override methods
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_login_view);

		// IOC
		InjectUtil.inject(this);

		// initial
		initViews();

		// test : 账号&密码
		mAccountEt.setText("liut@21cn.com");
		mAccountEt.setSelection(mAccountEt.getText().length());
		mAccountEt.requestFocus();
		mPwdEt.setText("111");
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_REGISTER) {
			if (resultCode == RESULT_OK) {
				finish();
			}
		}
	}

	// ---------------------------------------------------- Private methods
	/**
	 * initial view.
	 */
	private void initViews() {
	}

	// ---------------------------------------------------- Private methods
	/**
	 * 检查输入。
	 * 
	 * @return 输入是否完整。
	 */
	private boolean checkInput() {
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

		// 密码
		String pwd = mPwdEt.getText().toString();
		if (TextUtils.isEmpty(pwd)) {
			ToastHelper.toast(this, R.string.personal_login_hint_input_pwd);
			return false;
		}

		// 验证码
		if (mTimes >= 3) {
		}

		return true;
	}

	/**
	 * 登录。
	 */
	public void login() {
		// check input
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

				dealLoginResponse(response);
			}
		};

		// request
		String account = mAccountEt.getText().toString();
		String pwd = mPwdEt.getText().toString();
		AccountAPI.login(this, account, "0", pwd, listener);
	}

	/**
	 * deal : 登录响应包。
	 * 
	 * @param response
	 *            响应包。
	 */
	private void dealLoginResponse(String response) {
		// check result
		boolean result = ResponseParser.parseResult(response);
		if (!result) {
			mTimes++;

			int code = ResponseParser.parseCode(response);
			String hint = ResponseCodeHelper.getHint(this, code);
			ToastHelper.toast(this, hint);
			return;
		}

		// 用户信息
		UserInfo user = ResponseParser.parse(response, UserInfo.class);
		Global.setUser(user);

		toMain();
		finish();
	}

	/**
	 * 注册。
	 */
	public void register() {
		Intent intent = new Intent(this, RegisterActivity.class);

		startActivityForResult(intent, REQUEST_CODE_REGISTER);
	}

	/**
	 * 忘记密码。
	 */
	public void forgetPwd() {
		Intent intent = new Intent(this, RetrievePwdActivity.class);

		startActivity(intent);
	}

	/**
	 * 跳转：主界面.
	 */
	private void toMain() {
		Intent intent = new Intent(this, MainActivity.class);

		startActivity(intent);
	}
}
