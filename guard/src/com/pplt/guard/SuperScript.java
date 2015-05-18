package com.pplt.guard;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.pplt.guard.R;
import com.pplt.ui.PreferenceItem;


/**
 * 角标。
 */
public class SuperScript {

	// ---------------------------------------------------- Constants
	/** PREF */
	private final static long SUPERSCRIPT_SETTING = 0x01; // 系统设置
	private final static long SUPERSCRIPT_ABOUT = 0x02; // 关于
	private final static long SUPERSCRIPT_VERSION = 0x04; // 版本

	// ---------------------------------------------------- Private data
	private static long mSuperScript;

	// ---------------------------------------------------- Trigger
	/**
	 * load.
	 */
	public static void load() {
		mSuperScript = Global.getSuperScript();
	}

	/**
	 * 提示：有新版本。
	 */
	public static void triggerNewVersion() {
		// 系统设置
		mSuperScript |= SUPERSCRIPT_SETTING;

		// about
		mSuperScript |= SUPERSCRIPT_ABOUT;

		// 版本
		mSuperScript |= SUPERSCRIPT_VERSION;

		save();
	}

	// ---------------------------------------------------- Public methods
	/**
	 * 是否提示：系统设置。
	 */
	public static boolean hintSetting() {
		return (mSuperScript & SUPERSCRIPT_SETTING) == SUPERSCRIPT_SETTING;
	}

	/**
	 * 已点击：系统设置。
	 */
	public static void settingClicked() {
		mSuperScript &= ~SUPERSCRIPT_SETTING;

		save();
	}

	/**
	 * 是否提示：关于。
	 */
	public static boolean hintAbout() {
		return (mSuperScript & SUPERSCRIPT_ABOUT) == SUPERSCRIPT_ABOUT;
	}

	/**
	 * 已点击：about
	 */
	public static void aboutClicked() {
		mSuperScript &= ~SUPERSCRIPT_ABOUT;

		save();
	}

	/**
	 * 是否提示：版本
	 */
	public static boolean hintVersion() {
		return (mSuperScript & SUPERSCRIPT_VERSION) == SUPERSCRIPT_VERSION;
	}

	/**
	 * 已点击：版本。
	 */
	public static void versionClicked() {
		mSuperScript &= ~SUPERSCRIPT_VERSION;

		save();
	}

	// ---------------------------------------------------- Public methods
	/**
	 * 设置item“角标”。
	 * 
	 * @param resId
	 *            item的resource id.
	 * @param text
	 *            文本。
	 */
	public static void show(View itemView, String text) {
		if (itemView == null || !(itemView instanceof PreferenceItem)) {
			return;
		}

		// 文字颜色&背景
		TextView textView = ((PreferenceItem) itemView).getItemTextView();
		textView.setTextColor(Color.WHITE);
		textView.setBackgroundResource(R.drawable.shape_superscript_red);

		// 内容
		if (text != null) {
			textView.setVisibility(View.VISIBLE);
			textView.setText(text);
		} else {
			textView.setVisibility(View.INVISIBLE);
		}
	}

	public static void showSub(View parent, int resid, String text) {
		View itemView = parent.findViewById(resid);

		show(itemView, text);
	}

	/**
	 * 设置item“角标”。
	 * 
	 * @param resId
	 *            item的resource id.
	 * @param count
	 *            数目。
	 */
	public static void show(View view, int count) {
		String text = count > 0 ? String.valueOf(count) : null;

		show(view, text);
	}

	public static void showSub(View parent, int resid, int count) {
		View itemView = parent.findViewById(resid);

		show(itemView, count);
	}

	/**
	 * 设置item“角标”：label。
	 * 
	 * @param resId
	 *            item的resource id.
	 * @param count
	 *            数目。
	 */
	public static void showLabel(View itemView, String text) {
		if (itemView == null || !(itemView instanceof PreferenceItem)) {
			return;
		}

		// 文字颜色&背景
		TextView textView = ((PreferenceItem) itemView).getItemLabelView();
		textView.setTextColor(Color.WHITE);
		textView.setBackgroundResource(R.drawable.shape_superscript_red);

		// 内容
		if (text != null) {
			textView.setVisibility(View.VISIBLE);
			textView.setText(text);
		} else {
			textView.setVisibility(View.INVISIBLE);
		}
	}

	public static void showLabelSub(View parent, int resid, String text) {
		View itemView = parent.findViewById(resid);

		showLabel(itemView, text);
	}

	// ---------------------------------------------------- Private methods
	private static void save() {
		Global.setSuperScript(mSuperScript);
	}
}
