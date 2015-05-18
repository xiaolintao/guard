package com.pplt.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pplt.guard.R;


/**
 * 排序的head item.
 */
public class SortHeadItem extends RelativeLayout {

	// ---------------------------------------------------- Constants
	/** 排序方式 */
	public final static int SORT_NORMAL = 0; // 未排序
	public final static int SORT_DES = 1; // 降序
	public final static int SORT_ASC = 2; // 升序

	public interface OnSortChangeListener {
		/**
		 * 排序变化。
		 * 
		 * @param sort
		 *            排序方式。
		 */
		void onSortChange(int sort);
	}

	// ---------------------------------------------------- Private data
	private int mSort; // sort
	private OnSortChangeListener mListener; // listener

	private String mText;
	private TextView mTextView;
	private int mDrawableWidth = 10; // drawable的宽度
	private int mDrawableHeight = 20; // drawable的高度

	// ---------------------------------------------------- Constructor
	public SortHeadItem(Context context) {
		super(context);

		init();
	}

	public SortHeadItem(Context context, AttributeSet attrs) {
		super(context, attrs);

		init();

		mDrawableWidth = (int) AttributeSetHelper.getDimension(context, attrs,
				R.styleable.SortHeadItem,
				R.styleable.SortHeadItem_drawableWidth, 0.0f);
		mDrawableHeight = (int) AttributeSetHelper.getDimension(context, attrs,
				R.styleable.SortHeadItem,
				R.styleable.SortHeadItem_drawableHeight, 0.0f);
	}

	/**
	 * 设置listener.
	 * 
	 * @param listener
	 *            listener.
	 */
	public void setOnSortChangeListener(OnSortChangeListener listener) {
		mListener = listener;
	}

	public void setText(String text) {
		if (mTextView != null) {
			mTextView.setText(text);
		}
		mText = text;
	}

	/**
	 * 设置排序方式。
	 * 
	 * @param sort
	 *            排序方式。
	 */
	public void setSort(int sort) {
		mSort = sort;

		showDrawable();
	}

	/**
	 * 获取排序方式。
	 * 
	 * @return 排序方式。
	 */
	public int getSort() {
		return mSort;
	}

	// ---------------------------------------------------- Private methods
	/**
	 * initial.
	 */
	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.sort_head_item,
				this, true);
		mTextView = (TextView) findViewById(R.id.tv);
		mTextView.setText(mText);

		setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mSort++;
				if (mSort >= 3) {
					mSort = 0;
				}

				showDrawable();

				if (mListener != null) {
					mListener.onSortChange(mSort);
				}
			}
		});

	}

	/**
	 * 显示drawable.
	 */
	private void showDrawable() {
		Drawable drawable = null;

		Resources res = getContext().getResources();
		if (mSort == SORT_DES) {
			drawable = res.getDrawable(R.drawable.sort_desending);
		} else if (mSort == SORT_ASC) {
			drawable = res.getDrawable(R.drawable.sort_ascending);
		}

		if (drawable != null) {
			drawable.setBounds(0, 0, mDrawableWidth, mDrawableHeight);
		}

		mTextView.setCompoundDrawables(null, null, drawable, null);
	}

}
