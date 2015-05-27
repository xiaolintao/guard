package com.pplt.guard.file;

import java.util.List;

import org.codehaus.jackson.type.TypeReference;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jty.util.FileHelper;
import com.jty.util.JSonUtils;
import com.jty.util.ToastHelper;
import com.jty.util.reflect.FieldHelper;
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
import com.pplt.guard.entity.GuardFileSeeker;
import com.pplt.ui.EmbededListView;
import com.pplt.ui.TitleBar;

/**
 * 密防文件：制作。
 * 
 */
public class GuardFileAddActivity extends BaseActivity {

	// ---------------------------------------------------- Constants
	/** 时间格式 */
	private final static String PATTERN_TIME = "yyyy-MM-dd";

	/** request code */
	private final static int REQUEST_CODE_CONTACT = 1; // 选择联系人

	// ---------------------------------------------------- Private data
	@InjectView(id = R.id.title_bar)
	private TitleBar mTitleBar;

	@InjectView(id = R.id.tv_select_file, click = "onClickSelectFile")
	private TextView mSelectFileTv; // 选择文件

	@InjectView(id = R.id.et_open_times)
	private EditText mOpenTimesEt; // 可以打开次数

	@InjectView(id = R.id.tv_begin_time)
	private TextView mBeginTimeTv; // 授权开始日期

	@InjectView(id = R.id.tv_end_time)
	private TextView mEndTimeTv; // 授权截止日期

	@InjectView(id = R.id.et_summary)
	private EditText mSummaryEt; // 摘要

	@InjectView(id = R.id.tv_auth_to, click = "onClickAuthTo")
	private TextView mAutoToTv; // 授权给

	@InjectView(id = R.id.list_view)
	private EmbededListView mListView; // 授权的联系人

	@InjectView(id = R.id.bottom_panel, click = "onClickCmdPanel")
	private View mCmdPanel;

	private GuardFileAuthAdapter mAdapter; // 授权的联系人：adapter

	private GuardFile mGuardFile = new GuardFile(); // 密防密防文件

	// ---------------------------------------------------- Override methods
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guard_file_add_view);

		// IOC
		InjectUtil.inject(this);

		// initial views
		initViews();
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
	 * initial view.
	 */
	private void initViews() {
		// 打开次数
		FieldHelper.addTextWatcher(mOpenTimesEt, mGuardFile, "openTimes");

		// 授权开始日期
		FieldHelper.addDatePicker(this, mBeginTimeTv,
				R.string.guard_file_label_begin_time, PATTERN_TIME);
		FieldHelper.addDateWatcher(mBeginTimeTv, mGuardFile, "authBeginTime",
				PATTERN_TIME);

		// 授权截止日期
		FieldHelper.addDatePicker(this, mEndTimeTv,
				R.string.guard_file_label_end_time, PATTERN_TIME);
		FieldHelper.addDateWatcher(mEndTimeTv, mGuardFile, "authEndTime",
				PATTERN_TIME);

		// 摘要
		FieldHelper.addTextWatcher(mSummaryEt, mGuardFile, "summary");

		// 授权的联系人
		mAdapter = new GuardFileAuthAdapter(this);
		mListView.setAdapter(mAdapter);
	}

	/**
	 * 点击：选择文件。
	 */
	public void onClickSelectFile() {
		String filePath = GuardFileSeeker.getFile();
		if (filePath == null) {
			return;
		}

		showFileInfo(filePath);
	}

	/**
	 * 显示文件信息.
	 * 
	 * @param filePath
	 *            文件路径名.
	 */
	private void showFileInfo(String filePath) {
		String fileName = FileHelper.getFilename(filePath);
		mSelectFileTv.setText(fileName);

		mGuardFile.setFileName(fileName);
		mGuardFile.setFilePath(filePath);
	}

	/**
	 * 点击：授权给。
	 */
	public void onClickAuthTo() {
		List<Long> ids = mAdapter.getAuthTo();
		List<Long> left = ContactDataHelper.filter(ids);

		Intent intent = new Intent(this, ContactChoiceActivity.class);
		String json = JSonUtils.toJSon(left);
		intent.putExtra(Global.EXTRA_IDS, json);

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

		List<Contact> contacts = ContactDataHelper.getContacts(ids);
		mAdapter.appendData(contacts);
	}

	/**
	 * 点击：制作文件。
	 */
	public void onClickCmdPanel() {
		if (!checkInput()) {
			return;
		}

		// 密防文件
		mGuardFile.setTimestamp(System.currentTimeMillis());
		if (GuardFileDataHelper.insert(mGuardFile) == 0) {
			return;
		}

		// 授权
		List<Long> ids = mAdapter.getAuthTo();
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

		finish();
	}

	/**
	 * 检查输入。
	 * 
	 * @return 输入是否完整。
	 */
	private boolean checkInput() {
		// 文件
		if (TextUtils.isEmpty(mGuardFile.getFileName())) {
			ToastHelper.toast(this, R.string.guard_file_hint_input_file);
			return false;
		}

		// 可打开次数
		if (mGuardFile.getOpenTimes() == 0) {
			ToastHelper.toast(this, R.string.guard_file_hint_input_open_times);
			return false;
		}

		// 授权开始日期
		if (mGuardFile.getAuthBeginTime() == 0) {
			ToastHelper.toast(this, R.string.guard_file_hint_input_begin_time);
			return false;
		}

		// 授权截止日期
		if (mGuardFile.getAuthEndTime() == 0) {
			ToastHelper.toast(this, R.string.guard_file_hint_input_end_time);
			return false;
		}

		return true;
	}

}
