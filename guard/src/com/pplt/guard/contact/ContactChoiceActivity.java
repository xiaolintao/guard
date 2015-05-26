package com.pplt.guard.contact;

import java.util.List;

import android.os.Bundle;

import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.kingdom.sdk.ioc.InjectUtil;
import com.kingdom.sdk.ioc.annotation.InjectView;
import com.pplt.guard.BaseActivity;
import com.pplt.guard.R;
import com.pplt.guard.entity.Contact;
import com.pplt.guard.entity.ContactDataHelper;
import com.pplt.ui.EmbededListView;
import com.pplt.ui.TitleBar;

/**
 * 联系人：选择。
 */
public class ContactChoiceActivity extends BaseActivity {

	// ---------------------------------------------------- Private data
	@InjectView(id = R.id.title_bar)
	private TitleBar mTitleBar;

	@InjectView(id = R.id.scroll_view)
	private PullToRefreshScrollView mScrollView; // 下拉

	@InjectView(id = R.id.list_view)
	private EmbededListView mListView; // list view

	ContactAdapter mAdapter; // adapter

	// ---------------------------------------------------- Override methods
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_choice_view);

		// IOC
		InjectUtil.inject(this);

		// initial
		initViews();
	}

	@Override
	public void onResume() {
		super.onResume();

		refresh();
	}

	// ---------------------------------------------------- Private methods
	/**
	 * initial views.
	 */
	private void initViews() {
		// adapter
		mAdapter = new ContactAdapter(this);
		mAdapter.setMode(ContactAdapter.MODE_CHOICE);
		mListView.setAdapter(mAdapter);
	}

	/**
	 * 刷新。
	 */
	private void refresh() {
		List<Contact> list = ContactDataHelper.getContacts("");
		mAdapter.setData(list);
	}

}
