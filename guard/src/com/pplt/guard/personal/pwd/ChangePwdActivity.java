package com.pplt.guard.personal.pwd;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.hipalsports.entity.UserInfo;
import com.jty.util.ToastHelper;
import com.kingdom.sdk.ioc.InjectUtil;
import com.kingdom.sdk.ioc.annotation.InjectView;
import com.pplt.guard.BaseActivity;
import com.pplt.guard.Global;
import com.pplt.guard.R;
import com.pplt.guard.comm.api.AccountAPI;
import com.pplt.guard.comm.response.ResponseCodeHelper;
import com.pplt.guard.comm.response.ResponseParser;
import com.pplt.guard.personal.checker.AccountChecker;
import com.pplt.guard.personal.checker.InputChecker;
import com.pplt.ui.TitleBar;

/**
 * 修改密码。
 */
public class ChangePwdActivity extends BaseActivity {

	// ---------------------------------------------------- Private data
	@InjectView(id = R.id.title_bar)
	private TitleBar mTitleBar;

	@InjectView(id = R.id.et_account)
	protected EditText mAccountEt; // 账号：手机&邮箱

	@InjectView(id = R.id.et_old_pwd)
	private EditText mOldPwdEt; // 旧密码

	@InjectView(id = R.id.et_new_pwd)
	private EditText mNewPwdEt; // 新密码

	@InjectView(id = R.id.tv_change_pwd, click = "changePwd")
	private TextView mChangePwdTv; // 修改密码按钮

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
		String oldPwd = mOldPwdEt.getText().toString();
		String newPwd = mNewPwdEt.getText().toString();
		AccountAPI.changePassword(this, account, oldPwd, newPwd, listener);
	}

	/**
	 * 处理响应：修改密码。
	 * 
	 * @param response
	 *            响应包。
	 */
	private void dealChangePwd(String response) {
		int code = ResponseParser.parseCode(response);

		// success
		if (code == 0) {
			ToastHelper.toast(this, R.string.personal_retrieve_pwd_changed);
			finish();
			return;
		}

		// fail
		String hint = ResponseCodeHelper.getHint(this, code);
		ToastHelper.toast(this, hint);
	}

	// ---------------------------------------------------- Check input
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

		// 旧密码
		if (!InputChecker.check(this, mOldPwdEt)) {
			return false;
		}

		// 新密码
		if (!InputChecker.check(this, mNewPwdEt)) {
			return false;
		}

		return true;
	}
}
