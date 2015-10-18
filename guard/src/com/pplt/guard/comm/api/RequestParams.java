package com.pplt.guard.comm.api;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 通信：请求参数。
 */
public class RequestParams {

	// ---------------------------------------------------- Private data
	Map<String, Object> mValues = new HashMap<String, Object>();

	// ---------------------------------------------------- Constructor
	/**
	 * 构造函数。
	 */
	public RequestParams() {
	}

	// ---------------------------------------------------- Override
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();

		Iterator<Entry<String, Object>> it = mValues.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();

			if (sb.length() != 0) {
				sb.append("&");
			}

			sb.append(entry.getKey());
			sb.append("=");
			if (entry.getValue() != null) {
				sb.append(entry.getValue());
			} else {
				sb.append("");
			}
		}

		return sb.toString();
	}

	// ---------------------------------------------------- Public methods
	/**
	 * 添加参数。
	 * 
	 * @param key
	 *            key。
	 * @param value
	 *            值。
	 */
	public void put(String key, int value) {
		mValues.put(key, Integer.valueOf(value));
	}

	public void put(String key, long value) {
		mValues.put(key, Long.valueOf(value));
	}

	public void put(String key, float value) {
		mValues.put(key, Float.valueOf(value));
	}

	public void put(String key, double value) {
		mValues.put(key, Double.valueOf(value));
	}

	public void put(String key, String value) {
		mValues.put(key, value);
	}
}
