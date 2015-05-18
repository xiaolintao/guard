package com.pplt.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;

/**
 * 自动换行的list view.
 */
public class LineListView extends ViewGroup {

	// ---------------------------------------------------- Constants
	private static final int SIDE_MARGIN = 10; // "页"边距
	private static final int TEXT_MARGIN = 10; // 子view的间距

	// ---------------------------------------------------- Private data
	private OnItemClickListener mOnItemClickListener; // 点击item时的listener.

	private List<View> mViews = new ArrayList<View>(); // 子view

	// ------------------------------------------------- Constructor & Setting
	public LineListView(Context context) {
		super(context);
	}

	public LineListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	// ---------------------------------------------------- Public methods
	/**
	 * 设置adapter。
	 * 
	 * @param adapter
	 *            adapter。
	 */
	public void setAdapter(final ListAdapter adapter) {
		// 子views的listener
		View.OnClickListener listener = new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				if (mOnItemClickListener == null) {
					return;
				}
				int position = mViews.indexOf(view);
				if (position == -1) {
					return;
				}

				long id = adapter.getItemId(position);
				mOnItemClickListener.onItemClick(null, view, position,
						id);
			}
		};

		// 添加子views
		for (int i = 0; i < adapter.getCount(); i++) {
			View view = adapter.getView(i, null, this);
			view.setOnClickListener(listener);

			// add to view group
			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			addView(view, params);

			// add to list
			mViews.add(view);
		}

		// DataSetObserver
		adapter.registerDataSetObserver(new DataSetObserver() {

			@Override
			public void onChanged() {
				for (int i = 0; i < adapter.getCount(); i++) {
					View view = mViews.get(i);

					adapter.getView(i, view, LineListView.this);
				}
			}

			@Override
			public void onInvalidated() {
				Log.d("LineListView", "DataSetObserver::onInvalidated");
			}
		});
	}

	/**
	 * 设置点击item时的listener.
	 * 
	 * @param listener
	 *            listener.
	 */
	public void setOnItemClickListener(OnItemClickListener listener) {
		mOnItemClickListener = listener;
	}

	// ---------------------------------------------------- Override methods
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (changed) {
			measuredSize(r - l, true);
		}
	};

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int specWidth = MeasureSpec.getSize(widthMeasureSpec); // 控件宽度
		int actualWidth = specWidth - SIDE_MARGIN * 2;// 实际宽度

		Point size = measuredSize(actualWidth, false);
		setMeasuredDimension(size.x, size.y);
	}

	// ---------------------------------------------------- Private methods
	private Point measuredSize(int actualWidth, boolean applyLayout) {
		Point size = new Point();

		int x = 0;// 横坐标
		int y = 0;// 纵坐标
		int rows = 1;// 总行数

		int childCount = getChildCount();
		for (int index = 0; index < childCount; index++) {
			View child = getChildAt(index);

			// measure
			child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

			// calculate x & y
			int width = child.getMeasuredWidth();
			int height = child.getMeasuredHeight();

			x += width + TEXT_MARGIN;
			if (x > actualWidth) {
				x = 0;
				rows++;
			}
			y = rows * (height + TEXT_MARGIN);

			// apply layout
			if (applyLayout) {
				child.layout(x - width, y - height, x, y);
			}
		}

		size.x = actualWidth;
		size.y = y;
		return size;
	}
}