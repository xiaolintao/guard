package com.pplt.guard.contact;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jty.util.JSonUtils;
import com.jty.util.ToastHelper;
import com.jty.util.reflect.FieldHelper;
import com.kingdom.sdk.ioc.InjectUtil;
import com.kingdom.sdk.ioc.annotation.InjectView;
import com.pplt.guard.BaseActivity;
import com.pplt.guard.Global;
import com.pplt.guard.R;
import com.pplt.guard.entity.Contact;
import com.pplt.guard.entity.ContactDataHelper;
import com.pplt.ui.TitleBar;

/**
 * 联系人：编辑。
 */
public class ContactEditActivity extends BaseActivity {

	// ---------------------------------------------------- Private data
	@InjectView(id = R.id.title_bar)
	private TitleBar mTitleBar;

	@InjectView(id = R.id.et_name)
	private EditText mNameEt; // 姓名

	@InjectView(id = R.id.et_phone)
	private EditText mPhoneEt; // 手机号码

	@InjectView(id = R.id.et_email)
	private EditText mEmailEt; // email

	@InjectView(id = R.id.et_user)
	private EditText mUserEt; // 密防系统的账号

	@InjectView(id = R.id.et_qq)
	private EditText mQQEt; // QQ

	@InjectView(id = R.id.et_sina)
	private EditText mSinaEt; // 新浪微博

	@InjectView(id = R.id.bottom_panel, click = "onClickCmdPanel")
	private View mCmdPanel;

	private Contact mContact = new Contact();

	// ---------------------------------------------------- Override methods
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_add_view);

		// extra
		Intent intent = getIntent();
		if (intent.hasExtra(Global.EXTRA_CONTACT)) {
			String json = intent.getStringExtra(Global.EXTRA_CONTACT);
			mContact = JSonUtils.readValue(json, Contact.class);
			if (mContact == null) {
				finish();
				return;
			}
		}

		// IOC
		InjectUtil.inject(this);

		// initial
		initTitleBar();
		initViews();
	}

	// ---------------------------------------------------- Private methods
	/**
	 * initial title bar.
	 */
	private void initTitleBar() {
		// title
		int resId = mContact.getId() != 0 ? R.string.contact_title_edit
				: R.string.contact_title_add;
		mTitleBar.setTitleText(resId);

		// command panel
		int visibility = mContact.getId() != 0 ? View.GONE : View.VISIBLE;
		setVisibility(R.id.iv_add, visibility);
		int textResId = mContact.getId() != 0 ? R.string.save : R.string.add;
		setText(R.id.tv_add, textResId);
	}

	/**
	 * initial view.
	 */
	private void initViews() {
		// 姓名
		FieldHelper.addTextWatcher(mNameEt, mContact, "name");

		// 手机号码
		FieldHelper.addTextWatcher(mPhoneEt, mContact, "phone");

		// email
		FieldHelper.addTextWatcher(mEmailEt, mContact, "email");

		// 密防系统的账号
		FieldHelper.addTextWatcher(mUserEt, mContact, "user");

		// QQ
		FieldHelper.addTextWatcher(mQQEt, mContact, "qq");

		// 新浪微博
		FieldHelper.addTextWatcher(mSinaEt, mContact, "sina");
	}

	/**
	 * 点击：制作文件。
	 */
	public void onClickCmdPanel() {
		if (!checkInput()) {
			return;
		}

		mContact.setTimestamp(System.currentTimeMillis());
		if (mContact.getId() == 0) {
			if (ContactDataHelper.insert(mContact) == 0) {
				return;
			}
		} else {
			if (ContactDataHelper.update(mContact) == 0) {
				return;
			}
		}

		finish();
	}

	/**
	 * 检查输入。
	 * 
	 * @return 输入是否完整。
	 */
	private boolean checkInput() {
		// 姓名
		if (TextUtils.isEmpty(mContact.getName())) {
			ToastHelper.toast(this, R.string.contact_hint_input_name);
			return false;
		}

		return true;
	}

	private void setVisibility(int resId, int visibility) {
		View view = findViewById(resId);
		if (view != null) {
			view.setVisibility(visibility);
		}
	}

	private void setText(int resId, int textResId) {
		View view = findViewById(resId);
		if (view != null && view instanceof TextView) {
			((TextView) view).setText(textResId);
		}
	}

}
