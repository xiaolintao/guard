package com.pplt.guard.personal;

import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.TextView;

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
import com.pplt.guard.comm.entity.UserTagEntity;
import com.pplt.guard.R;
import com.pplt.ui.LineListView;
import com.pplt.ui.TitleBar;

/**
 * 用户资料。
 */
public class ProfileActivity extends BaseActivity {

	// ---------------------------------------------------- Private data
	@InjectView(id = R.id.title_bar)
	private TitleBar mTitleBar;

	@InjectView(id = R.id.tv_hint)
	private TextView mHintTv;

	@InjectView(id = R.id.et_name)
	private EditText mNameEt; // 昵称

	@InjectView(id = R.id.tag_list)
	private LineListView mTagList; // 标签

	@InjectView(id = R.id.tv_finish)
	private TextView mFinishTv; // 完成按钮

	private UserTagAdapter mAdapter;

	private int mUid; // 用户id
	private String mPhone; // 手机号码

	// ---------------------------------------------------- Override methods
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_profile_view);

		// intent data
		mUid = getIntent().getIntExtra(Global.EXTRA_UID, -1);
		if (mUid == -1) {
			finish();
			return;
		}
		mPhone = getIntent().getStringExtra(Global.EXTRA_PHONE);

		// IOC
		InjectUtil.inject(this);

		// initial
		initTitleBar();
		initViews();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	// ---------------------------------------------------- Private methods
	/**
	 * initial title bar.
	 */
	private void initTitleBar() {
	}

	/**
	 * initial view.
	 */
	private void initViews() {
		// hint
		String format = getResources().getText(
				R.string.personal_profile_label_registered).toString();
		String hint = String.format(format, mPhone);
		mHintTv.setText(hint);

		// 昵称
		String name = getNickname();
		mNameEt.setText(name);
		mNameEt.setSelection(mNameEt.getText().length());
		mNameEt.requestFocus();

		// 完成
		mFinishTv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!checkInput()) {
					return;
				}

				profile();
			}
		});
	}

	// ---------------------------------------------------- Private methods
	/**
	 * 检查输入。
	 * 
	 * @return 输入是否完整。
	 */
	private boolean checkInput() {
		// 昵称
		String name = mNameEt.getText().toString();
		if (TextUtils.isEmpty(name)) {
			ToastHelper.toast(this, R.string.personal_profile_hint_input_name);
			return false;
		}

		return true;
	}

	/**
	 * 完善资料。
	 */
	private void profile() {
		String name = mNameEt.getText().toString();

		String params = HttpParameters.profile(mUid, name, "");
		IHttpResponeListener listener = new IHttpResponeListener() {

			@Override
			public void onHttpRespone(ResponseEntity response) {
				int code = ResponseParser.parseCode(response);
				if (code == 0) {
					setResult(RESULT_OK);
					finish();
					return;
				} else {
					String msg = ResponseCodeHelper.getHint(
							ProfileActivity.this, code);
					ToastHelper.toast(ProfileActivity.this, msg);
				}
			}
		};

		HttpUtils.HttpPost(HttpUrls.URL_USER_PROFILE, params, listener);
	}

	/**
	 * 获取用户标签。
	 */
	void getUserTag() {
		IHttpResponeListener listener = new IHttpResponeListener() {

			@Override
			public void onHttpRespone(ResponseEntity response) {
				int code = ResponseParser.parseCode(response);
				if (code == 0) {
					List<UserTagEntity> list = ResponseParser.parseList(
							response, UserTagEntity.class);
					if (list != null && list.size() != 0) {
						showUserTag(list);
					}
				}
			}
		};

		HttpUtils.HttpGet(HttpUrls.URL_USER_TAG, listener);
	}

	/**
	 * 显示用户标签。
	 */
	private void showUserTag(List<UserTagEntity> list) {
		// adapter
		mAdapter = new UserTagAdapter(this);
		mAdapter.setData(list);

		// list
		mTagList.setAdapter(mAdapter);
		mTagList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 最多选3个
				if (!mAdapter.isSelected(id)) {
					List<Long> ids = mAdapter.getSelectedIds();
					if (ids.size() >= 3) {
						return;
					}
				}

				mAdapter.select(id);
			}
		});
	}

	/**
	 * 获取昵称。
	 * 
	 * @return 昵称。
	 */
	private String getNickname() {
		if (!TextUtils.isEmpty(mPhone)) {
			return mPhone.length() > 6 ? mPhone.substring(mPhone.length() - 6)
					: mPhone;
		} else {
			long time = System.currentTimeMillis();
			return String.valueOf(time).substring(7);
		}
	}
}