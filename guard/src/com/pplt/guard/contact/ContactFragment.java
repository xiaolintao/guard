package com.pplt.guard.contact;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.kingdom.sdk.ioc.InjectUtil;
import com.kingdom.sdk.ioc.annotation.InjectView;
import com.pplt.guard.R;
import com.pplt.guard.entity.Contact;
import com.pplt.guard.entity.ContactDataHelper;
import com.pplt.ui.EmbededListView;
import com.pplt.ui.TitleBar;

/**
 * 密防文件。
 */
public class ContactFragment extends Fragment {

	// ---------------------------------------------------- Private methods
	@InjectView(id = R.id.title_bar)
	private TitleBar mTitleBar;

	@InjectView(id = R.id.scroll_view)
	private PullToRefreshScrollView mScrollView; // 下拉

	@InjectView(id = R.id.list_view)
	private EmbededListView mListView; // list view

	ContactAdapter mAdapter; // adapter

	// ---------------------------------------------------- Override methods
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.contact_view, container, false);

		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// IOC
		InjectUtil.injectFragment(this, view);

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
		// right button
		mTitleBar.setRightBtnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						ContactAddActivity.class);

				startActivity(intent);
			}
		});
	}

	/**
	 * initial views.
	 */
	private void initViews() {
		// adapter
		mAdapter = new ContactAdapter(getActivity());
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
