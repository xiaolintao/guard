package com.jty.ui.hscroll;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Head view.
 * 
 */
public class HScrollHeadView extends LinearLayout {

	// ---------------------------------------------------- Interface
	public interface OnSizeChangedListener {
		void onSizeChanged(int w, int h, int oldw, int oldh);
	}

	// ---------------------------------------------------- Private data
	private LinearLayout mFixedPanel;
	private int mFixedWidth = 0;

	private LinearLayout mMovablePanel;
	private int mMovableWidth = 0;

	private OnSizeChangedListener mOnSizeChangedListener;

	// ---------------------------------------------------- Constructor
	/**
	 * Constructor.
	 * 
	 * @param context
	 *            context.
	 */
	public HScrollHeadView(Context context) {
		this(context, null);
	}

	/**
	 * Constructor.
	 * 
	 * @param context
	 *            context.
	 * @param attrs
	 *            attributes.
	 */
	public HScrollHeadView(Context context, AttributeSet attrs) {
		super(context, attrs);

		init();
	}

	// ---------------------------------------------------- Override methods
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		if (mOnSizeChangedListener != null) {
			mOnSizeChangedListener.onSizeChanged(w, h, oldw, oldh);
		}
	}

	// ---------------------------------------------------- Public methods
	public LinearLayout getFixedPanel() {
		return mFixedPanel;
	}

	public LinearLayout getMovablePanel() {
		return mMovablePanel;
	}

	public void clear() {
		mFixedPanel.removeAllViews();
		mMovablePanel.removeAllViews();
	}

	/**
	 * 添加一个fixed的view.
	 * 
	 * @param view
	 *            view.
	 * @param width
	 *            view的宽度。
	 */
	public void addFixed(View view, int width) {
		setViewWidth(view, width);
		addView(mFixedPanel, view, width);

		mFixedWidth += width;
		setViewWidth(mFixedPanel, mFixedWidth);
	}

	/**
	 * 添加一个movable的view.
	 * 
	 * @param view
	 *            view.
	 * @param width
	 *            view的宽度。
	 */
	public void addMovable(View view, int width) {
		setViewWidth(view, width);
		addView(mMovablePanel, view, width);

		mMovableWidth += width;
		setViewWidth(mMovablePanel, mMovableWidth);
	}

	/**
	 * 设置listener.
	 * 
	 * @param listener
	 *            listener.
	 */
	public void setOnSizeChangedListener(OnSizeChangedListener listener) {
		mOnSizeChangedListener = listener;
	}

	// ---------------------------------------------------- Private methods
	/**
	 * initial.
	 */
	private void init() {
		setOrientation(HORIZONTAL);

		// fixed panel
		mFixedPanel = createHLinearLayout();
		addView(this, mFixedPanel, 0);

		// movable panel
		mMovablePanel = createHLinearLayout();
		addView(this, mMovablePanel, 0);
	}

	/**
	 * create horizontal LinearLayout.
	 * 
	 * @return LinearLayout.
	 */
	private LinearLayout createHLinearLayout() {
		LinearLayout layout = new LinearLayout(getContext());
		layout.setOrientation(LinearLayout.HORIZONTAL);

		return layout;
	}

	/**
	 * add view.
	 * 
	 * @param parent
	 *            parent.
	 * @param view
	 *            view.
	 * @param width
	 *            width of view.
	 */
	private void addView(LinearLayout parent, View view, int width) {
		parent.addView(view, new LayoutParams(width, LayoutParams.MATCH_PARENT));
	}

	/**
	 * set width of view.
	 * 
	 * @param view
	 *            view.
	 * @param width
	 *            width.
	 */
	private void setViewWidth(View view, int width) {
		ViewGroup.LayoutParams params = view.getLayoutParams();
		if (params == null) {
			params = new LayoutParams(width, LayoutParams.MATCH_PARENT);
		}

		params.width = width;
		view.setLayoutParams(params);
	}

}
