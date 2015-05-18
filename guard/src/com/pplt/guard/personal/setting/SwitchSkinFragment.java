package com.pplt.guard.personal.setting;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kingdom.sdk.ioc.InjectUtil;
import com.pplt.guard.Global;
import com.pplt.guard.R;

/**
 * 设切换皮肤。
 */
public class SwitchSkinFragment extends Fragment {

	// ---------------------------------------------------- Private data
	private int[] mPanels = new int[] { R.id.style_black_panel,
			R.id.style_red_panel,
			R.id.style_blue_panel };

	private View mContentView;

	// ---------------------------------------------------- Override methods
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mContentView = inflater.inflate(R.layout.personal_setting_swtich_skin,
				container, false);

		return mContentView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// IOC
		InjectUtil.injectFragment(this, view);

		// initial views
		initViews();
	}

	// ---------------------------------------------------- Private methods
	/**
	 * initial views.
	 */
	private void initViews() {
		initStyle();
	}

	/**
	 * 设置风格。
	 */
	private void initStyle() {
		View.OnClickListener listener = new View.OnClickListener() {

			@Override
			public void onClick(View view) {
			}
		};

		for (int id : mPanels) {
			View view = mContentView.findViewById(id);
			if (view != null) {
				view.setOnClickListener(listener);
			}
		}
	}

	/**
	 * change theme.
	 * 
	 * @param resId
	 *            resource id of style view.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	void changeTheme(int resId) {
		int style = getStyleIndex(resId);

		// save
		Global.setTheme(style);

		// recreate current activity
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActivity().recreate();
		}

		// send broadcast
		Intent intent = new Intent();
		intent.setAction(Global.ACTION_CHANGE_THEME);
		getActivity().sendBroadcast(intent);
	}

	private int getStyleIndex(int resId) {
		int style = 0;
		for (int i = 0; i < mPanels.length; i++) {
			if (resId == mPanels[i]) {
				style = i;
			}
		}
		return style;
	}
}
