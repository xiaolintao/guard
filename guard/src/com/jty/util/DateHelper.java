package com.jty.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;


/**
 * 日期时间转换。
 * 
 * @author Administrator
 * 
 */
@SuppressLint("SimpleDateFormat")
public class DateHelper {

	// ---------------------------------------------------- Constructor
	private DateHelper() {
	}

	// ---------------------------------------------------- helper
	/**
	 * 获取当前时间。
	 * 
	 * @param pattern
	 *            时间格式: 例如 yyyy-MM-dd。
	 * @return String。
	 */
	public static String getDatetime(String pattern) {
		return toString(new Date(), pattern);
	}

	/**
	 * long = > String
	 * 
	 * @param time
	 *            UNIX 时间。
	 * @param pattern
	 *            时间格式: 例如 yyyy-MM-dd。
	 * @return String.
	 */
	public static String toString(long time, String pattern) {
		Date date = toDate(time);
		if (date != null) {
			return toString(date, pattern);
		}

		return null;
	}

	/**
	 * String = > String
	 * 
	 * @param date
	 *            时间。
	 * @param from
	 *            时间格式: 例如 yy-MM-dd。
	 * @param to
	 *            to 时间格式: 例如 yyyy-MM-dd。
	 * @return String.
	 */
	public static String format(String date, String from, String to) {
		try {
			Date dt = new SimpleDateFormat(from).parse(String.valueOf(date));
			return new SimpleDateFormat(to).format(dt);
		} catch (ParseException e) {
			return "";
		}
	}

	// ---------------------------------------------------- *=> String
	/**
	 * date => String.
	 * 
	 * @param date
	 *            时间。
	 * @param pattern
	 *            时间格式: 例如 yyyy-MM-dd。
	 * @return String.
	 */
	public static String toString(Date date, String pattern) {
		try {
			SimpleDateFormat df = new SimpleDateFormat(pattern);
			return df.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	// ---------------------------------------------------- * => Date
	/**
	 * Sting=>Date
	 * 
	 * @param time
	 *            字符串表示的时间。
	 * @param pattern
	 *            时间格式: 例如 yyyy-MM-dd。
	 * @return 时间。
	 */
	public static Date toDate(String time, String pattern) {
		try {
			SimpleDateFormat df = new SimpleDateFormat(pattern);
			return df.parse(time);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * long = > Date
	 * 
	 * @param time
	 *            UNIX时间。
	 */
	public static Date toDate(long time) {
		try {
			return new Date(time);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
