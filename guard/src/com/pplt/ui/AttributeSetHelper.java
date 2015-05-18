package com.pplt.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

/**
 * AttributeSet helper.
 * 
 */
public class AttributeSetHelper {

	// ---------------------------------------------------- Constructor
	private AttributeSetHelper() {
	}

	// ---------------------------------------------------- Public methods
	/**
	 * 获取自定义属性：resource id.
	 */
	public static int getResourceId(Context context, AttributeSet set,
			int[] attrs, int index, int defValue) {
		TypedArray array = null;

		try {
			array = context.obtainStyledAttributes(set, attrs);

			return array.getResourceId(index, defValue);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (array != null) {
				array.recycle();
				array = null;
			}
		}

		return defValue;
	}

	/**
	 * 获取android系统属性：resource id.
	 */
	public static int getResourceId(Context context, AttributeSet set,
			int index, int defValue) {
		int[] attrs = new int[] { index };

		return getResourceId(context, set, attrs, 0, defValue);
	}

	/**
	 * 获取自定义属性：string.
	 */
	public static String getString(Context context, AttributeSet set,
			int[] attrs, int index) {
		TypedArray array = null;

		try {
			array = context.obtainStyledAttributes(set, attrs);

			return array.getString(index);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (array != null) {
				array.recycle();
				array = null;
			}
		}

		return null;
	}

	/**
	 * 获取android系统属性：string.
	 */
	public static String getString(Context context, AttributeSet set, int index) {
		int[] attrs = new int[] { index };

		return getString(context, set, attrs, 0);
	}

	/**
	 * 获取自定义属性：boolean.
	 */
	public static boolean getBoolean(Context context, AttributeSet set,
			int[] attrs, int index, boolean defValue) {
		TypedArray array = null;

		try {
			array = context.obtainStyledAttributes(set, attrs);

			return array.getBoolean(index, defValue);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (array != null) {
				array.recycle();
				array = null;
			}
		}

		return defValue;
	}

	/**
	 * 获取android系统属性：boolean.
	 */
	public static boolean getBoolean(Context context, AttributeSet set,
			int index, boolean defValue) {
		int[] attrs = new int[] { index };

		return getBoolean(context, set, attrs, 0, defValue);
	}

	/**
	 * 获取自定义属性：integer.
	 */
	public static int getInt(Context context, AttributeSet set,
			int[] attrs, int index, int defValue) {
		TypedArray array = null;

		try {
			array = context.obtainStyledAttributes(set, attrs);

			return array.getInt(index, defValue);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (array != null) {
				array.recycle();
				array = null;
			}
		}

		return defValue;
	}

	/**
	 * 获取android系统属性：integer.
	 */
	public static int getInt(Context context, AttributeSet set, int index,
			int defValue) {
		int[] attrs = new int[] { index };

		return getInt(context, set, attrs, 0, defValue);
	}


	/**
	 * 获取自定义属性：dimension。
	 */
	public static float getDimension(Context context, AttributeSet set,
			int[] attrs, int index, float defValue) {
		TypedArray array = null;

		try {
			array = context.obtainStyledAttributes(set, attrs);

			return array.getDimension(index, defValue);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (array != null) {
				array.recycle();
				array = null;
			}
		}

		return defValue;
	}

	/**
	 * 获取android系统属性：dimension。
	 */
	public static float getDimension(Context context, AttributeSet set,
			int index, float defValue) {
		int[] attrs = new int[] { index };

		return getDimension(context, set, attrs, 0, defValue);
	}

	/**
	 * 获取自定义属性：color。
	 */
	public static int getColor(Context context, AttributeSet set, int[] attrs,
			int index, int defValue) {
		TypedArray array = null;

		try {
			array = context.obtainStyledAttributes(set, attrs);

			return array.getColor(index, defValue);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (array != null) {
				array.recycle();
				array = null;
			}
		}

		return defValue;
	}

	/**
	 * 获取android系统属性：color。
	 */
	public static int getColor(Context context, AttributeSet set, int index,
			int defValue) {
		int[] attrs = new int[] { index };

		return getColor(context, set, attrs, 0, defValue);
	}

	/**
	 * 获取自定义属性：size。
	 */
	public static float getSize(Context context, AttributeSet set, int[] attrs,
			int index, float defValue) {
		float size = getDimension(context, set, attrs, index, defValue);

		return pixelsToSp(context, size);
	}

	/**
	 * 获取android系统属性：size。
	 */
	public static float getSize(Context context, AttributeSet set, int index,
			float defValue) {
		int[] attrs = new int[] { index };

		return getSize(context, set, attrs, 0, defValue);
	}

	// ---------------------------------------------------- Private methods
	/**
	 * 转换：PIXEL=>SP.
	 * 
	 * @param context
	 *            context.
	 * @param px
	 *            PIXEL.
	 * @return SP.
	 */
	public static float pixelsToSp(Context context, float px) {
		float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
		return px / scaledDensity;
	}
}
