package com.pplt.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.pplt.guard.R;


/**
 * tab change.
 * 
 */
public class TabChangeEx extends TabChange {

	// ---------------------------------------------------- Constructor
	/**
	 * 构造函数。
	 * 
	 * @param context
	 *            context.
	 */
	public TabChangeEx(Context context) {
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
	public TabChangeEx(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	// ---------------------------------------------------- Public methods
	/**
	 * 添加一个Tab.
	 * 
	 * @param textResId
	 *            标题的resource id.
	 */
	public void addTabEx(int textResId) {
		addTab(R.layout.focus_ranking_tab_item_left, textResId);

		customUI();
	}

	// ---------------------------------------------------- Private methods
	private void customUI() {
		int count = getTabCount();

		for (int i = 0; i < count; i++) {
			View tabView = getTabView(i);
			if (i == 0) {
				tabView.setBackgroundResource(R.drawable.tab_left_bg);
			} else if (i == count - 1) {
				tabView.setBackgroundResource(R.drawable.tab_right_bg);
			} else {
				tabView.setBackgroundResource(R.drawable.tab_middle_bg);
			}
		}
	}

}
