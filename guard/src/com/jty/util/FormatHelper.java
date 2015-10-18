package com.jty.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

/**
 * 数据格式。
 */
public class FormatHelper {

	// ---------------------------------------------------- Constants
	/** PATTERN */
	public final static String REGEX_EMAIL = "^([\\.a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\\.([a-zA-Z0-9_-])+)+$";
	public final static String REGEX_PHONE = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";

	// ---------------------------------------------------- Public methods
	/**
	 * 判断value是否是email。
	 */
	public static boolean isEmail(String value) {
		return matches(value, REGEX_EMAIL);
	}

	/**
	 * 判断value是否是手机号码。.
	 */
	public static boolean isPhone(String value) {
		return matches(value, REGEX_PHONE);
	}

	// ---------------------------------------------------- Private methods
	/**
	 * 判断字符串是否符合正则表达式。
	 * 
	 * @param value
	 *            字符串。
	 * @param regex
	 *            正则表达式。
	 * @return 是否符合。
	 */
	private static boolean matches(String value, String regex) {
		if (TextUtils.isEmpty(value)) {
			return false;
		}

		try {
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(value);
			return matcher.matches();
		} catch (Exception e) {
		}

		return false;
	}
}
