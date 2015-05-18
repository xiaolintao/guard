package com.pplt.guard.personal.setting;

import android.os.Bundle;
import android.widget.TextView;

import com.jty.util.FileHelper;
import com.kingdom.sdk.ioc.InjectUtil;
import com.kingdom.sdk.ioc.annotation.InjectView;
import com.pplt.guard.BaseActivity;
import com.pplt.guard.R;

/**
 * 免责。
 */
public class Disclaimer extends BaseActivity {

	// ---------------------------------------------------- Private data
	@InjectView(id = R.id.tv_state)
	private TextView mStateTv;

	// ---------------------------------------------------- Override methods
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_disclaimer_view);

		// IOC
		InjectUtil.inject(this);

		// initial
		initViews();
	}

	// ---------------------------------------------------- Private methods
	/**
	 * initial view.
	 */
	private void initViews() {
		String text = FileHelper
				.readAssertFile(this, "disclaimer.txt", "utf-8");

		mStateTv.setText(text);
	}
}
