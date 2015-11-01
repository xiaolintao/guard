package com.pplt.guard.contact;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.hipalsports.entity.FriendDetail;
import com.hipalsports.entity.UserInfo;
import com.jty.util.ToastHelper;
import com.kingdom.sdk.ioc.InjectUtil;
import com.kingdom.sdk.ioc.annotation.InjectView;
import com.pplt.guard.Global;
import com.pplt.guard.R;
import com.pplt.guard.comm.api.FriendAPI;
import com.pplt.guard.comm.response.ResponseCodeHelper;
import com.pplt.guard.comm.response.ResponseParser;
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

		refreshFromDB();
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
			}
		});
	}

	/**
	 * initial views.
	 */
	private void initViews() {
		// adapter
		mAdapter = new ContactAdapter(getActivity());
		mAdapter.setMode(ContactAdapter.MODE_CHOICE);
		mListView.setAdapter(mAdapter);
	}

	/**
	 * 刷新：from server。
	 */
	private void refresh() {
		// check
		UserInfo user = Global.getUser();
		if (user == null) {
			return;
		}

		// listener
		Response.Listener<String> listener = new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				if (!isAdded() || isDetached()) {
					return;
				}

				if (response == null) {
					return;
				}

				dealRefresh(response);
			}
		};

		// request
		int userId = Global.getUser().getUserId();
		String lastUpdateTime = FriendDataHelper.getLastUpdateTime(userId);
		FriendAPI.getFriends(getActivity(), userId, lastUpdateTime, 0, 100,
				listener);
	}

	/**
	 * deal : 刷新响应包。
	 * 
	 * @param response
	 *            响应包。
	 */
	private void dealRefresh(String response) {
		int code = ResponseParser.parseCode(response);

		// success
		if (code == 0) {
			// 写数据库
			List<FriendDetail> list = ResponseParser.parseList(response,
					FriendDetail.class);
			FriendDataHelper.add(list);

			refreshFromDB();
			return;
		}

		// fail
		String hint = ResponseCodeHelper.getHint(getActivity(), code);
		ToastHelper.toast(getActivity(), hint);
	}

	/**
	 * 刷新：from database
	 */
	private void refreshFromDB() {
		UserInfo user = Global.getUser();
		if (user == null) {
			return;
		}

		int userId = Global.getUser().getUserId();
		List<FriendDetail> data = FriendDataHelper.queryList(userId, 0);
		mAdapter.setData(data);
	}

}
