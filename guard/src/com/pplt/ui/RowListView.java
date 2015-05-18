package com.pplt.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import com.pplt.guard.R;


/**
 * 多行的list view.
 * 
 */
public class RowListView extends LinearLayout {

	private List<View> mViews = new ArrayList<View>(); // 子view

	private int mColumns; // 列数
	private int mRowHeight; // 行高

	// ------------------------------------------------- Constructor & Setting
	public RowListView(Context context) {
		super(context);
	}

	public RowListView(Context context, AttributeSet attrs) {
		super(context, attrs);

		mColumns = AttributeSetHelper.getInt(context, attrs,
				R.styleable.RowListView, R.styleable.RowListView_columns, 1);
		mRowHeight = (int) AttributeSetHelper.getDimension(context, attrs,
				R.styleable.RowListView, R.styleable.RowListView_rowHeight, 10);

		setOrientation(VERTICAL);
	}

	// ---------------------------------------------------- Public methods
	/**
	 * 设置adapter。
	 * 
	 * @param adapter
	 *            adapter。
	 */
	public void setAdapter(final ListAdapter adapter) {
		LinearLayout row = null;

		removeAllViews();

		for (int i = 0; i < adapter.getCount(); i++) {
			// change row
			if (mColumns != 0 && i % mColumns == 0) {
				row = changeRow();
			}

			// add view to row
			View view = adapter.getView(i, null, this);
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT, 1);
			row.addView(view, params);

			// add to list
			mViews.add(view);
		}

		// DataSetObserver
		adapter.registerDataSetObserver(new DataSetObserver() {

			@Override
			public void onChanged() {
				for (int i = 0; i < adapter.getCount(); i++) {
					View view = mViews.get(i);

					adapter.getView(i, view, RowListView.this);
				}
			}

			@Override
			public void onInvalidated() {
			}
		});

	}

	// ---------------------------------------------------- Private methods
	private LinearLayout changeRow() {
		LinearLayout row = new LinearLayout(getContext());
		row.setOrientation(HORIZONTAL);

		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
				mRowHeight);
		addView(row, lp);

		return row;
	}

}
