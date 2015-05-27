package com.pplt.guard.contact;

import java.util.List;

import org.codehaus.jackson.type.TypeReference;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.jty.util.JSonUtils;
import com.kingdom.sdk.ioc.InjectUtil;
import com.kingdom.sdk.ioc.annotation.InjectView;
import com.pplt.guard.BaseActivity;
import com.pplt.guard.Global;
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

	private ContactAdapter mAdapter; // adapter

	private List<Long> mIds; // 联系人id

	// ---------------------------------------------------- Override methods
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_choice_view);

		// extra
		Intent intent = getIntent();
		if (intent.hasExtra(Global.EXTRA_IDS)) {
			String json = intent.getStringExtra(Global.EXTRA_IDS);
			TypeReference<List<Long>> typeReference = new TypeReference<List<Long>>() {
			};
			mIds = JSonUtils.readValue(json, typeReference);
		}

		// IOC
		InjectUtil.inject(this);

		// initial
		initTitleBar();
		initViews();
	}

	@Override
	public void onResume() {
		super.onResume();

		refresh();
	}

	// ---------------------------------------------------- Private methods
	/**
	 * initial title bar.
	 */
	private void initTitleBar() {
		mTitleBar.setRightBtnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				onClickFinish();
			}
		});
	}

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
		List<Contact> list = mIds != null ? ContactDataHelper.getContacts(mIds)
				: ContactDataHelper.getContacts();
		mAdapter.setData(list);
	}

	/**
	 * 完成。
	 */
	private void onClickFinish() {
		List<Long> ids = mAdapter.getSelection();
		if (ids != null && ids.size() != 0) {
			String json = JSonUtils.toJSon(ids);
			Intent data = new Intent();
			data.putExtra(Global.EXTRA_IDS, json);

			setResult(RESULT_OK, data);
		}

		finish();
	}

}
