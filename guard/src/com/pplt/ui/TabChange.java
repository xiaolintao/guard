package com.pplt.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.pplt.guard.R;


/**
 * tab change.
 * 
 */
public class TabChange extends LinearLayout {

	// ---------------------------------------------------- Private data

	// all tab
	private final List<View> mTabList = new ArrayList<View>();
	private int mCurrentTab = 0; // position of current tab

	private int mTextColor = Color.BLACK; // text color
	private int mCheckedTextColor = -1; // text color when checked
	private int mBorderWidth = 0; // border width

	private boolean mRepeatClick = false; // tab can be repeat click or not.

	private OnTabChangedListener mTabChangedListener;

	// ---------------------------------------------------- Interface
	/**
	 * 当前Tab发生变化时回调的Listener.
	 * 
	 */
	public interface OnTabChangedListener {

		/**
		 * 当前Tab发生变化。
		 * 
		 * @param position
		 *            当前位置。
		 */
		void onTabChanged(int position);
	}

	// ---------------------------------------------------- Constructor
	/**
	 * 构造函数。
	 * 
	 * @param context
	 *            context.
	 */
	public TabChange(Context context) {
		super(context);
	}

	/**
	 * 构造函数。
	 * 
	 * @param context
	 *            context.
	 * @param attrs
	 *            属性。
	 */
	public TabChange(Context context, AttributeSet attrs) {
		super(context, attrs);

		mTextColor = AttributeSetHelper.getColor(context, attrs,
				R.styleable.TabChange, R.styleable.TabChange_textColor,
				Color.BLACK);
		mCheckedTextColor = AttributeSetHelper.getColor(context, attrs,
				R.styleable.TabChange, R.styleable.TabChange_checkedTextColor,
				-1);
		mBorderWidth = (int) AttributeSetHelper.getDimension(context, attrs,
				R.styleable.TabChange, R.styleable.TabChange_borderWidth, 0.0f);

		mRepeatClick = AttributeSetHelper
				.getBoolean(context, attrs, R.styleable.TabChange,
						R.styleable.TabChange_repeatClick, false);
	}

	// ---------------------------------------------------- Public methods
	/**
	 * 设置当前Tab发生变化时回调的Listener..
	 * 
	 * @param listener
	 *            listener.
	 */
	public void setOnTabChangedListener(OnTabChangedListener listener) {
		mTabChangedListener = listener;
	}

	/**
	 * 添加一个Tab.
	 * 
	 * @param layoutResId
	 *            Tab view的layout.
	 * @param textResId
	 *            标题的resource id.
	 */
	public View addTab(int layoutResId, int textResId) {
		View view = inflate(getContext(), layoutResId, null);

		if (textResId != -1) {
			setText(view, textResId);
		}

		addTab(view);

		return view;
	}

	/**
	 * 添加一个Tab.
	 * 
	 * @param layoutResId
	 *            Tab view的layout.
	 * @param text
	 *            标题.
	 */
	public View addTab(int layoutResId, String text) {
		View view = inflate(getContext(), layoutResId, null);

		if (!TextUtils.isEmpty(text)) {
			setText(view, text);
		}

		addTab(view);

		return view;
	}

	/**
	 * 添加一个Tab.
	 * 
	 * @param view
	 *            Tab view.
	 */
	public void addTab(View view) {
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, 1);

		if (mBorderWidth != 0.0f && mTabList.size() != 0) {
			params.setMargins(-mBorderWidth, 0, 0, 0);
		}

		addView(view, params);

		mTabList.add(view);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				int position = mTabList.indexOf(view);

				// 重复点击
				if (!mRepeatClick) {
					if (position == mCurrentTab) {
						return;
					}
				}

				// call listener
				if (mTabChangedListener != null) {
					mTabChangedListener.onTabChanged(position);
				}

				// set current
				mCurrentTab = position;
				setTabStatus();
			}
		});
	}

	public int getTabCount() {
		return mTabList != null ? mTabList.size() : 0;
	}

	/**
	 * 获取当前Tab.
	 * 
	 * @return 当前Tab的位置.
	 */
	public int getCurrentTab() {
		return mCurrentTab;
	}

	/**
	 * 获取当前Tab.
	 * 
	 * @return 当前Tab的View.
	 */
	public View getCurrentTabView() {
		int current = getCurrentTab();

		return current >= 0 && current < mTabList.size() ? mTabList
				.get(current) : null;
	}

	/**
	 * 获取Tab的view.
	 * 
	 * @return Tab的View.
	 */
	public View getTabView(int position) {
		return position >= 0 && position < mTabList.size() ? mTabList
				.get(position) : null;
	}

	/**
	 * 设置当前的Tab.
	 * 
	 * @param position
	 *            当前Tab的位置。
	 */
	public void setCurrentTab(int position) {
		if (position < 0 || position >= mTabList.size()) {
			return;
		}

		mCurrentTab = position;
		setTabStatus();
	}

	/**
	 * 设置Tab的文本。
	 * 
	 * @param postion
	 *            Tab的位置。
	 * @param resid
	 *            文本的resource id.
	 */
	public void setTabText(int position, int resid) {
		View view = getTabView(position);

		if (view != null) {
			setText(view, resid);
		}
	}

	/**
	 * 设置Tab的文本。
	 * 
	 * @param postion
	 *            Tab的位置。
	 * @param text
	 *            文本.
	 */
	public void setTabText(int position, String text) {
		View view = getTabView(position);

		if (view != null) {
			setText(view, text);
		}
	}

	// ---------------------------------------------------- Private methods
	/**
	 * 设置Tab的checked状态
	 */
	private void setTabStatus() {
		for (int i = 0; i < mTabList.size(); i++) {
			View view = mTabList.get(i);

			// checked
			setChecked(view, i == mCurrentTab);

			// text color
			int textColor = i == mCurrentTab ? mCheckedTextColor : mTextColor;
			if (textColor != -1) {
				setTextColor(view, textColor);
			}
		}
	}

	private void setText(View view, int resId) {
		CharSequence text = getContext().getText(resId);

		setText(view, text);
	}

	private void setText(View view, CharSequence text) {
		if (view instanceof TextView) {
			((TextView) view).setText(text);
			((TextView) view).setTextColor(mTextColor);
		}
		if (view instanceof ToggleButton) {
			((ToggleButton) view).setTextOn(text);
			((ToggleButton) view).setTextOff(text);
		}

		if (view instanceof ViewGroup) {
			ViewGroup viewGroup = (ViewGroup) view;
			for (int i = 0; i < viewGroup.getChildCount(); i++) {
				setText(viewGroup.getChildAt(i), text);
			}
		}
	}

	private void setTextColor(View view, int textColor) {
		if (view instanceof TextView) {
			((TextView) view).setTextColor(textColor);
		}

		if (view instanceof ViewGroup) {
			ViewGroup viewGroup = (ViewGroup) view;
			for (int i = 0; i < viewGroup.getChildCount(); i++) {
				setTextColor(viewGroup.getChildAt(i), textColor);
			}
		}
	}

	private void setChecked(View view, boolean checked) {
		if (view instanceof CompoundButton) {
			((CompoundButton) view).setChecked(checked);
		}

		if (view instanceof ViewGroup) {
			ViewGroup viewGroup = (ViewGroup) view;
			for (int i = 0; i < viewGroup.getChildCount(); i++) {
				setChecked(viewGroup.getChildAt(i), checked);
			}
		}
	}
}
