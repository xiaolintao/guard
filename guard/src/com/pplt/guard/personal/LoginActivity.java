package com.pplt.guard.personal;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jty.util.ToastHelper;
import com.kingdom.sdk.ioc.InjectUtil;
import com.kingdom.sdk.ioc.annotation.InjectView;
import com.pplt.guard.BaseActivity;
import com.pplt.guard.Global;
import com.pplt.guard.MainActivity;
import com.pplt.guard.R;
import com.pplt.guard.personal.pwd.RetrievePwdActivity;

/**
 * 登录。
 */
public class LoginActivity extends BaseActivity {

	// ---------------------------------------------------- Constants
	private final static int REQUEST_CODE_REGISTER = 100; // 注册

	// ---------------------------------------------------- Private data
	@InjectView(id = R.id.et_phone)
	private EditText mPhoneEt; // 手机号码

	@InjectView(id = R.id.et_pwd)
	private EditText mPwdEt; // 密码

	@InjectView(id = R.id.tv_login)
	private TextView mLoginTv; // 登录按钮

	@InjectView(id = R.id.tv_verify_code)
	private TextView mVerifyCodeTv; // 验证码

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

		// test
		mPhoneEt.setText("13510853010");
		mPhoneEt.setSelection(mPhoneEt.getText().length());
		mPhoneEt.requestFocus();
		mPwdEt.setText("000000");
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
		// 验证码
		int code = (int) (Math.random() * 10000.0f);
		mVerifyCodeTv.setText("" + code);

		// 登录
		mLoginTv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!checkInput()) {
					return;
				}

				login();
			}
		});

		// // 忘记密码
		// mForgetPwdTv.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// forgetPwd();
		// }
		// });

	}

	// ---------------------------------------------------- Private methods
	/**
	 * 检查输入。
	 * 
	 * @return 输入是否完整。
	 */
	private boolean checkInput() {
		// 手机号码
		String phone = mPhoneEt.getText().toString();
		if (TextUtils.isEmpty(phone)) {
			ToastHelper.toast(this, R.string.personal_login_hint_input_phone);
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
	private void login() {
		// 手机号码
		String phone = mPhoneEt.getText().toString();
		Global.setPhone(phone);

		toMain();

		finish();
		mTimes = 0;
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
