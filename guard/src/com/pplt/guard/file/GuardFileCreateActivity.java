package com.pplt.guard.file;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jty.util.FileHelper;
import com.jty.util.ToastHelper;
import com.jty.util.reflect.FieldHelper;
import com.kingdom.sdk.ioc.InjectUtil;
import com.kingdom.sdk.ioc.annotation.InjectView;
import com.pplt.guard.BaseActivity;
import com.pplt.guard.R;
import com.pplt.guard.entity.GuardFile;
import com.pplt.guard.entity.GuardFileDataHelper;
import com.pplt.guard.entity.GuardFileSeeker;
import com.pplt.ui.TitleBar;

/**
 * 密防文件：制作。
 * 
 */
public class GuardFileCreateActivity extends BaseActivity {

	// ---------------------------------------------------- Constants
	/** 时间格式 */
	private final static String PATTERN_TIME = "yyyy-MM-dd";

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

	@InjectView(id = R.id.bottom_panel, click = "onClickCmdPanel")
	private View mCmdPanel;

	private GuardFile mGuardFile = new GuardFile();

	// ---------------------------------------------------- Override methods
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guard_file_create_view);

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
	 * 点击：制作文件。
	 */
	public void onClickCmdPanel() {
		if (!checkInput()) {
			return;
		}

		mGuardFile.setTimestamp(System.currentTimeMillis());
		if (GuardFileDataHelper.insert(mGuardFile) == 0) {
			return;
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
