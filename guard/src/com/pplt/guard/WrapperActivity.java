package com.pplt.guard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.jty.util.FragmentHelper;
import com.kingdom.sdk.ioc.InjectUtil;
import com.kingdom.sdk.ioc.annotation.InjectView;
import com.pplt.guard.R;
import com.pplt.ui.TitleBar;

/**
 * Fragment wrapper。
 * 
 */
public class WrapperActivity extends BaseActivity {

	// ---------------------------------------------------- Constants
	private final static String EXTRA_TITLE = "title"; // 标题
	private final static String EXTRA_FRAGMENT = "fragment"; // Fragment类名

	// ---------------------------------------------------- Private data
	@InjectView(id = R.id.title_bar)
	private TitleBar mTitleBar;

	private String mTitle = ""; // 标题
	private String mFragment = ""; // Fragment类名

	// ---------------------------------------------------- Override methods
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wrapper_view);

		// intent data
		Intent intent = getIntent();
		if (intent.hasExtra(EXTRA_FRAGMENT)) {
			mFragment = intent.getStringExtra(EXTRA_FRAGMENT);
		} else {
			finish();
			return;
		}
		if (intent.hasExtra(EXTRA_TITLE)) {
			mTitle = intent.getStringExtra(EXTRA_TITLE);
		}

		// IOC
		InjectUtil.inject(this);

		// initial views
		initViews();
	}

	/**
	 * start.
	 * 
	 * @param context
	 *            context.
	 * @param title
	 *            标题。
	 * @param fragment
	 *            Fragment类名
	 */
	public static void start(Context context, String title, String fragment) {
		Intent intent = new Intent(context, WrapperActivity.class);
		intent.putExtra(EXTRA_TITLE, title);
		intent.putExtra(EXTRA_FRAGMENT, fragment);

		context.startActivity(intent);
	}

	// ---------------------------------------------------- Private methods
	/**
	 * initial view.
	 */
	private void initViews() {
		// title
		mTitleBar.setTitleText(mTitle);

		// fragment
		FragmentHelper.add(getSupportFragmentManager(), this, mFragment,
				R.id.content_panel);
	}

}
