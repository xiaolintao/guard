package com.jty.ui.hscroll;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Intercept水平划动的MotionEvent：
 * 
 * 1. 触发MovableViews中view的scrollBy(x,0)。
 * 
 * 2. 不再触发子view的TouchEvent.
 */
public class HScrollContainer extends LinearLayout {

	// ---------------------------------------------------- Constants
	private final static String TAG = "HScrollContainer";
	private final static float DISTANCE = 10.0f;

	// ---------------------------------------------------- Private data
	private float mTouchStartX = 0.0f;
	private float mTouchStartY = 0.0f;

	private boolean mScrollStarted = false;
	private int mScrollDirection = -1;

	private List<View> mMovableViews = new ArrayList<View>();

	private int mMovableTotalWidth = 0;
	private int mMovableVisibleWidth = 0;

	private int mStep = -1;



	// ---------------------------------------------------- Constructor
	/**
	 * Constructor.
	 * 
	 * @param context
	 *            context.
	 */
	public HScrollContainer(Context context) {
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
	public HScrollContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	// ---------------------------------------------------- Public methods
	/**
	 * set step length of scroll.
	 * 
	 * @param step
	 *            step length.
	 */
	public void setStep(int step) {
		mStep = step;
	}

	/**
	 * add movable view.
	 * 
	 * @param view
	 *            movable view.
	 */
	public void addMovableView(View view) {
		// add to list
		if (!mMovableViews.contains(view)) {
			mMovableViews.add(view);
		}

		// align
		int scrollX = getMoveableScrollX();
		if (view.getScrollX() != scrollX) {
			view.scrollTo(scrollX, 0);
		}
	}

	// ---------------------------------------------------- Override methods
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		Log.d(TAG, "onInterceptTouchEvent=" + ev.getAction());

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mScrollStarted = isInMoveable(ev);
			mScrollDirection = -1;

			mTouchStartX = ev.getX();
			mTouchStartY = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			if (mScrollStarted) {
				return onActionMove(ev);
			} else {
				break;
			}
		case MotionEvent.ACTION_UP:
			if (mScrollStarted) {
				return true;
			}
		default:
			break;
		}

		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.d(TAG, "onTouchEvent=" + event.getAction());

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_MOVE:
			return onActionMove(event);
		case MotionEvent.ACTION_UP:
			break;
		default:
			break;
		}
		return super.onTouchEvent(event);
	}

	// ---------------------------------------------------- Detect methods
	/**
	 * whether event is in movable.
	 * 
	 * @param event
	 *            event.
	 * @return in movable or not.
	 */
	private boolean isInMoveable(MotionEvent event) {
		for (View view : mMovableViews) {
			if (isInMoveable(event, view)) {
				return true;
			}
		}

		return false;
	}

	private boolean isInMoveable(MotionEvent event, View moveable) {
		if (moveable == null) {
			return false;
		}

		int[] location = new int[2];
		View parent = (View) moveable.getParent();
		parent.getLocationInWindow(location);

		RectF rect = new RectF();
		rect.left = moveable.getLeft() + location[0];
		rect.right = rect.left + moveable.getWidth();
		rect.top = location[1];
		rect.bottom = rect.top + parent.getHeight();

		return rect.contains(event.getX(), event.getY());
	}

	// ---------------------------------------------------- Scroll methods
	/**
	 * get scroll direction.
	 * 
	 * @param ev
	 *            Touch event.
	 * @return direction.
	 */
	private int getScrollDirection(MotionEvent ev) {
		if (mScrollDirection != -1) {
			return mScrollDirection;
		}

		float dx = Math.abs(ev.getX() - mTouchStartX);
		float dy = Math.abs(ev.getY() - mTouchStartY);
		if (dx < DISTANCE && dy < DISTANCE) {
			return -1;
		}

		mScrollDirection = dx > dy ? LinearLayout.HORIZONTAL
				: LinearLayout.VERTICAL;
		return mScrollDirection;
	}

	/**
	 * called when MotionEvent.ACTION_MOVE.
	 * 
	 * @param ev
	 *            Touch event.
	 * @return @see onInterceptTouchEvent.
	 */
	private boolean onActionMove(MotionEvent ev) {
		int direction = getScrollDirection(ev);

		if (direction == LinearLayout.HORIZONTAL) {
			scrollByInHorizontal(mTouchStartX - ev.getX());

			mTouchStartX = ev.getX();
			mTouchStartY = ev.getY();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * scroll ListView in HORIZONTAL.
	 * 
	 * @param distance
	 *            distance.
	 */
	private void scrollByInHorizontal(float distance) {
		int dx = adjust(distance);
		Log.d(TAG, "scrollByInHorizontal: distance=" + distance + ",dx=" + dx);


		// rows
		for (View movable : mMovableViews) {
			movable.scrollBy(dx, 0);
		}

		invalidate();
	}

	// ---------------------------------------------------- Adjust methods
	/**
	 * adjust horizontal scroll distance
	 * 
	 * @param distance
	 *            scroll distance.
	 * @return adjusted distance.
	 */
	private int adjust(float distance) {
		int dx = (int) distance;
		if (mStep > 0) {
			dx = distance > 0 ? mStep : -mStep;
		}

		int scrollX = getMoveableScrollX();
		int totalWidth = getMovableTotalWidth();
		int visibleWidth = getMovableVisibleWidth();
		if (mStep > 0) {
			visibleWidth = Math.min(mStep, visibleWidth);
		}
		Log.d(TAG, "adjust: scrollX=" + scrollX + ",totalWidth=" + totalWidth
				+ ",visibleWidth=" + visibleWidth + ", distance=" + distance);

		if (dx + scrollX < 0) {
			dx = 0;
		} else if (dx + scrollX + visibleWidth > totalWidth) {
			dx = totalWidth - scrollX - visibleWidth;
		}

		return dx;
	}

	/**
	 * get movable view.
	 * 
	 * @return movable view.
	 */
	private View getMovable() {
		if (mMovableViews != null && mMovableViews.size() > 0) {
			return mMovableViews.get(0);
		}

		return null;
	}

	/**
	 * get scroll value of movable view.
	 * 
	 * @return scroll value.
	 */
	private int getMoveableScrollX() {
		View moveable = getMovable();

		return moveable != null ? moveable.getScrollX() : 0;
	}

	/**
	 * get total width of movable view.
	 * 
	 * @return total width.
	 */
	private int getMovableTotalWidth() {
		if (mMovableTotalWidth == 0) {
			View moveable = getMovable();
			if (moveable != null) {
				mMovableTotalWidth = moveable.getMeasuredWidth();
			}
		}

		return mMovableTotalWidth;
	}

	/**
	 * get visible width of movable view.
	 * 
	 * @return visible width.
	 */
	private int getMovableVisibleWidth() {
		if (mMovableVisibleWidth == 0) {
			View moveable = getMovable();
			if (moveable != null) {
				View parent = (View) moveable.getParent();
				int width = parent.getWidth();
				int left = moveable.getLeft();

				mMovableVisibleWidth = width - left;
			}
		}

		return mMovableVisibleWidth;
	}
}
