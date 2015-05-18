package com.jty.ui.intercept;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Intercept TouchEvent.
 * 
 */
public class RelativeLayoutEx extends RelativeLayout {

	// ---------------------------------------------------- Private data
	private OnInterceptTouchEventListener listener;

	// -------------------------------------------------- Constructor & Setting
	public RelativeLayoutEx(Context context) {
		super(context);
	}

	public RelativeLayoutEx(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setOnInterceptTouchEventListener(
			OnInterceptTouchEventListener listener) {
		this.listener = listener;
	}

	// ---------------------------------------------------- Override methods
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (listener != null && listener.onInterceptTouchEvent(ev)) {
			return true;
		}

		return super.onInterceptTouchEvent(ev);
	}

}
