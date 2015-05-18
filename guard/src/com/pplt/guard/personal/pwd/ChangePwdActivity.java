package com.pplt.guard.personal.pwd;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.jty.util.ToastHelper;
import com.kingdom.sdk.ioc.InjectUtil;
import com.kingdom.sdk.ioc.annotation.InjectView;
import com.kingdom.sdk.net.http.HttpUtils;
import com.kingdom.sdk.net.http.IHttpResponeListener;
import com.kingdom.sdk.net.http.ResponseEntity;
import com.pplt.guard.BaseActivity;
import com.pplt.guard.Global;
import com.pplt.guard.comm.HttpParameters;
import com.pplt.guard.comm.HttpUrls;
import com.pplt.guard.comm.ResponseCodeHelper;
import com.pplt.guard.comm.ResponseParser;
import com.pplt.guard.comm.entity.UserEntity;
import com.pplt.guard.R;
import com.pplt.ui.TitleBar;

/**
 * 修改密码。
 */
public class ChangePwdActivity extends BaseActivity {

	// ---------------------------------------------------- Private data
	@InjectView(id = R.id.title_bar)
	private TitleBar mTitleBar;

	@InjectView(id = R.id.et_old_pwd)
	private EditText mOldPwdEt; // 旧密码

	@InjectView(id = R.id.et_pwd)
	private EditText mPwdEt; // 密码

	@InjectView(id = R.id.et_pwd2)
	private EditText mPwd2Et; // 密码2

	// ---------------------------------------------------- Override methods
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_change_pwd_view);

		// IOC
		InjectUtil.inject(this);

		// initial
		initTitleBar();
		initViews();
	}

	/**
	 * initial title bar.
	 */
	private void initTitleBar() {
		// right button
		mTitleBar.setRightBtnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!checkInput()) {
					return;
				}

				changePwd();
			}
		});
	}

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
		// 密码
		String oldPwd = mOldPwdEt.getText().toString();
		if (TextUtils.isEmpty(oldPwd)) {
			ToastHelper.toast(this, R.string.personal_login_hint_input_old_pwd);
			return false;
		}

		// 密码
		String pwd = mPwdEt.getText().toString();
		if (TextUtils.isEmpty(pwd)) {
			ToastHelper.toast(this, R.string.personal_login_hint_input_pwd);
			return false;
		}

		// 密码2
		String pwd2 = mPwdEt.getText().toString();
		if (TextUtils.isEmpty(pwd2)) {
			ToastHelper.toast(this, R.string.personal_login_hint_input_pwd2);
			return false;
		}

		// 两次密码比较
		if (pwd.compareTo(pwd2) != 0) {
			ToastHelper.toast(this,
					R.string.personal_login_hint_input_pwd_not_equal);
			return false;
		}

		return true;
	}

	/**
	 * 修改密码。
	 */
	private void changePwd() {
		// user
		UserEntity user = Global.getUser();
		if (user == null) {
			return;
		}

		// parameters
		String oldPwd = mOldPwdEt.getText().toString();
		String pwd = mPwdEt.getText().toString();

		// send
		String params = HttpParameters.changePwd(user.getId(), oldPwd, pwd);
		IHttpResponeListener listener = new IHttpResponeListener() {

			@Override
			public void onHttpRespone(ResponseEntity response) {
				dealChangePwd(response);
			}
		};
		HttpUtils.HttpPost(HttpUrls.URL_CHANGE_PWD, params, listener);
	}

	/**
	 * 处理响应。
	 * 
	 * @param response
	 *            响应。
	 */
	private void dealChangePwd(ResponseEntity response) {
		int code = ResponseParser.parseCode(response);

		// success
		if (code == 0) {
			ToastHelper.toast(this, R.string.personal_retrieve_pwd_changed);
			finish();
			return;
		}

		// fail
		String msg = ResponseCodeHelper.getHint(this, code);
		ToastHelper.toast(this, msg);
	}

}
