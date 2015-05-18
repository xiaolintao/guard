package com.jty.ui.intercept;

import android.view.MotionEvent;

/**
 * Intercept TouchEvent.
 * 
 */
public interface OnInterceptTouchEventListener {
	/**
	 * intercept touch event.
	 * 
	 * @param event
	 *            touch event.
	 * @return event is intercepted or not.
	 */
	boolean onInterceptTouchEvent(MotionEvent event);
}
