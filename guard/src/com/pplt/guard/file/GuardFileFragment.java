package com.pplt.guard.file;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.kingdom.sdk.ioc.InjectUtil;
import com.kingdom.sdk.ioc.annotation.InjectView;
import com.pplt.guard.Global;
import com.pplt.guard.R;
import com.pplt.guard.entity.GuardFile;
import com.pplt.guard.entity.GuardFileDataHelper;
import com.pplt.ui.EmbededListView;
import com.pplt.ui.TitleBar;

/**
 * 密防文件。
 */
public class GuardFileFragment extends Fragment {

	// ---------------------------------------------------- Private methods
	@InjectView(id = R.id.title_bar)
	private TitleBar mTitleBar;

	@InjectView(id = R.id.scroll_view)
	private PullToRefreshScrollView mScrollView; // 下拉

	@InjectView(id = R.id.list_view)
	private EmbededListView mListView; // list view

	private GuardFileAdapter mAdapter; // adapter

	private BroadcastReceiver mReceiver; // 广播receiver

	// ---------------------------------------------------- Override methods
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater
				.inflate(R.layout.guard_file_view, container, false);

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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// broadcast receiver
		registerBroadcastReceiver();
	}

	@Override
	public void onDestroy() {
		// broadcast receiver
		unregisterBroadcastReceiver();

		super.onDestroy();
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
						GuardFileAddActivity.class);
				startActivity(intent);
			}
		});
	}

	/**
	 * initial views.
	 */
	private void initViews() {
		// adapter
		mAdapter = new GuardFileAdapter(getActivity());
		mListView.setAdapter(mAdapter);
	}

	/**
	 * 刷新。
	 */
	private void refresh() {
		List<GuardFile> list = GuardFileDataHelper.getFiles();
		mAdapter.setData(list);
	}

	/**
	 * register broadcast receiver.
	 */
	private void registerBroadcastReceiver() {
		mReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals(Global.ACTION_PPLT_FILE)) {
					refresh();
				}
			}
		};

		IntentFilter filter = new IntentFilter();
		filter.addAction(Global.ACTION_PPLT_FILE); // pplt文件变动
		getActivity().registerReceiver(mReceiver, filter);
	}

	/**
	 * unregister broadcast receiver.
	 */
	private void unregisterBroadcastReceiver() {
		if (mReceiver != null) {
			getActivity().unregisterReceiver(mReceiver);
			mReceiver = null;
		}
	}

}
