package com.pplt.guard.personal.pwd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kingdom.sdk.ioc.InjectUtil;
import com.kingdom.sdk.ioc.annotation.InjectView;
import com.pplt.guard.personal.LoginActivity;
import com.pplt.guard.R;

/**
 * 找回密码：第三步。
 */
public class RetrievePwdStep3Fragment extends Fragment {

	// ---------------------------------------------------- Private data
	@InjectView(id = R.id.tv_to_login)
	private TextView mNextTv;

	// ---------------------------------------------------- Override methods
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.personal_retrieve_pwd_step3_view,
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
		mNextTv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				login();
			}
		});
	}

	/**
	 * 登录。
	 */
	private void login() {
		Intent intent = new Intent(getActivity(), LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		startActivity(intent);
	}
}
