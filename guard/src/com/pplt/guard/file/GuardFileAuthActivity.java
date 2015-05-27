package com.pplt.guard.file;

import java.util.List;

import org.codehaus.jackson.type.TypeReference;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jty.util.JSonUtils;
import com.kingdom.sdk.ioc.InjectUtil;
import com.kingdom.sdk.ioc.annotation.InjectView;
import com.pplt.guard.BaseActivity;
import com.pplt.guard.Global;
import com.pplt.guard.R;
import com.pplt.guard.contact.ContactChoiceActivity;
import com.pplt.guard.entity.Contact;
import com.pplt.guard.entity.ContactDataHelper;
import com.pplt.guard.entity.GuardFile;
import com.pplt.guard.entity.GuardFileAuth;
import com.pplt.guard.entity.GuardFileAuthDataHelper;
import com.pplt.guard.entity.GuardFileDataHelper;
import com.pplt.guard.file.GuardFileAuthAdapter.OnClickItemListener;
import com.pplt.ui.EmbededListView;
import com.pplt.ui.TitleBar;

/**
 * 密防文件：制作。
 * 
 */
public class GuardFileAuthActivity extends BaseActivity {

	// ---------------------------------------------------- Constants
	/** request code */
	private final static int REQUEST_CODE_CONTACT = 1; // 选择联系人

	// ---------------------------------------------------- Private data
	@InjectView(id = R.id.title_bar)
	private TitleBar mTitleBar;

	@InjectView(id = R.id.tv_filename)
	private TextView mFileNameTv; // 文件名

	@InjectView(id = R.id.tv_file_summary)
	private TextView mFileSummaryTv; // 文件摘要

	@InjectView(id = R.id.tv_auth_to, click = "onClickAuthTo")
	private TextView mAutoToTv; // 授权给

	@InjectView(id = R.id.list_view)
	private EmbededListView mListView; // 授权的联系人

	private GuardFileAuthAdapter mAdapter; // 授权的联系人：adapter

	private GuardFile mGuardFile = new GuardFile(); // 密防密防文件

	// ---------------------------------------------------- Override methods
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guard_file_auth_view);

		// extra
		Intent intent = getIntent();
		if (!intent.hasExtra(Global.EXTRA_FILE_ID)) {
			finish();
			return;
		}
		long guardFileId = intent.getLongExtra(Global.EXTRA_FILE_ID, 0);
		mGuardFile = GuardFileDataHelper.getFile(guardFileId);
		if (mGuardFile == null) {
			finish();
			return;
		}

		// IOC
		InjectUtil.inject(this);

		// initial views
		initTitleBar();
		initViews();
	}

	@Override
	protected void onResume() {
		super.onResume();

		// 刷新
		refresh();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_CONTACT && resultCode == RESULT_OK) {
			if (data.hasExtra(Global.EXTRA_IDS)) {
				String json = data.getStringExtra(Global.EXTRA_IDS);
				TypeReference<List<Long>> typeReference = new TypeReference<List<Long>>() {};
				List<Long> ids = JSonUtils.readValue(json, typeReference);
				authTo(ids);
			}
		}
	}

	// ---------------------------------------------------- Private methods
	/**
	 * initial title bar.
	 */
	private void initTitleBar() {
		mTitleBar.setRightBtnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				finish();
			}
		});
	}

	/**
	 * initial view.
	 */
	private void initViews() {
		// 文件信息
		showFileInfo();

		// 授权的联系人
		mAdapter = new GuardFileAuthAdapter(this);
		mListView.setAdapter(mAdapter);
		mAdapter.setOnClickItemListener(new OnClickItemListener() {

			@Override
			public void onReverse(Contact contact) {
				GuardFileAuthDataHelper.delete(mGuardFile.getId(),
						contact.getId());
			}
		});
	}

	/**
	 * 显示文件信息
	 */
	private void showFileInfo() {
		// 文件名
		mFileNameTv.setText(mGuardFile.getFileName());

		// 文件摘要
		mFileSummaryTv.setText(mGuardFile.getSummary());
	}

	/**
	 * 点击：授权给。
	 */
	public void onClickAuthTo() {
		Intent intent = new Intent(this, ContactChoiceActivity.class);

		startActivityForResult(intent, REQUEST_CODE_CONTACT);
	}

	/**
	 * 授权给。
	 * 
	 * @param ids
	 *            联系人id.
	 */
	private void authTo(List<Long> ids) {
		if (ids == null || ids.size() == 0) {
			return;
		}

		// 授权
		if (ids != null && ids.size() != 0) {
			for (Long id : ids) {
				GuardFileAuth auth = new GuardFileAuth();
				auth.setGuardFileId(mGuardFile.getId());
				auth.setContactId(id);
				auth.setTimestamp(System.currentTimeMillis());

				if (GuardFileAuthDataHelper.insert(auth) == 0) {
					break;
				}
			}
		}

		// 刷新
		refresh();
	}

	/**
	 * 刷新。
	 */
	private void refresh() {
		List<Long> ids = GuardFileAuthDataHelper.getAuthTo(mGuardFile.getId());

		List<Contact> contacts = ContactDataHelper.getContacts(ids);
		mAdapter.setData(contacts);
	}
}
