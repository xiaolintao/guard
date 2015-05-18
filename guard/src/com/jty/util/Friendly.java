package com.jty.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 友好显示。
 */
public class Friendly {

	// ---------------------------------------------------- Constants
	private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};

	// ---------------------------------------------------- Constructor
	private Friendly() {
	}

	// ---------------------------------------------------- Public methods
	/**
	 * 转换时间。
	 * 
	 * @param ldate
	 *            UNIX时间。
	 * @return
	 */
	public static String toTime(long ldate) {
		return toTime(new Date(ldate));
	}

	/**
	 * 转换时间。
	 * 
	 * @param time
	 *            时间。
	 * @return 结果。
	 */
	public static String toTime(Date time) {
		if (time == null) {
			return "";
		}

		String ftime = "";
		Calendar cal = Calendar.getInstance();
		// 判断是否是同一天
		String curDate = dateFormater2.get().format(cal.getTime());
		String paramDate = dateFormater2.get().format(time);
		if (curDate.equals(paramDate)) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0) {
				if ((cal.getTimeInMillis() - time.getTime()) / 60000 < 1) {
					ftime = "刚刚";
				} else {
					ftime = Math
							.max((cal.getTimeInMillis() - time.getTime()) / 60000,
									1)
									+ "分钟前";
				}
			} else {
				ftime = hour + "小时前";
			}
			return ftime;
		}

		long lt = time.getTime() / 86400000;
		long ct = cal.getTimeInMillis() / 86400000;
		int days = (int) (ct - lt);
		if (days == 0) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0) {
				if ((cal.getTimeInMillis() - time.getTime()) / 60000 < 1) {
					ftime = "刚刚";
				} else {
					ftime = Math
							.max((cal.getTimeInMillis() - time.getTime()) / 60000,
									1)
									+ "分钟前";
				}
			} else {
				ftime = hour + "小时前";
			}
		} else if (days == 1) {
			ftime = "昨天";
		} else if (days == 2) {
			ftime = "前天";
		} else if (days > 2 && days <= 10) {
			ftime = days + "天前";
		} else if (days > 10) {
			ftime = dateFormater2.get().format(time);
		}
		return ftime;
	}
}
