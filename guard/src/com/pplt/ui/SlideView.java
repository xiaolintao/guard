package com.pplt.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * 滑动view.
 */
public class SlideView extends LinearLayout {

	// ---------------------------------------------------- Constants
	final static String TAG = "SlideView";

	// ---------------------------------------------------- interface
	public interface OnSlideListener {
		public static final int SLIDE_STATUS_OFF = 0;
		public static final int SLIDE_STATUS_START_SCROLL = 1;
		public static final int SLIDE_STATUS_ON = 2;

		public void onSlide(View view, int slideStatus);
	}

	// ---------------------------------------------------- Private data
	private Scroller mScroller;

	private int mHolderWidth;

	private int mLastX = 0; // TouchEvent最后的点
	private int mLastY = 0; // TouchEvent最后的点

	boolean mSliding = false; // 是否已开始slide

	private OnSlideListener mOnSlideListener; // listener

	// ---------------------------------------------------- Constructor
	public SlideView(Context context) {
		super(context);

		mScroller = new Scroller(context);
		setOrientation(LinearLayout.HORIZONTAL);
	}

	public SlideView(Context context, AttributeSet attrs) {
		super(context, attrs);

		mScroller = new Scroller(context);
		setOrientation(LinearLayout.HORIZONTAL);
	}

	// ---------------------------------------------------- Override methods
	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
	}

	// ---------------------------------------------------- Public methods
	public void setHolderWidth(int width) {
		mHolderWidth = width;
	}

	public void setOnSlideListener(OnSlideListener onSlideListener) {
		mOnSlideListener = onSlideListener;
	}

	public void smoothScrollTo(int destX, int destY) {
		int scrollX = getScrollX();
		if (scrollX == destX) {
			return;
		}

		int delta = destX - scrollX;
		mScroller.startScroll(scrollX, 0, delta, 0, Math.abs(delta) * 1);
		invalidate();
	}

	public boolean onRequireTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		int scrollX = getScrollX();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}
			if (mOnSlideListener != null) {
				mOnSlideListener.onSlide(this,
						OnSlideListener.SLIDE_STATUS_START_SCROLL);
			}
			mSliding = false;
			break;
		}
		case MotionEvent.ACTION_MOVE: {
			int deltaX = x - mLastX;
			int deltaY = y - mLastY;
			if (Math.abs(deltaX) < 10
					|| Math.abs(deltaX) < Math.abs(deltaY) * 2) {
				break;
			}

			int newScrollX = scrollX - deltaX;
			if (deltaX != 0) {
				if (newScrollX < 0) {
					newScrollX = 0;
				} else if (newScrollX > mHolderWidth) {
					newScrollX = mHolderWidth;
				}
				smoothScrollTo(newScrollX, 0);
			}
			mSliding = true;
			break;
		}
		case MotionEvent.ACTION_UP: {
			int newScrollX = 0;
			if (scrollX - mHolderWidth * 0.5 > 0) {
				newScrollX = mHolderWidth;
			}
			smoothScrollTo(newScrollX, 0);
			if (mOnSlideListener != null) {
				mOnSlideListener.onSlide(this,
						newScrollX == 0 ? OnSlideListener.SLIDE_STATUS_OFF
								: OnSlideListener.SLIDE_STATUS_ON);
			}
			break;
		}
		default:
			break;
		}

		mLastX = x;
		mLastY = y;

		return mSliding;
	}
}
