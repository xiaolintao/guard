package com.pplt.guard;

import java.text.DecimalFormat;

/**
 * 格式信息。
 */
public class FormatHelper {

	// ---------------------------------------------------- Constructor
	private FormatHelper() {
	}

	// ---------------------------------------------------- Public methods
	/**
	 * 转换：涨跌幅。
	 * 
	 * @param change
	 *            涨跌幅。
	 * @return "+1.01%"、"-1.01%"......
	 */
	public static String toChange(float change) {
		DecimalFormat df = new DecimalFormat("#0.00");

		return (change > 0.0f ? "+" + df.format(change) : df.format(change))
				+ "%";
	}

	/**
	 * 转换：金额。
	 * 
	 * @param amount
	 *            金额。
	 * @param plus
	 *            正值是否带+号。
	 * @return "+21.01"、"-21.01"......
	 */
	public static String toAmount(float amount, boolean plus) {
		DecimalFormat df = new DecimalFormat("#0.00");

		if (plus) {
			return amount > 0.0f ? "+" + df.format(amount) : df.format(amount);
		} else {
			return df.format(amount);
		}
	}

}
