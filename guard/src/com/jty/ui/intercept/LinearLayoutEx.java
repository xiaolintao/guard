package com.jty.ui.intercept;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Intercept TouchEvent.
 * 
 */
public class LinearLayoutEx extends LinearLayout {

	// ---------------------------------------------------- Private data
	private OnInterceptTouchEventListener listener;

	// -------------------------------------------------- Constructor & Setting
	public LinearLayoutEx(Context context) {
		super(context);
	}

	public LinearLayoutEx(Context context, AttributeSet attrs) {
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
