package com.pplt.guard.personal.pwd;

import android.os.Bundle;
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
 * 找回密码：第二步。
 */
public class RetrievePwdStep2Fragment extends Fragment {

	// ---------------------------------------------------- Private data
	@InjectView(id = R.id.et_pwd)
	private EditText mPwdEt; // 密码

	@InjectView(id = R.id.et_pwd2)
	private EditText mPwd2Et; // 密码2

	@InjectView(id = R.id.tv_next)
	private TextView mNextTv; // "下一步"按钮

	@InjectView(id = R.id.tv_hint)
	private TextView mHintTv; // 提示

	private String mPhone = ""; // 手机号码

	// ---------------------------------------------------- Public methods
	/**
	 * 设置手机号码。
	 * 
	 * @param phone
	 *            号码。
	 */
	public void setPhone(String phone) {
		mPhone = phone;
	}

	// ---------------------------------------------------- Override methods
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.personal_retrieve_pwd_step2_view,
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
				"RetrievePwdStep2.txt", "utf-8");
		mHintTv.setText(text);
	}

	// ---------------------------------------------------- Private methods
	/**
	 * 检查输入。
	 * 
	 * @return 输入是否完整。
	 */
	private boolean checkInput() {
		// 密码
		String pwd = mPwdEt.getText().toString();
		if (TextUtils.isEmpty(pwd)) {
			ToastHelper.toast(getActivity(),
					R.string.personal_login_hint_input_pwd);
			return false;
		}

		// 密码2
		String pwd2 = mPwdEt.getText().toString();
		if (TextUtils.isEmpty(pwd2)) {
			ToastHelper.toast(getActivity(),
					R.string.personal_login_hint_input_pwd2);
			return false;
		}

		// 两次密码比较
		if (pwd.compareTo(pwd2) != 0) {
			ToastHelper.toast(getActivity(),
					R.string.personal_login_hint_input_pwd_not_equal);
			return false;
		}

		return true;
	}

	/**
	 * "下一步"。
	 */
	private void next() {
		// parameters
		String pwd = mPwdEt.getText().toString();

		// send
		String params = HttpParameters.retrievePwdFind(mPhone, pwd);
		IHttpResponeListener listener = new IHttpResponeListener() {

			@Override
			public void onHttpRespone(ResponseEntity response) {
				dealNext(response);
			}
		};
		HttpUtils.HttpPost(HttpUrls.URL_RETRIEVE_PWD_FIND, params, listener);
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
			RetrievePwdActivity.sendBroadcast(getActivity(), 3, null);
			return;
		}

		// fail
		String msg = ResponseCodeHelper.getHint(getActivity(), code);
		ToastHelper.toast(getActivity(), msg);
	}
}
